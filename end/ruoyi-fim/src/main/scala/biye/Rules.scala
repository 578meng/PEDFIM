package biye

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ListBuffer


object Rules {

  val maxLen = 10

  def findRules(fitemset:List[FI],confidence:Double,spark: SparkSession, sc: SparkContext): List[Rule] = {

    if(fitemset != null){
      val fimap = sc.parallelize(fitemset)
        .map(t => (t.itemset.sorted.toString.replace("List(","").replace(")",""),t.support))
        .collect()
        .toMap
      sc.broadcast(fimap)
      sc.parallelize(fitemset)
        .mapPartitions(_.map(t=>{
          val res = new ListBuffer[Rule]()
          if(t.itemset.length > 1){
            var i = 1
            while(i < t.itemset.length){
              val p = t.itemset.slice(0,i).sorted.toString().replace("List(","").replace(")","")
              val e = t.itemset.slice(i,t.itemset.length).sorted.toString().replace("List(","").replace(")","")
              var conf = t.support/fimap(p)
              if(conf >= confidence){
                res.append(new Rule(p,e,t.support,conf, conf/fimap(p)))
              }
              conf = t.support/fimap(e)
              if(conf >= confidence){
                res.append(new Rule(e,p,t.support,conf,conf/fimap(p)))
              }
              i = i + 1
            }
          }
          res.toList
        }))
//        .map(t=>{
//          val res = new ListBuffer[Rule]()
//          if(t.itemset.length > 1){
//            var i = 1
//            while(i < t.itemset.length){
//              val p = t.itemset.slice(0,i).sorted.toString().replace("List(","").replace(")","")
//              val e = t.itemset.slice(i,t.itemset.length).sorted.toString().replace("List(","").replace(")","")
//              var conf = t.support/fimap(p)
//              if(conf >= confidence){
//                res.append(new Rule(p,e,t.support,conf, conf/fimap(p)))
//              }
//              conf = t.support/fimap(e)
//              if(conf >= confidence){
//                res.append(new Rule(e,p,t.support,conf,conf/fimap(p)))
//              }
//              i = i + 1
//            }
//          }
//          res.toList
//        })
        .flatMap(identity)
        .collect()
        .toList
    }
    else List.empty[Rule]

  }


}
