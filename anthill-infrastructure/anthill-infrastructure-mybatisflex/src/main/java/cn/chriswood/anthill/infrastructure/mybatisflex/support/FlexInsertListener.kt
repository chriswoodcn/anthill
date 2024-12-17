package cn.chriswood.anthill.infrastructure.mybatisflex.support

import StpKit
import cn.chriswood.anthill.infrastructure.core.utils.ObjectUtil
import cn.chriswood.anthill.infrastructure.web.auth.AuthConfig
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import cn.hutool.extra.spring.SpringUtil
import com.mybatisflex.annotation.InsertListener
import java.time.LocalDateTime

class FlexInsertListener : InsertListener {
    override fun onInsert(entity: Any?) {
        when (entity) {
            is Create -> entity.createTime = LocalDateTime.now()
            is CreateBy -> {
                if (ObjectUtil.isNotNull(SpringUtil.getBean(AuthConfig::class.java)))
                    entity.createBy = AuthHelper.getUserId(StpKit.SysUser).toLong()
            }
        }
    }
}
