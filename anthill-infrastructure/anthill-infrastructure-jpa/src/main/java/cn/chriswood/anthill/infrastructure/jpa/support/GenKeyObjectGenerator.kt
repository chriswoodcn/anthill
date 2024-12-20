package cn.chriswood.anthill.infrastructure.jpa.support

import cn.hutool.core.util.IdUtil
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

class GenKeyObjectGenerator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Any {
        return IdUtil.objectId()
    }
}
