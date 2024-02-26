package cn.chriswood.anthill.example.mybatisflex.dto

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.time.LocalDateTime

@Table("sys_user", dataSource = "ds1")
class SysUserDto {
    @Id
    var userId: Long = 0L
    var username: String? = null
    var createTime: LocalDateTime? = null
}

