package cn.chriswood.anthill.infrastructure.web.utils

import cn.chriswood.anthill.infrastructure.core.logger
import cn.chriswood.anthill.infrastructure.core.utils.ExcelUtil
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object HttpExcelUtil {

    val log = logger<HttpExcelUtil>()

    /**
     * 导出excel
     */
    inline fun <reified T : Any> export(
        response: HttpServletResponse,
        fileName: String? = null,
        dataSource: ExcelUtil.DataSource<T>,
    ): Boolean {

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"
        // 这里URLEncoder.encode可以防止中文乱码
        val finalFileName: String = URLEncoder.encode(
            "${fileName ?: T::class.java.simpleName}-${System.currentTimeMillis()}.xlsx", StandardCharsets.UTF_8
        ).replace("\\+", "%20")
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename")
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''$finalFileName")
        response.setHeader("download-filename", finalFileName)

        return ExcelUtil.writeToStream(
            response.outputStream,
            dataSource,
            LongestMatchColumnWidthStyleStrategy()
        ) {
            log.error("HttpExcelUtil export error >>> {}", it.message)
        }
    }


    /**
     * 导入excel
     */
    inline fun <reified T : Any> import(
        file: MultipartFile,
        noinline readList: (list: List<T?>?) -> Unit
    ): Boolean {
        return ExcelUtil.readFromStream<T>(
            file.inputStream,
            readList
        ) {
            log.error("HttpExcelUtil import error >>> {}", it.message)
        }
    }
}
