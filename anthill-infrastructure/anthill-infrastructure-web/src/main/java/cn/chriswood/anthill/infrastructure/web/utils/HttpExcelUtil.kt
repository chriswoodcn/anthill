package cn.chriswood.anthill.infrastructure.web.utils

import cn.chriswood.anthill.infrastructure.core.logger
import cn.hutool.json.JSONUtil
import com.alibaba.excel.EasyExcel
import com.alibaba.excel.ExcelWriter
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.exception.ExcelDataConvertException
import com.alibaba.excel.read.listener.ReadListener
import com.alibaba.excel.util.ListUtils
import com.alibaba.excel.write.handler.WriteHandler
import com.alibaba.excel.write.metadata.WriteSheet
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.io.OutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass


object HttpExcelUtil {

    val log = logger<HttpExcelUtil>()

    /**
     * 导出excel
     */
    fun <T : Any> export(
        response: HttpServletResponse,
        fileName: String? = null,
        dataSource: DataSource<T>,
        dataClass: KClass<T>
    ): Boolean {

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"
        // 这里URLEncoder.encode可以防止中文乱码
        val finalFileName: String = URLEncoder.encode(
            "${fileName ?: dataClass.java.simpleName}-${System.currentTimeMillis()}.xlsx", StandardCharsets.UTF_8
        ).replace("\\+", "%20")
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename")
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''$finalFileName")
        response.setHeader("download-filename", finalFileName)

        return writeToStream(
            response.outputStream,
            dataSource,
            dataClass
        ) {
            log.error("HttpExcelUtil export error >>> {}", it.message)
        }
    }


    /**
     * 导入excel
     */
    fun <T : Any> import(
        file: MultipartFile,
        readList: (list: List<T?>?) -> Unit,
        dataClass: KClass<T>
    ): Boolean {
        return readFromStream(
            file.inputStream,
            readList,
            dataClass
        ) {
            log.error("HttpExcelUtil import error >>> {}", it.message)
        }
    }

    const val BATCH_SIZE = 100

    class UploadDataListener<T>(
        private val batchSize: Int, val readList: (list: List<T?>?) -> Unit
    ) : ReadListener<T> {
        private val log = logger<UploadDataListener<T>>()
        private var cachedDataList: ArrayList<T> = ListUtils.newArrayListWithExpectedSize(batchSize)

        override fun invoke(data: T, context: AnalysisContext?) {
            log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
            cachedDataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size >= batchSize) {
                saveData();
                // 存储完成清理 list
                cachedDataList = ListUtils.newArrayListWithExpectedSize(batchSize);
            }
        }

        override fun doAfterAllAnalysed(context: AnalysisContext?) {
            saveData();
            log.debug("UploadDataListener -- 当前sheet数据解析完成！")
        }

        override fun onException(exception: Exception?, context: AnalysisContext?) {
            log.error("解析失败,继续解析下一行:{}", exception?.message)
            // 如果是某一个单元格的转换异常 能获取到具体行号
            // 如果要获取头的信息 配合invokeHeadMap使用
            if (exception is ExcelDataConvertException) {
                log.error(
                    "第{}行，第{}列解析异常，数据为:{}", exception.rowIndex, exception.columnIndex, exception.cellData
                )
            }
        }

        private fun saveData() {
            log.debug("{}条数据，开始存储数据库！", cachedDataList.size)
            readList(cachedDataList);
            log.debug("存储数据库成功！")
        }

    }

    fun <T : Any> readFromStream(
        inputStream: InputStream,
        readList: (list: List<T?>?) -> Unit,
        dataClass: KClass<T>,
        fail: ((Exception) -> Unit)?,
    ): Boolean {
        return try {
            EasyExcel.read(inputStream, dataClass.java, UploadDataListener<T>(BATCH_SIZE, readList)).doReadAll()
            true
        } catch (e: Exception) {
            if (fail != null) fail(e)
            false
        }
    }

    /**
     * 导出的数据源,分批写,减少内存开销
     */
    class DataSource<T>(
        var pageNum: Long,
        val pageSize: Long,
        val total: Long,
        val head: List<List<String>>?,
        val writeHandlers: List<WriteHandler>?,
        val fetchData: (Long, Long) -> List<T>?,
    ) {
        private var finishSize: Long = 0
        private var finished: Boolean = false

        /**
         * 是否已经导出完毕
         */
        fun hasFinished(): Boolean {
            return finished
        }

        /**
         * 获取一次数据
         */
        fun next(): List<T>? {
            val tempList = fetchData(pageNum, pageSize)
            if (tempList == null) {
                finished = true
                return null
            }
            pageNum += 1
            finishSize += tempList.size
            if (finishSize >= total) finished = true
            return tempList
        }
    }

    fun <T : Any> writeToStream(
        outputStream: OutputStream,
        dataSource: DataSource<T>,
        dataClass: KClass<T>,
        fail: ((Exception) -> Unit)
    ): Boolean {
        var excelWriter: ExcelWriter? = null
        var writeSheet: WriteSheet? = null
        return try {
            val writerBuilder = EasyExcel.write(outputStream).autoCloseStream(false)
            writerBuilder.registerWriteHandler(LongestMatchColumnWidthStyleStrategy())
            if (!dataSource.writeHandlers.isNullOrEmpty()) {
                dataSource.writeHandlers.forEach {
                    writerBuilder.registerWriteHandler(it)
                }
            }
            if (!dataSource.head.isNullOrEmpty()) {
                writerBuilder.head(dataClass.java).head(dataSource.head)
            } else {
                writerBuilder.head(dataClass.java)
            }
            excelWriter = writerBuilder.build()

            writeSheet = EasyExcel.writerSheet().build()

            while (!dataSource.hasFinished()) {
                val nextData = dataSource.next()
                nextData?.let {
                    excelWriter.write(it, writeSheet)
                }
            }
            true
        } catch (e: Exception) {
            log.error("HttpExcelUtil writeToStream error >>> {}", e.message)
            fail(e)
            false
        } finally {
            excelWriter?.close()
        }
    }
}
