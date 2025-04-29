package cn.chriswood.anthill.infrastructure.push.support

import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs

@NoArgs
class PushTemplateProperty(
    var supplier: String,
    var key: String,
) {
    var appKey: String? = null
    var secret: String? = null
}
