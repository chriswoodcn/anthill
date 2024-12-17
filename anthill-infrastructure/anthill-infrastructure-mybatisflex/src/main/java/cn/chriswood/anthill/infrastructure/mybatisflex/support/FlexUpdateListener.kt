package cn.chriswood.anthill.infrastructure.mybatisflex.support

import StpKit
import cn.chriswood.anthill.infrastructure.core.utils.ObjectUtil
import cn.chriswood.anthill.infrastructure.web.auth.AuthConfig
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import cn.hutool.extra.spring.SpringUtil
import com.mybatisflex.annotation.UpdateListener
import java.time.LocalDateTime

class FlexUpdateListener : UpdateListener {

    override fun onUpdate(entity: Any?) {

        when (entity) {
            is Update -> entity.updateTime = LocalDateTime.now()
            is UpdateBy -> {
                if (ObjectUtil.isNotNull(SpringUtil.getBean(AuthConfig::class.java)))
                    entity.updateBy = AuthHelper.getUserId(StpKit.SysUser).toLong()
            }
        }
    }
}
