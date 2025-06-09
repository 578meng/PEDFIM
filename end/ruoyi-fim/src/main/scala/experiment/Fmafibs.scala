package experiment

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{HashPartitioner, SparkContext}
import sequential.Apriori.Itemset

import java.math.BigInteger
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break, breakable}

/**
 * @Author：ytl
 * @Package：experiment.Fmafibs
 * @Project：fimcode
 * @name：Fmafibs
 * @Date：2024/5/7 15:31
 * @Filename：Fmafibs
 */
class Fmafibs extends SparkFIM with Serializable  {
  override def findFrequentItemsets(transactions: RDD[Itemset], singletons: RDD[(String, Int)], minSupport: Int,
                                    spark: SparkSession, sc: SparkContext): List[Itemset] = {
    val rdd1 = transactions
    val support_value = minSupport

    val items = rdd1.flatMap(x=>x).map(x=>(x,1)).aggregateByKey(0)((x,y)=>x+y,(pair1,pair2)=>pair1+pair2)
      .filter(_._2 >= support_value).collect().sortBy(-_._2)

    val size = items.map(_._1).length

    val items_group = items.map(_._1).splitAt(5)

    val rdd2 = rdd1.map(x=> {
      val arr1 = items_group._1.intersect(x)
      val arr2 = items_group._2.intersect(x)
      (arr1,arr2)
    })

    val rdd3= rdd2.zipWithIndex().map(x=>(x._2,x._1)).partitionBy(new HashPartitioner(4))

    val rdd4 = rdd3.mapPartitions(x=>{
      val list = ListBuffer[(Long,String,String)]()
      while (x.hasNext){
        val elem = x.next()
        list.append(
          (elem._1,
            StringToBitString(items_group._1,elem._2._1),
            StringToBitString(items_group._2,elem._2._2)
          )
        )
      }
      list.iterator
    })

    val rdd5 = rdd4.mapPartitions(x=>{
      val list = ListBuffer[(Long,ListBuffer[BigInteger],ListBuffer[BigInteger])]()
      while (x.hasNext){
        val elem = x.next()
        list.append(
          (elem._1, bit_op(elem._2), bit_op(elem._3)
          )
        )
      }
      list.iterator
    })

    val rdd6 = rdd5.mapPartitions(x=>{
      val list = ListBuffer[(Long,ListBuffer[String],ListBuffer[String])]()
      while (x.hasNext){
        val elem = x.next()
        list.append((elem._1,
          elem._2.map(x=>BigIntegerToString(x,items_group._1)),
          elem._3.map(x=>BigIntegerToString(x,items_group._2)))
        )
      }
      list.iterator
    })

    val rdd7 = rdd6.map(x=>{
      val list = ListBuffer[String]()
      for{i2 <- x._2
          i3 <- x._3}
        list.append(i2 + i3)
      x._2.++(x._3).++(list)
    })

    val rdd8 = rdd7.flatMap(x=>x).map(x=>(x,1)).aggregateByKey(0)((x,y)=>x+y,(pair1,pair2)=>pair1+pair2)

    val rdd9 = rdd8.filter(_._2 >= support_value)

    val res = rdd9.map(_._1).map(_.split("-").toList).collect().toList

    res
  }

  //解析函数：整形转字符串方法，输入一个BigInteger型的数字和该数字对应的项的集合，返回结果字符串
  def BigIntegerToString(number: BigInteger,array:Array[String]): String = {
    var s = ""
    if(number != None && s.equals(BigInteger.ZERO) == false){
      val buffer = number.toString(2)
      val pre = array.length - buffer.length
      var i = 0
      var flag = 0
      breakable(
        while (i < buffer.length) {
          flag = buffer.indexOf( "1", i )
          if (flag != -1) {
            flag = flag + pre
            s = s + array.apply( flag ) + "-"
            flag = flag - pre
          }
          else {
            break()
          }
          i = flag + 1
        } )
    }
    s
  }
  //位操作函数
  def bit_op(bitstring: String) = {
    val set = ListBuffer[BigInteger]()
    if(bitstring.length>0){
      val a = new BigInteger(bitstring,2)
      var s = a.and(a.negate())
      while (
        s.equals(BigInteger.ZERO) == false
      ) {
        set.append(s)
        s = a.and(s.subtract(a))
      }
    }
    set
  }
  //字符串转位串
  def StringToBitString(s1: Array[String], s2: Array[String]):String = {
    var i = ""
    var index = 0L
    for (num <- s1) {
      index = s2.indexOf(num)
      if(index != -1){
        i = i + "1"
      }
      else{
        i = i + "0"
      }
    }
    i
  }
}
