package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.logger
import cn.hutool.json.JSONUtil
import com.alibaba.excel.EasyExcel
import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.exception.ExcelDataConvertException
import com.alibaba.excel.read.listener.ReadListener
import com.alibaba.excel.util.ListUtils
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy
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
                    "第{}行，第{}列解析异常，数据为:{}",
                    exception.rowIndex, exception.columnIndex, exception.cellData
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
        noinline readList: (list: List<T?>?) -> Unit
    ): Boolean {
        EasyExcel.read(inputStream, T::class.java, UploadDataListener<T>(BATCH_SIZE, readList)).doReadAll()
        return true
    }

    inline fun <reified T : Any> writeToStream(
        outputStream: OutputStream,
        sheetName: String,
        autoCloseStream: Boolean,
        head: List<List<String>>? = null,
        needHead: Boolean = true,
        data: List<T>
    ): Boolean {
        val excelWriterBuilder = EasyExcel.write(outputStream, T::class.java)
            .registerWriteHandler(LongestMatchColumnWidthStyleStrategy())
            .autoCloseStream(autoCloseStream)
        if (head != null) {
            excelWriterBuilder.head(head).needHead(needHead)
        }
        excelWriterBuilder.sheet(sheetName).doWrite(data)
        return true
    }

}
