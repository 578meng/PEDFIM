package biye

import breeze.numerics.pow
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

object SPEFIM {

  val maxLen = 10

  def spefimfindFrequentItemsets(transactions: RDD[Itemset], singletons: RDD[(String, Int)], minSupport: Int, total:Int,
                                    spark: SparkSession, sc: SparkContext): List[FI] = {

    val sortedSingletons = singletons.collect.map(t => t._1)
    val slfi = singletons.map(t => new FI(List(t._1),t._2*1.0/total)).collect().toList
    val slfimap = singletons.map(t=>(t._1,t._2*1.0/total)).collect().toMap

    if (sortedSingletons.nonEmpty) {
      transactions
        .mapPartitions(_.map(pruneAndSort(_, sortedSingletons)))
        .map(t => pruneAndSort(t, sortedSingletons))
        .flatMap(buildConditionalPatternsBase)
        .groupByKey(sortedSingletons.length)
        .flatMap(t => minePatternFragment(t._1, t._2.toList, minSupport,slfimap,total))
        .collect()
        .toList ++ List(slfi(0))
    }
    else
      List.empty[FI]
  }

  def printTree(root:FPNode, seq:String): Unit ={
    if(root != null){
      println(seq+""+root.item+":"+root.support)
      root.children.foreach(child=>{
        printTree(child,seq+"-")
      })
    }
  }

  def minePatternFragment(prefix: String
                          , conditionalPatterns: List[Itemset]
                          , minSupport: Int
                          , slfimap: Map[String, Double]
                          , total: Int
                         ): List[FI] = {

    val preItem = new ListBuffer[String]()
    preItem.append(prefix)
    val preList = new ListBuffer[List[String]]()
    preList.append(preItem.toList)
    val Itemset1 = conditionalPatterns.flatten
      .groupBy(identity)
      .map(t => (t._1, t._2.size))
      .filter(_._2 >= minSupport)

    var res = List[FI]()

    if(Itemset1.keys.size > maxLen){
      res = conditionalPatterns
        .map(cp => pruneAndSort(cp, Itemset1.keys.toList))
        .flatMap(buildConditionalPatternsBase)
        .groupBy(_._1)
        .flatMap(cp=>minePatternFragment(cp._1,cp._2.map(cp2=>cp2._2),minSupport,slfimap,total))
        .toList
        .map(t=>{
          new FI(t.itemset:+prefix,t.support)
        }) ++ List(new FI(List(prefix),slfimap(prefix))) ++
        List(new FI(List(Itemset1.keys.toList(0))++List(prefix)
          ,Itemset1(Itemset1.keys.toList(0))*1.0/total))
    }
    else{
      val fpGrowth = new FPGrowth
      val singletons = mutable.LinkedHashMap(fpGrowth.findSingletons(conditionalPatterns, minSupport).map(i => i -> Option.empty[FPNode]): _*)
      val condFPTree = new FPTree(conditionalPatterns.map((_, 1)), minSupport, singletons)
      val prefixes = fpGrowth.generatePrefixes(List(prefix), singletons.keySet)
      res = bitOp(new BigInteger((pow(2,singletons.keys.toList.size)-1).toString,10),singletons.keys.toList,singletons,prefix,minSupport,total)
        .map(t=>{
          new FI(t.itemset:+prefix,t.support)
        }) ++ List(new FI(List(prefix),slfimap(prefix)))
    }
    res

  }

  def showCapital(x: Option[FPNode]) = x match {
    case Some(s) => s
    case None => null
  }

  def bitOp(bit:BigInteger
            , preList: List[String]
            , singletons: mutable.LinkedHashMap[String, Option[FPNode]]
            , pre:String
            , minSupport: Int
            , total: Int) = {

    val res = new ListBuffer[FI]()
    val a = bit
    var s = a.and(a.negate())

    while (s.equals(BigInteger.ZERO) == false) {
      var tempStr = s.toString(2)
      var itemset = new ListBuffer[String]()
      val diff = singletons.size - tempStr.length
      while(tempStr.indexOf("1") != -1){
        itemset.append(preList(diff+tempStr.indexOf('1')))
        tempStr = tempStr.replaceFirst("1","0")
      }

      if(itemset.length == 1){
        var root = showCapital(singletons(itemset(0)))
        var sup = 0
        while (root != null){
          sup += root.support
          root = root.itemLink
        }
        res.append(new FI(itemset.toList,sup*1.0/total))
      }
      else if (itemset.length > 1){
        itemset = itemset.reverse
        var root = showCapital(singletons(itemset(0)))
        var sup = 0

        while(root != null){
          var tree = root.parent
          itemset
            .tail
            .foreach(i=>{
              while(tree.item != null && !tree.item.equals(i)){
                tree = tree.parent
              }
            })
          if(tree.item != null && tree.item.equals(itemset.last)){
            sup += root.support
          }
          root = root.itemLink
        }
        if(sup >= minSupport){
          res.append(new FI(itemset.toList,sup*1.0/total))
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
