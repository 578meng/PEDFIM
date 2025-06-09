package spark

import breeze.numerics.pow
import experiment.SparkFIM
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import sequential.Apriori.Itemset
import sequential.Util
import sequential.Util.absoluteSupport
import sequential.fpgrowth.{FPGrowth, FPNode, FPTree}

import java.math.BigInteger
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @Author：ytl
 * @Package：spark
 * @Project：SPEFIM
 * @name：SPEFIM
 * @Date：2024/5/7 11:34
 * @Filename：SPEFIM
 */
class SPEFIM extends SparkFIM with Serializable  {

  val maxLen = 10

  def main(args: Array[String]): Unit = {

    /**
     * 创建spark
     */
    var spark: SparkSession = null
    val appName = Util.appName
    spark
      = SparkSession.builder()
      .appName(appName)
      .master("local[*]")
      //.config("spark.eventLog.enabled", "true")
      .getOrCreate()

    val sc
    = spark
      .sparkContext
    sc.setLogLevel("WARN")

    /**
     * 文件读取
     */
    var transactionsRDD: RDD[Itemset] = null
    val path
    //    = getClass.getResource("/datasets/shop.txt").getPath
    = getClass.getResource("/datasets/mushroom.txt").getPath
    //      = getClass.getResource("/datasets/chess.txt").getPath
    //    = getClass.getResource("/datasets/T10I4D100K.txt").getPath
    //    = getClass.getResource("/datasets/kosarak.txt").getPath
    //      = getClass.getResource("/datasets/retail.txt").getPath
    //          = getClass.getResource("/datasets/pamp.txt").getPath
    //          = getClass.getResource("/datasets/Chainstore.txt").getPath

    val file = List.fill(Util.replicateNTimes)(path).mkString(" ")
    var fileRDD: RDD[String] = null
    fileRDD = sc.textFile(file)

    transactionsRDD
      = fileRDD
      .filter(!_.trim.isEmpty)
      .map(_.split(" " + "+"))
      .map(l => l.map(_.trim).toList)
      .cache()
      .repartition(10)
    val support
    = absoluteSupport(0.3, transactionsRDD.count().toInt)


    val sortedSingletons
    = transactionsRDD
      .flatMap(identity)
      .map(item => (item, 1))
      .reduceByKey(_ + _)
      .filter(_._2 >= support)

    val start = System.currentTimeMillis()
    val res = findFrequentItemsets(transactionsRDD, sortedSingletons, support, spark, sc)
    val end = System.currentTimeMillis()

    res.foreach(println)
    println("number:" + res.length)
    println("time:" + (end - start) * 1.0 / 1000)
  }

  override def findFrequentItemsets(transactions: RDD[Itemset], singletons: RDD[(String, Int)], minSupport: Int,
                           spark: SparkSession, sc: SparkContext): List[Itemset] = {
    // Generate singletons
    val sortedSingletons = singletons.collect.map(t => t._1)
    //    println(sortedSingletons.length)
    //    sortedSingletons.foreach(s=>print(s+" "))
    //    println(sortedSingletons)
    //    singletons.collect().foreach(println)

    if (sortedSingletons.nonEmpty) {
      transactions
        //        .repartition(8)
        .mapPartitions(_.map(pruneAndSort(_, sortedSingletons)))
        .map(t => pruneAndSort(t, sortedSingletons))
        .flatMap(buildConditionalPatternsBase)
        .groupByKey(sortedSingletons.length)
        //        .collect()
        .flatMap(t => minePatternFragment(t._1, t._2.toList, minSupport))
        .collect()
        .toList ++
        List(List(sortedSingletons(0)))
      //      ++ sortedSingletons.map(List(_))
    }
    else
      List.empty[Itemset]
  }

  def printTree(root: FPNode, seq: String): Unit = {
    if (root != null) {
      println(seq + "" + root.item + ":" + root.support)
      root.children.foreach(child => {
        printTree(child, seq + "-")
      })
    }
  }

