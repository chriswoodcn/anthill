package cn.chriswood.anthill.infrastructure.core.validate

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class PhoneNumber(
    val message: String = "{Validation.Invalid}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val isInternational: Boolean = true,
    val regx: String = "^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}\$"
) {
    class PhoneNumberValidator : ConstraintValidator<PhoneNumber, String> {
        private var annotation: PhoneNumber? = null
        override fun initialize(constraintAnnotation: PhoneNumber?) {
            annotation = constraintAnnotation
        }

        override fun isValid(p0: String?, p1: ConstraintValidatorContext?): Boolean {
            if (p0 == null || annotation == null) return true
            return if (annotation!!.isInternational) {
                val phoneNumberUtil = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneNumberUtil.parse(
                    p0,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name
                )
                phoneNumberUtil.isValidNumber(phoneNumber)
            } else {
                val regex = Regex(annotation!!.regx)
                regex.matches(p0)
            }
        }
    }
}
