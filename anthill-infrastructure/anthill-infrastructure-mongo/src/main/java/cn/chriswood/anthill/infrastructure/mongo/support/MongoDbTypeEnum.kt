package cn.chriswood.anthill.infrastructure.mongo.support

enum class MongoDbTypeEnum(
    var code: String
) {
    SINGLE(Constants.SINGLE),
    Dynamic(Constants.DYNAMIC);

    companion object {
        fun getMongoDbTypeByCode(code: String): MongoDbTypeEnum {
            var enum = SINGLE
            entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
