package cn.chriswood.anthill.infrastructure.mail.support

import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication

class UserPassAuthenticator(
    private val user: String,
    private val pass: String
) : Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(this.user, this.pass)
    }
}
