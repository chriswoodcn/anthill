package cn.chriswood.anthill.infrastructure.web.utils

import cn.chriswood.anthill.infrastructure.core.logger
import cn.chriswood.anthill.infrastructure.core.utils.ExcelUtil
import cn.hutool.core.util.IdUtil
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object HttpExcelUtil {

    val log = logger<HttpExcelUtil>()

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

    /**
     * 导出excel
     */
    inline fun <reified T : Any> export(
        response: HttpServletResponse,
        fileName: String? = null,
        dataSource: DataSource<T>,
        noinline fail: ((Exception) -> Unit)?
    ) {
        try {
            response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            response.characterEncoding = StandardCharsets.UTF_8.name()
            // 这里URLEncoder.encode可以防止中文乱码
            val finalFileName: String = URLEncoder.encode(
                "${fileName ?: T::class.java.simpleName}${IdUtil.fastSimpleUUID()}",
                StandardCharsets.UTF_8
            )
                .replace("\\+", "%20")
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''$finalFileName.xlsx")
            while (!dataSource.hasFinished()) {
                val nextData = dataSource.next()
                nextData?.let {
                    ExcelUtil.writeToStream(
                        response.outputStream, "sheet1",
                        false,
                        dataSource.head,
                        dataSource.pageNum == 1,
                        it
                    )
                }
            }
        } catch (e: Exception) {
            log.error("HttpExcelUtil export error >>> {}", e.message)
            if (fail != null) fail(e)
        } finally {
            response.outputStream?.close()
        }
    }

    /**
     * 导入excel
     */
    inline fun <reified T : Any> import(file: MultipartFile, noinline readList: (list: List<T?>?) -> Unit): Boolean {
        return try {
            ExcelUtil.readFromStream<T>(file.inputStream, readList)
        } catch (e: Exception) {
            log.error("HttpExcelUtil import error >>> {}", e.message)
            false
        }
    }
}
