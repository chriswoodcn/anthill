package cn.chriswood.anthill.mongo.support

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T, ID> : MongoRepository<T, ID>


// 参考：https://www.jianshu.com/p/3f4a80c26f92
// 参考：https://www.cnblogs.com/qlqwjy/p/8652555.html
// 参考：https://www.mongodb.com/docs/v4.4/reference/operator/aggregation-pipeline/
//aggregate([{管道:{表达式}}])
//mongodb常用管道
//1、$group：将集合中的文档分组，可用于统计结果
//      _id表示分组的依据，使用某个字段的格式为'$字段'
//      { $group: { _id: "userId", count: { "$sum": 1 } } }
//      { $group: { _id: null, count: { "$sum": 1 } } }
//2、$match：过滤数据，只输出符合条件的文档 类似于find,find不能统计,现在是可以过滤并统计
//      { $match: { $or: [ { score: { $gt: 70, $lt: 90 } }, { views: { $gte: 1000 } } ] } },
//      { $match: { name: '央视新闻',date:'2020-10-08',tag:{'$in':['视频','直播']}} },
//      *****先分组再将同组中的title放到一个数组中存到title字段，将所有列都添加到数组中用$push:$$ROOT
//      { $group: { _id: '$tag', title: { '$push': '$title'} } }
//3、$project：修改输入文档的结构，如重命名、增加、删除字段、创建计算结果
//      { $match: { name: '央视新闻',date:'2020-10-08',tag:{'$in':['视频','直播']}} },
//      { $group: { _id: "$tag", count: { "$sum": 1 } } },
//      { $project: { _id: 0, count:1} } 表示在结果中取count列，不取_id列。
//4、$sort：将输入文档排序后输出  1:升序排序 -1:降序排序
//      { $sort : { age : -1, posts: 1 } }
//5、$limit：限制聚合管道返回的文档数
//6、$skip：跳过指定数量的文档，并返回余下的文档
//7、$unwind：将数组类型的字段进行拆分
//  { $unwind: <field path> }
//  { $unwind:{
//  path: <field path>,
//  includeArrayIndex: <string>, string类型，可选的。用于保存元素的数组索引的新字段的名称
//  preserveNullAndEmptyArrays: <boolean> boolean类型，可选的。如果为真，如果路径为空、丢失或数组为空，则$unwind输出文档。如果为false，如果路径为空、丢失或数组为空，则$unwind不输出文档
//  }}
//8、$geoNear：输出接近某一地理位置的有序文档。
//9、$addFields：将新字段添加到文档，通过向输出文档添加新字段，该输出文档既包含输入文档中的现有字段，又包含新添加的字段
//  { $addFields:{ "specs.fuel_type": "unleaded" }}
//{ $addFields: { homework: { $concatArrays: [ "$homework", [ 7 ] ] } } }  利用$concatArrays表达式向现有homework数组字段添加元素7
//10、$facet：在同一组输入文档的单一阶段中处理多个聚合管道
//{
//    $facet: {
//    "categorizedByTags": [
//    { $unwind: "$tags" },
//    { $sortByCount: "$tags" }
//    ],
//    "categorizedByPrice": [
//    // Filter out documents without a price e.g., _id: 7
//    { $match: { price: { $exists: 1 } } },
//    { $bucket: {
//        groupBy: "$price",
//        boundaries: [ 0, 150, 200, 300, 400 ],
//        default: "Other",
//        output: {
//        "count": { $sum: 1 },
//        "titles": { $push: "$title" } } }
//    }],
//    "categorizedByYears(Auto)": [
//    {
//    $bucketAuto: {
//      groupBy: "$year",
//      buckets: 4 }
//    }]
//    }
//}
//11、$bucket：根据指定的表达式和bucket边界将传入的文档分类到称为bucket的组中，并为每个bucket输出一个文档
//{
//    $bucket: {
//        groupBy: <expression>,  用来对文档进行分组的表达式
//        boundaries: [ <lowerbound1>, <lowerbound2>, ... ], 一个基于groupBy表达式的值数组，该表达式指定每个bucket的边界。每一对相邻的值充当桶的包含下边界和独占上边界
//        default: <literal>,  可选的。指定附加bucket的_id的文字，该bucket包含groupBy表达式结果不属于边界指定的bucket的所有文档
//        output:{   可选的。除_id字段外，指定输出文档中要包含的字段的文档。
//                <output1>: { <$accumulator expression> },
//                <outputN>: { <$accumulator expression> } }
//    }
//}
//12、$bucketAuto：指定的表达式将传入的文档分类到特定数量的组(称为bucket)中。Bucket边界将自动确定，以便将文档平均分配到指定数量的Bucket中
//{
//    $bucketAuto: {
//        groupBy: <expression>,
//        buckets: <number>, 指定将输入文档分组到其中的桶数
//        output: {
//        <output1>: { <$accumulator expression> },
//        ...
//        }
//        granularity: <string>
//    }
//}

//13、$lookup：多表连接的操作，对同一数据库中的未分片集合执行左外连接
//14、$collStats：返回关于集合或视图的统计信息 必须是聚合管道的第一个阶段

//表达式:'$列名'
//常用表达式
//1、$sum：计算总和，$sum:1 同count(*)表示计数
//2、$avg：计算平均值
//3、$min：获取最小值
//4、$max：获取最大值
//5、$push：在结果文档中插入值到一个数组中
//6、$first：根据资源文档的排序获取第一个文档数据
//7、$last：根据资源文档的排序获取最后一个文档数据
//8、$addToSet：在结果文档中插入值到一个数组中，但不创建副本

//查询条件
//$gt:大于 $lt:小于 $gte:大于或等于 $lte:小于或等于 $ne:不或等于
//in 和 not in ($in $nin) $in: [2,4,6]
//$all $all: [ 2, 3, 4 ]
//取模运算$mod $mod: [ 10 , 1 ]
//$size 匹配数组内的元素数量 $size:5
//$exists 判断一个元素是否存在 a: { $exists: true }
//$type 基于 bson type来匹配一个元素的类型  a: { $type: 2 }; // matches if a is a string

//public interface OrderRepository extends CrudRepository<Order, String>, OrderRepositoryCustom {
//
//    @Aggregation("{ $group : { _id : $customerId, total : { $sum : 1 } } }")
//    List<OrdersPerCustomer> totalOrdersPerCustomer(Sort sort);
//
//    @Aggregation(pipeline = { "{ $match : { customerId : ?0 } }", "{ $count : total }" })
//    Long totalOrdersForCustomer(String customerId);
//
//    @Aggregation(pipeline = { "{ $match : { id : ?0 } }", "{ $unwind : { path : '$items' } }",
//        "{ $project : { id : 1 , customerId : 1 , items : 1 , lineTotal : { $multiply: [ '$items.price' , '$items.quantity' ] } } }",
//        "{ $group : { '_id' : '$_id' , 'netAmount' : { $sum : '$lineTotal' } , 'items' : { $addToSet : '$items' } } }",
//        "{ $project : { 'orderId' : '$_id' , 'items' : 1 , 'netAmount' : 1 , 'taxAmount' : { $multiply: [ '$netAmount' , 0.19 ] } , 'totalAmount' : { $multiply: [ '$netAmount' , 1.19 ] } } }" })
//    Invoice aggregateInvoiceForOrder(String orderId);
//}
