package cn.chriswood.anthill.example.mybatisflex.dto

import cn.chriswood.anthill.example.flex.support.ExcelHead
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated
import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.format.DateTimeFormat
import com.alibaba.excel.annotation.write.style.ContentStyle
import com.alibaba.excel.annotation.write.style.HeadFontStyle
import com.alibaba.excel.annotation.write.style.HeadStyle
import com.alibaba.excel.enums.poi.BorderStyleEnum
import com.alibaba.excel.enums.poi.FillPatternTypeEnum
import java.time.LocalDateTime

@NoArgs
@HeadStyle(
    fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND,
    fillForegroundColor = 57
)
@HeadFontStyle(fontHeightInPoints = 14)
@ContentStyle(
    borderLeft = BorderStyleEnum.THIN,
    borderRight = BorderStyleEnum.THIN,
    borderTop = BorderStyleEnum.THIN,
    borderBottom = BorderStyleEnum.THIN,
)
@ExcelIgnoreUnannotated
data class TUserDs1Data(
    @ExcelHead("zh@ID")
    @ExcelProperty
    var userId: Long = 0L,

    @ExcelHead("zh@用户名")
    @ExcelProperty
    var username: String = StringUtil.EMPTY,

    @ExcelHead("zh@密码")
    @ExcelProperty
    var password: String? = null,

    @ExcelHead("zh@创建日期")
    @ExcelProperty
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime? = null
)

