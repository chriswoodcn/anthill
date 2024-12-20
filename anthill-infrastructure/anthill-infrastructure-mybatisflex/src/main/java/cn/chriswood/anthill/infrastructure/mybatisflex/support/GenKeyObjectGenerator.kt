package cn.chriswood.anthill.infrastructure.mybatisflex.support

import cn.hutool.core.util.IdUtil
import com.mybatisflex.core.keygen.IKeyGenerator

class GenKeyObjectGenerator : IKeyGenerator {
    override fun generate(entity: Any, keyColumn: String): Any {
        return IdUtil.objectId()
    }
}
