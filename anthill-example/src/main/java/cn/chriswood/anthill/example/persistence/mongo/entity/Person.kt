//package cn.chriswood.anthill.example.persistence.mongo.entity
//
//import cn.chriswood.anthill.infrastructure.core.utils.DateUtil
//import org.springframework.data.annotation.Transient
//import org.springframework.data.mongodb.core.mapping.Document
//import java.time.LocalDateTime
//import java.time.ZonedDateTime
//
//@Document("test_person")
//class Person {
//    lateinit var id: String
//    var userId: Long? = null
//    var username: String? = null
//    var birth: ZonedDateTime? = null
//        set(value) {
//            field = value
//            if (field != null)
//                this.birthTime = DateUtil.tranUTCTime2LocalDateTime(field!!)
//        }
//
//    @Transient
//    var birthTime: LocalDateTime? = null
//}
