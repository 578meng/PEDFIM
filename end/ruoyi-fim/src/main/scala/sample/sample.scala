package sample

import base.DFPSEnd6.{findFrequentItemsets, getClass}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import sequential.Apriori.Itemset
import sequential.Util
import sequential.Util.absoluteSupport

/**
 * @Author：ytl
 * @Package：sample
 * @Project：SPEFIM
 * @name：sample
 * @Date：2024/2/23 23:24
 * @Filename：sample
 */
object sample {
  def main(args: Array[String]): Unit = {

    val file = List(
      (0, "retail.txt", 0.0001)
      , (0, "chess.txt", 0.6)

      // 单机
      , (0, "chess.txt", 0.7)
      , (0, "chess.txt", 0.65)
      , (0, "chess.txt", 0.6)
      , (0, "chess.txt", 0.55)
      , (0, "chess.txt", 0.5)

      , (0, "T10I4D100K.txt", 0.008)
      , (0, "T10I4D100K.txt", 0.0075)
      , (0, "T10I4D100K.txt", 0.007)
      , (0, "T10I4D100K.txt", 0.0065)
      , (0, "T10I4D100K.txt", 0.006)

      , (0, "T10I4D100K.txt", 0.0001)

      , (0, "mushroom.txt", 0.60)
      , (0, "mushroom.txt", 0.55)
      , (0, "mushroom.txt", 0.50)
      , (0, "mushroom.txt", 0.45)
      , (0, "mushroom.txt", 0.40)
      , (0, "mushroom.txt", 0.35)
      , (0, "mushroom.txt", 0.3)
      , (0, "mushroom.txt", 0.25)
      , (0, "mushroom.txt", 0.2)
      , (0, "mushroom.txt", 0.15)

      , (0, "retail.txt", 0.0035)
      , (0, "retail.txt", 0.003)
      , (0, "retail.txt", 0.0025)
      , (0, "retail.txt", 0.002)
      , (0, "retail.txt", 0.0015)
      , (0, "retail.txt", 0.001)


      // 分布式
      , (0, "mushroom.txt", 0.35)
      , (0, "mushroom.txt", 0.3)
      , (0, "mushroom.txt", 0.25)
      , (0, "mushroom.txt", 0.2)
      , (0, "mushroom.txt", 0.15)

      , (0, "mushroom.txt", 0.05)
      , (0, "mushroom.txt", 0.045)
      , (0, "mushroom.txt", 0.04)
      , (0, "mushroom.txt", 0.035)
      , (0, "mushroom.txt", 0.03)
      , (0, "mushroom.txt", 0.025)
      , (0, "mushroom.txt", 0.02)
      , (0, "mushroom.txt", 0.015)
      , (0, "mushroom.txt", 0.01)





      , (0, "chess.txt", 0.85)
      , (0, "chess.txt", 0.8)
      , (0, "chess.txt", 0.75)
      , (0, "chess.txt", 0.7)
      , (0, "chess.txt", 0.65)
      , (0, "chess.txt", 0.6)

      , (0, "chess.txt", 0.36)
      , (0, "chess.txt", 0.37)
      , (0, "chess.txt", 0.38)
      , (0, "chess.txt", 0.39)
      , (0, "chess.txt", 0.40)
      , (0, "chess.txt", 0.6)

      , (0, "T10I4D100K.txt", 0.00035)
      , (0, "T10I4D100K.txt", 0.0003)
      , (0, "T10I4D100K.txt", 0.00025)
      , (0, "T10I4D100K.txt", 0.0002)
      , (0, "T10I4D100K.txt", 0.00015)
      , (0, "T10I4D100K.txt", 0.0015)

      , (0, "kosarak.txt", 0.0065)
      , (0, "kosarak.txt", 0.006)
      , (0, "kosarak.txt", 0.0055)
      , (0, "kosarak.txt", 0.005)
      , (0, "kosarak.txt", 0.0045)
      , (0, "kosarak.txt", 0.004)
      , (0, "kosarak.txt", 0.0035)
      , (0, "kosarak.txt", 0.003)
      , (0, "kosarak.txt", 0.0025)
      , (0, "kosarak.txt", 0.002)
      , (0, "kosarak.txt", 0.0015)
      , (0, "kosarak.txt", 0.001)

      , (0, "Chainstore.txt", 0.0016)
      , (0, "Chainstore.txt", 0.0014)
      , (0, "Chainstore.txt", 0.0012)
      , (0, "Chainstore.txt", 0.0010)
      , (0, "Chainstore.txt", 0.0008)
      , (0, "Chainstore.txt", 0.0006)
      , (0, "Chainstore.txt", 0.016)
      , (0, "Chainstore.txt", 0.014)
      , (0, "Chainstore.txt", 0.012)
      , (0, "Chainstore.txt", 0.010)
      , (1, "Chainstore.txt", 0.008)
      , (0, "Chainstore.txt", 0.06)

      , (0, "retail.txt", 0.0003)
      , (0, "retail.txt", 0.00025)
      , (0, "retail.txt", 0.0002)
      , (0, "retail.txt", 0.00015)
      , (0, "retail.txt", 0.0001)





      , (0, "mushroom.txt", 0.1)
      , (0, "pamp.txt", 0.469)
      , (0, "kosarak.txt", 0.003)
      , (0, "Chainstore.txt", 0.0006)
    )

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

    //  println("\t|\tfile\t|\tminsup\t|\tnumber\t|\ttime\t|\t")
    printf("\t|\t %-20s \t|\t %-15s \t|\t %-15s \t|\t %-15s \t|\t\n","file","minsup","number","time")

    file.foreach(f=>{

      if(f._1 == 1){
        val start =System.currentTimeMillis()
        var transactionsRDD: RDD[Itemset] = null
        val path = getClass.getResource("/datasets/"+f._2).getPath
        val file = List.fill(Util.replicateNTimes)(path).mkString(" ")
        var fileRDD: RDD[String]  = sc.textFile(file)
        transactionsRDD = fileRDD
          .filter(!_.trim.isEmpty)
          .map(_.split(" " + "+"))
          .map(l => l.map(_.trim).toList)
          .cache()
          .repartition(8)
        val support = absoluteSupport(f._3, transactionsRDD.count().toInt)

        val sortedSingletons = transactionsRDD
          .flatMap(identity)
          .map(item => (item, 1))
          .reduceByKey(_ + _)
          .filter(_._2 >= support)

        var res = List[Itemset]()
        try {
          res = findFrequentItemsets(transactionsRDD,sortedSingletons,support,spark,sc)
        }
        catch {
          case ex:Exception => ex.printStackTrace()
        }
        val end =System.currentTimeMillis()

        sc.parallelize(res).saveAsTextFile("./out/DFPSEnd6/"+f._2.replace(".txt","")+"_"+String.valueOf(f._3)+".txt")

        //      print("\t|\tfile\t|\tminsup\t|\tnumber\t|\ttime\t|\t")
        //            println("\t|\t"+f._2+"\t|\t"+f._3+"\t|\t"+res.length+"\t|\t"+((end-start)*1.0/1000)+"\t|\t")
        printf("\t|\t %-20s \t|\t %-15s \t|\t %-15s \t|\t %-15s \t|\t\n",f._2,String.valueOf(f._3),res.length,((end-start)*1.0/1000))

        //      res.foreach(println)
        //      print("file:"+f._2)
        //      println("\tminsup:"+f._3)
        //      println("number:"+res.length)
        //      println("time:"+(end-start)*1.0/1000)
      }
    })
  }
}
