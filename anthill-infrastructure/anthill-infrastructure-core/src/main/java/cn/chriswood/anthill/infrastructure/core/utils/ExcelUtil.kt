package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.logger
import cn.hutool.json.JSONUtil
import com.alibaba.excel.EasyExcel
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.exception.ExcelDataConvertException
import com.alibaba.excel.read.listener.ReadListener
import com.alibaba.excel.util.ListUtils
import com.alibaba.excel.write.handler.WriteHandler
import java.io.InputStream
import java.io.OutputStream

object ExcelUtil {

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

    inline fun <reified T : Any> readFromStream(
        inputStream: InputStream,
        noinline readList: (list: List<T?>?) -> Unit,
        noinline fail: ((Exception) -> Unit)?,
    ): Boolean {
        return try {
            EasyExcel.read(inputStream, T::class.java, UploadDataListener<T>(BATCH_SIZE, readList)).doReadAll()
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
        var pageNum: Int,
        val pageSize: Int,
        val total: Long,
        val head: List<List<String>>?,
        val fetchData: (Int, Int) -> List<T>?
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

    inline fun <reified T : Any> writeToStream(
        outputStream: OutputStream,
        dataSource: DataSource<T>,
        vararg writeHandlers: WriteHandler,
        noinline fail: ((Exception) -> Unit)?
    ): Boolean {
        val writerBuilder = EasyExcel.write(outputStream, T::class.java).autoCloseStream(false)
        if (writeHandlers.isNotEmpty()) {
            writeHandlers.forEach {
                writerBuilder.registerWriteHandler(it)
            }
        }
        if (dataSource.head != null) {
            writerBuilder.head(dataSource.head)
        }
        val excelWriter = writerBuilder.build()
        val writeSheet = EasyExcel.writerSheet().build()
        return try {
            while (!dataSource.hasFinished()) {
                val nextData = dataSource.next()
                nextData?.let {
                    excelWriter.write(it, writeSheet)
                }
            }
            true
        } catch (e: Exception) {
            if (fail != null) fail(e)
            false
        } finally {
            excelWriter.close()
        }
    }
}
