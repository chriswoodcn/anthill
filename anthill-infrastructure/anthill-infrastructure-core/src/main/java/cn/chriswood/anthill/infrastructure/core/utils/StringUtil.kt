package cn.chriswood.anthill.infrastructure.core.utils

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.Validator
import cn.hutool.core.text.AntPathMatcher
import cn.hutool.core.util.StrUtil
import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.max

object StringUtil {
    const val SEPARATOR = ","
    const val EMPTY = StringUtils.EMPTY
    val joinChar: (Iterable<String>, Char) -> String = StringUtils::join
    val join: (Iterable<String>, String) -> String = StringUtils::join
    val split: (String, Char) -> Array<String> = StringUtils::split
    val repeat: (Char, Int) -> String = StringUtils::repeat
    val rightPad: (String, Int, Char) -> String = StringUtils::rightPad
    val isNotBlank: (String?) -> Boolean = StringUtils::isNotBlank
    val isBlank: (String?) -> Boolean = StringUtils::isBlank
    val blankToDefault: (str: String?, defaultValue: String?) -> String = StrUtil::blankToDefault
    val isEmpty: (str: String?) -> Boolean = StrUtil::isEmpty
    val trimToEmpty: (str: String) -> String = StringUtils::trimToEmpty
    val containsAny: (String?, String?) -> Boolean = StringUtils::containsAny
    val startsWith: (String?, String?) -> Boolean = StringUtils::startsWith
    val startsWithAny: (String?, Array<String?>?) -> Boolean = StringUtils::startsWithAny
    val endsWith: (String?, String?) -> Boolean = StringUtils::endsWith
    val endsWithAny: (String?, Array<String?>?) -> Boolean = StringUtils::endsWithAny
    val equalsAnyIgnoreCase: (String?, Array<String?>?) -> Boolean = StringUtils::equalsAnyIgnoreCase

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 去空格
     */
    val trim: (str: String?) -> String = StrUtil::trim

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    fun substring(str: String, start: Int): String {
        return substring(str, start, str.length)
    }

    /**
     * 截取字符串
     */
    val substring: (str: String?, start: Int, end: Int) -> String = StrUtil::sub

    /**
     * 格式化文本, {} 表示占位符<br></br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br></br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br></br>
     * 例：<br></br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br></br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is {} for a<br></br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br></br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    fun format(template: String?, vararg params: Any?): String {
        return StrUtil.format(template, *params)
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    fun ishttp(link: String?): Boolean {
        return Validator.isUrl(link)
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    fun str2Set(str: String, sep: String): Set<String> {
        return HashSet(str2List(str, sep, true, false))
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    fun str2List(str: String, sep: String, filterBlank: Boolean, trim: Boolean): List<String> {
        val list: MutableList<String> = ArrayList()
        if (isEmpty(str)) {
            return list
        }

        // 过滤空白字符串
        if (filterBlank && StringUtils.isBlank(str)) {
            return list
        }
        val split = str.split(sep.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (string in split) {
            var temp = string
            if (filterBlank && StringUtils.isBlank(string)) {
                continue
            }
            if (trim) {
                temp = trim(string)
            }
            list.add(temp)
        }
        return list
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串同时串忽略大小写
     *
     * @param cs                  指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    fun containsAnyIgnoreCase(cs: CharSequence?, vararg searchCharSequences: CharSequence?): Boolean {
        return StrUtil.containsAnyIgnoreCase(cs, *searchCharSequences)
    }

    /**
     * 驼峰转下划线命名
     */
    fun toUnderScoreCase(str: String?): String {
        return StrUtil.toUnderlineCase(str)
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    fun inStringIgnoreCase(str: String?, vararg strs: String?): Boolean {
        return StrUtil.equalsAnyIgnoreCase(str, *strs)
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    fun convertToCamelCase(name: String?): String {
        return StrUtil.upperFirst(StrUtil.toCamelCase(name))
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    fun toCamelCase(s: String?): String {
        return StrUtil.toCamelCase(s)
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    fun matches(str: String?, strs: List<String?>): Boolean {
        if (isEmpty(str) || CollUtil.isEmpty(strs)) {
            return false
        }
        for (pattern in strs) {
            if (isMatch(pattern, str)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     */
    fun isMatch(pattern: String?, url: String?): Boolean {
        val matcher = AntPathMatcher()
        return matcher.match(pattern!!, url!!)
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    fun padl(num: Number, size: Int): String {
        return padl(num.toString(), size, '0')
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    fun padl(s: String?, size: Int, c: Char): String {
        val sb = StringBuilder(size)
        if (s != null) {
            val len = s.length
            if (s.length <= size) {
                sb.append(c.toString().repeat(size - len))
                sb.append(s)
            } else {
                return s.substring(len - size, len)
            }
        } else {
            sb.append(c.toString().repeat(max(0.0, size.toDouble()).toInt()))
        }
        return sb.toString()
    }

    /**
     * 切分字符串(分隔符默认逗号)
     *
     * @param str 被切分的字符串
     * @return 分割后的数据列表
     */
    fun splitList(str: String?): List<String> {
        return splitTo(str) { value: Any? ->
            Convert.toStr(
                value
            )
        }
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 分割后的数据列表
     */
    fun splitList(str: String?, separator: String?): List<String> {
        return splitTo(
            str, separator
        ) { value: Any? ->
            Convert.toStr(
                value
            )
        }
    }

    /**
     * 切分字符串自定义转换(分隔符默认逗号)
     *
     * @param str    被切分的字符串
     * @param mapper 自定义转换
     * @return 分割后的数据列表
     */
    fun <T> splitTo(str: String?, mapper: Function<in Any?, T>?): List<T> {
        return splitTo<T>(str, SEPARATOR, mapper)
    }

    /**
     * 切分字符串自定义转换
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @param mapper    自定义转换
     * @return 分割后的数据列表
     */
    fun <T> splitTo(str: String?, separator: String?, mapper: Function<in Any?, T>?): List<T> {
        return if (StringUtils.isBlank(str)) {
            ArrayList(0)
        } else StrUtil.split(str, separator).stream().filter { obj: String? -> Objects.nonNull(obj) }.map(mapper)
            .collect(Collectors.toList())
    }
}
