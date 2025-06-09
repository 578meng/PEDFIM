package experiment

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import sequential.Apriori.Itemset

import java.math.BigInteger
import scala.collection.mutable.{ListBuffer, Map} // 可以在任何地方引入 可变集合

class demo2 extends SparkFIM with Serializable{

  override def findFrequentItemsets(transactions: RDD[Itemset], singletons: RDD[(String, Int)], minSupport: Int,
                                    spark: SparkSession, sc: SparkContext): List[Itemset] = {

    if(transactions.collect().length > 0){

      val sortedSingletons = singletons.collect.map(t => t._1)
      val res:ListBuffer[Itemset] = ListBuffer()
      transactions.map(t => pruneAndSort(t, sortedSingletons))
                  .filter(t=> t.size > 0)
                  .flatMap(buildConditionalPatternsBase)
                  .groupByKey(sortedSingletons.length).collect().toList
                  .foreach(s => {
                    res ++= minePatternFragment(ListBuffer(s._1),s._2.toList,minSupport)
                  })

      res.toList ++ sortedSingletons.map(List(_))

    }
    else
      List.empty[Itemset]
  }

  def pruneAndSort(transaction: Itemset, singletons: Seq[String]): Itemset = {
    transaction
      .filter(i => singletons.contains(i))
      .sortWith((a, b) => singletons.indexOf(a) < singletons.indexOf(b)) // TODO: anyway to speedup initial sorting?
  }

  def buildConditionalPatternsBase(transaction: Itemset): List[(String, Itemset)] = {
    (1 until transaction.size).map(i => (transaction(i), transaction.slice(0, i))).toList
  }

  def minePatternFragment(prefix: ListBuffer[String], conditionalPatterns: List[Itemset], minSupport: Int):ListBuffer[Itemset] = {

    val items = conditionalPatterns
      .flatMap(identity)
      .map(item => (item, 1))
      .groupBy(_._1)
      .map(item => {(item._1,item._2.size)})
      .filter(item => {item._2>=minSupport})

    if(items.size > 0 && items.size <= 24){

      val sortedSingletons = items.toList.map(t => t._1)

      var res:ListBuffer[Itemset] = ListBuffer()

      val bitTexts2:Map[BigInteger,Int] = Map()
      conditionalPatterns.foreach(cond => {
        bit_op(StringToBitString(cond,sortedSingletons)).foreach(temp =>{
          if(bitTexts2.contains(temp)) bitTexts2(temp) += 1
          else bitTexts2 += (temp -> 1)
        })
      })

      bitTexts2.foreach(bitText => {
        if(bitText._2 >= minSupport) {
          res += BitStringToString(prefix,bigIntegerToBitString(bitText._1,sortedSingletons.size),sortedSingletons)
        }
      })

      res
    }
    else if(items.size > 24) {
      val sortedSingletons = items.toList.map(t => t._1)

      var res:ListBuffer[Itemset] = ListBuffer()

      sortedSingletons.foreach(s => {
        res += (s +: prefix).toList
      })

      conditionalPatterns.map(t => pruneAndSort(t, sortedSingletons))
        .flatMap(buildConditionalPatternsBase)
        .groupBy(_._1)
        .map(item => {(item._1,item._2.map(_._2))})
        .foreach(t => {
          if(t._2.size >= minSupport)
            res ++= minePatternFragment(prefix :+ t._1,t._2,minSupport)
        })
      res
    }
    else
      ListBuffer.empty[Itemset]
  }

  //位操作函数
  def bit_op(bitstring: String) = {
    val set = ListBuffer[BigInteger]()
    if(bitstring != ""){
      val a = new BigInteger(bitstring,2)
      var s = a.and(a.negate())

      var i = 1

      print("\n*************************"+bitstring+"."+i+"************************")
      print("\na:::"+a.toString(2))
      println("\ns:::"+s.toString(2))

      while (
        s.equals(BigInteger.ZERO) == false
      ) {

        set.append(s)
        s = a.and(s.subtract(a))

        i+=1
        print("\n*************************"+bitstring+"."+i+"************************")
        print("\na:::"+a.toString(2))
        println("\ns:::"+s.toString(2))
      }
    }
    set
  }

  //字符串转位串
  def StringToBitString(text: Itemset, head: List[String]):String = {
    var bitText = ""
    for (str <- head) {
      if(text.indexOf(str) != -1) bitText = bitText + "1"
      else bitText = bitText + "0"
    }
    bitText
  }

  //字符串转位串
  def BitStringToString(prefix: String,bitString: String, head: List[String]):Itemset = {
    val list:ListBuffer[String] = ListBuffer()
    var i:Int = 0
    val headArray = head.toArray
    list += prefix
    while(i < bitString.length){
      if(bitString.charAt(i) == '1') list += headArray(i)
      i += 1
    }
    list.toList
  }

  //字符串转位串
  def BitStringToString(prefix: ListBuffer[String],bitString: String, head: List[String]):Itemset = {
    var i:Int = 0
    val headArray = head.toArray
    val list:ListBuffer[String] = ListBuffer()
    while(i < bitString.length){
      if(bitString.charAt(i) == '1') list += headArray(i)
      i += 1
    }
    (list ++: prefix).toList
  }

  def bigIntegerToBitString(bigInteger: BigInteger,len: Int): String ={
    val temp = bigInteger.longValue().toBinaryString
    val bitText = "0"*(len-temp.length) + temp
    bitText
  }

}
