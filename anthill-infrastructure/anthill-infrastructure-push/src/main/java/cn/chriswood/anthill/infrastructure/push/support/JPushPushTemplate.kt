package cn.chriswood.anthill.infrastructure.push.support

import cn.hutool.core.map.MapUtil
import cn.jiguang.sdk.api.PushApi
import cn.jiguang.sdk.bean.push.PushSendParam
import cn.jiguang.sdk.bean.push.audience.Audience
import cn.jiguang.sdk.bean.push.message.notification.NotificationMessage
import cn.jiguang.sdk.bean.push.options.Options
import cn.jiguang.sdk.constants.ApiConstants
import org.slf4j.LoggerFactory

class JPushPushTemplate(
    private val pushAccountProperty: PushTemplateProperty
) : PushTemplate {

    private val log = LoggerFactory.getLogger(javaClass)

    private val client: PushApi? = try {
        PushApi.Builder()
            .setAppKey(pushAccountProperty.appKey!!)
            .setMasterSecret(pushAccountProperty.secret!!)
            .build();
    } catch (e: Exception) {
        log.error(
            "JPushPushTemplate client build failed, push key: ${pushAccountProperty.key}"
        )
        null
    }

    override fun sendPush(message: Map<String, Any>) {
        val pushSendParam: PushSendParam = buildPushObjectAndroidAndIos(message)
        try {
            val pushSendResult = client!!.send(pushSendParam)
            log.debug("JPushPushTemplate sendPush result: {}", pushSendResult)
        } catch (e: java.lang.Exception) {
            log.error("JPushPushTemplate sendPush Exception: {}", e.message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildPushObjectAndroidAndIos(message: Map<String, Any>): PushSendParam {
        val param = PushSendParam()

        val title = MapUtil.getStr(message, "title")
        val content = MapUtil.getStr(message, "content")
        val jpushRegistrationId = MapUtil.getStr(message, "jpushRegistrationId")
        val jpushRegistrationIds = message["jpushRegistrationIds"] as List<String>?
        val extras = message["extras"] as Map<String, Any>?

        val audience = Audience()
        if (!jpushRegistrationIds.isNullOrEmpty()) {
            audience.registrationIdList = jpushRegistrationIds
        }
        if (!jpushRegistrationId.isNullOrBlank()) {
            audience.registrationIdList = listOf(jpushRegistrationId)
        }
        param.audience = audience

        param.platform = ApiConstants.Platform.ALL

        val android = NotificationMessage.Android()
        android.alert = content
        android.title = title
        android.extras = extras
        val ios = NotificationMessage.IOS()
        val iosAlert: MutableMap<String, String> = HashMap()
        iosAlert["title"] = title
        iosAlert["subtitle"] = content
        ios.alert = iosAlert
        ios.extras = extras
        val hmos = NotificationMessage.HMOS()
        hmos.alert = content
        hmos.title = title
        hmos.extras = extras
        val notificationMessage = NotificationMessage()
        notificationMessage.alert = content
        notificationMessage.android = android
        notificationMessage.ios = ios
        notificationMessage.hmos = hmos
        param.notification = notificationMessage

        val options = Options()
        options.timeToLive = 43200L
        options.apnsProduction = false
        param.options = options

        return param
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getClient(): T? {
        return client as T?
    }

    override fun getKey(): String {
        return pushAccountProperty.key
    }

    override fun getSupplier(): String {
        return pushAccountProperty.supplier
    }
}
