package cn.chriswood.anthill.infrastructure.mybatisflex.support

import cn.hutool.core.util.IdUtil
import com.mybatisflex.core.keygen.IKeyGenerator

class GenKeyUUIDGenerator : IKeyGenerator {
    override fun generate(entity: Any, keyColumn: String): Any {
        return IdUtil.simpleUUID()
    }
}
