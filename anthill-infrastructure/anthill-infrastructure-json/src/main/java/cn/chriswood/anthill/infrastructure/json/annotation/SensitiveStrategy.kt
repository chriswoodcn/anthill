package cn.chriswood.anthill.infrastructure.json.annotation

import cn.hutool.core.util.DesensitizedUtil

enum class SensitiveStrategy(
    val desensitizer: (String?) -> (String?)
) {
    PASSWORD(DesensitizedUtil::password),

    ID_CARD({ s -> DesensitizedUtil.idCardNum(s, 3, 4) }),

    CHINESE_NAME(DesensitizedUtil::chineseName),

    MOBILE(DesensitizedUtil::mobilePhone),

    TEL(DesensitizedUtil::fixedPhone),

    ADDRESS({ s -> DesensitizedUtil.address(s, 8) }),

    EMAIL(DesensitizedUtil::email),

    BANK_CARD(DesensitizedUtil::bankCard);
}
