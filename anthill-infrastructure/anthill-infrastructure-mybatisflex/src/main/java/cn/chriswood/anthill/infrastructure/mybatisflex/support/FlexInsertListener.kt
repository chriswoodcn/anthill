package cn.chriswood.anthill.infrastructure.mybatisflex.support

import StpKit
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import com.mybatisflex.annotation.InsertListener
import java.time.LocalDateTime

class FlexInsertListener : InsertListener {
    override fun onInsert(entity: Any?) {
        if (entity is Create) {
            entity.createTime = LocalDateTime.now()
        }
        if (entity is CreateBy) {
            entity.createBy = AuthHelper.getUserId(StpKit.SysUser).toLong()
        }
    }
}
