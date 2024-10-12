package cn.chriswood.anthill.infrastructure.mybatisflex.support

import StpKit
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import com.mybatisflex.annotation.UpdateListener
import java.time.LocalDateTime

class FlexUpdateListener : UpdateListener {

    override fun onUpdate(entity: Any?) {
        if (entity is Update) {
            entity.updateTime = LocalDateTime.now()
        }
        if (entity is UpdateBy) {
            entity.updateBy = AuthHelper.getUserId(StpKit.SysUser).toLong()
        }
    }
}
