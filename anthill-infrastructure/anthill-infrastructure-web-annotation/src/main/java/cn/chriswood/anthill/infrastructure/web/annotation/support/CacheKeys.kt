package cn.chriswood.anthill.infrastructure.web.annotation.support

object CacheKeys {
    private const val GLOBAL_CACHE_KEY = "global:"

    const val REPEAT_SUBMIT_KEY = GLOBAL_CACHE_KEY + "repeat_submit:"

    const val RATE_LIMIT_KEY: String = GLOBAL_CACHE_KEY + "rate_limit:"
}