  def minePatternFragment(prefix: String
                          , conditionalPatterns: List[Itemset]
                          , minSupport: Int
                         ): List[Itemset] = {
    val fpGrowth = new FPGrowth
    val singletons = mutable.LinkedHashMap(fpGrowth.findSingletons(conditionalPatterns, minSupport).map(i => i -> Option.empty[FPNode]): _*)
    val preItem = new ListBuffer[String]()
    preItem.append(prefix)
    val preList = new ListBuffer[List[String]]()
    preList.append(preItem.toList)
    //    println("*************************后缀项***************************")
    //    println(prefix)
    //    println("*************************条件基***************************")
    //    conditionalPatterns.foreach(println)
    //    println("*************************条件树***************************")
    //    printTree(condFPTree.rootNode,"")

    var res = List[Itemset]()

    if (singletons.keys.size > maxLen) {
      res = conditionalPatterns
        .map(cp => pruneAndSort(cp, singletons.keys.toList))
        .flatMap(buildConditionalPatternsBase)
        .groupBy(_._1)
        .flatMap(cp => minePatternFragment(cp._1, cp._2.map(cp2 => cp2._2), minSupport))
        .toList
        .map(_ :+ prefix) ++ List(List(prefix)) ++
        List(List(singletons.keys.toList(0)) ++ List(prefix))
    }
    else {
      val condFPTree = new FPTree(conditionalPatterns.map((_, 1)), minSupport, singletons)
      val prefixes = fpGrowth.generatePrefixes(List(prefix), singletons.keySet)
      res = bitOp(new BigInteger((pow(2, singletons.keys.toList.size) - 1).toString, 10), singletons.keys.toList, singletons, prefix, minSupport)
        .map(_ :+ prefix) ++ List(List(prefix))
      //      ++ List(List(singletons.keys.toList(0)))
      //      ++ preList.toList
    }
    //    else res = res ++ List(List(prefix))

    //    println("*************************"+prefix+"项集***************************")
    //    res.foreach(println)
    //    println("*************************"+prefix+"项集***************************")
    res

  }

  def showCapital(x: Option[FPNode]) = x match {
    case Some(s) => s
    case None => null
  }

  def bitOp(bit: BigInteger
            , preList: List[String]
            , singletons: mutable.LinkedHashMap[String, Option[FPNode]]
            , pre: String
            , minSupport: Int) = {

    val res = new ListBuffer[Itemset]()
    val a = bit
    var s = a.and(a.negate())
    //    println("*************************编码***************************")
    //    println(s.toString(2))

    while (s.equals(BigInteger.ZERO) == false) {
      var tempStr = s.toString(2)
      var itemset = new ListBuffer[String]()
      val diff = singletons.size - tempStr.length
      while (tempStr.indexOf("1") != -1) {
        itemset.append(preList(diff + tempStr.indexOf('1')))
        tempStr = tempStr.replaceFirst("1", "0")
      }
      //      println("itemset:"+itemset)

      if (itemset.length == 1) {
        //        itemset.append(pre)
        res.append(itemset.toList)
      }
      else if (itemset.length > 1) {
        itemset = itemset.reverse
        var root = showCapital(singletons(itemset(0)))
        var sup = 0

        while (root != null) {
          //          println("root:"+root)
          var tree = root.parent
          itemset
            .tail
            .foreach(i => {
              while (tree.item != null && !tree.item.equals(i)) {
                //              println("tree:"+tree)
                tree = tree.parent
              }
            })
          if (tree.item != null && tree.item.equals(itemset.last)) {
            sup += root.support
          }
          root = root.itemLink
        }
        if (sup >= minSupport) {
          //          pre.split(",").foreach(ps=>{
          //
          //          })
          //          itemset.append(pre)
          res.append(itemset.toList)
        }
      }

      s = a.and(s.subtract(a))
    }
    res.toList
  }

  /**
   * in: f,c,a,m,p
   * out:
   * p -> f,c,a,m
   * m -> f,c,a
   * a -> f,c
   * c -> f
   */
  def buildConditionalPatternsBase(transaction: Itemset): List[(String, Itemset)] = {
    (1 until transaction.size).map(i => (transaction(i), transaction.slice(0, i))).toList
  }

  def pruneAndSort(transaction: Itemset, singletons: Seq[String]) = {
    singletons.intersect(transaction).toList
  }

}
