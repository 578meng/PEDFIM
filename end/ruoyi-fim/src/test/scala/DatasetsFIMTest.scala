import FIMTest.assertItemsetsMatch
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import sequential.Apriori.Itemset
import sequential._
import sequential.fpgrowth.FPGrowth
import sequential.hashtree.AprioriHashTree
import spark.{SPEFIM}
import standAlone.{PEFIM}
import experiment.{HBPFP_DC, EAFIM, Fmafibs}


import scala.collection.mutable

class DatasetsFIMTest extends FunSuite with BeforeAndAfterAll {

  private val fimInstances = List(
    /**
     * 单机实验
     */
      (1, () => new FPGrowth)
    , (0, () => new AprioriHashTree)
    , (1, () => new PEFIM)
    /**
     * 分布式实验
     */
    , (0, () => new Fmafibs)
    , (1, () => new SPEFIM)
    , (1, () => new EAFIM)
    , (1, () => new HBPFP_DC)
  )

  private val datasets = List(
    /**
     * 稀疏
     */
     (1, "chess.txt", 0.8)
    ,(1, "mushroom.txt", 0.35)
    ,(1, "retail.txt", 0.0035)
    ,(1, "T10I4D100K.txt", 0.008)

    /**
     * 分布式
     */
    ,(0, "chess.txt", 0.4)
    ,(0, "mushroom.txt", 0.05)
    ,(0, "pamap.txt", 0.1)
    ,(0, "retail.txt", 0.003)
    ,(0, "T10I4D100K.txt", 0.0003)
    ,(0, "kosarak.txt", 0.0065)
    ,(0, "chainstore.txt", 0.00016)

    /**
     * demo
     */
    ,(0, "shop.txt", 0.2)
    ,(0, "test.txt", 0.2)
  )

  private val runNTimes = 1
  Util.replicateNTimes = 1

  private val executionTimes: mutable.Map[(String, String), List[Long]] = mutable.LinkedHashMap()
  private val resultsCache: mutable.Map[String, List[Itemset]] = mutable.Map()

  datasets.filter(_._1 == 1).foreach(t => {
    for (run <- 1 to runNTimes) {
      fimInstances.filter(_._1 == 1).map(_._2.apply()).foreach(fim => {

        val className = fim.getClass.getSimpleName
        Util.appName = s"$className - ${t._2} - x${Util.replicateNTimes} - $run"
        test(Util.appName) {
          val path = getClass.getResource("/datasets/" + t._2).getPath
          val frequentSets = fim.execute(path, " ", t._3)
//          Util.printItemsets(frequentSets)

          if (!resultsCache.contains(t._2 + t._3))
            resultsCache.update(t._2 + t._3, frequentSets)
          val expectedItemsets = resultsCache(t._2 + t._3)

          val key = (className, s"${t._2}")
          val executions = executionTimes.getOrElse(key, List.empty[Long])
          executionTimes.update(key, executions :+ fim.executionTime)

          assertItemsetsMatch(expectedItemsets, frequentSets, className)
        }
      })
    }
  })

  override def afterAll() {
    val header = Seq("Class ", "Dataset ") ++ (1 to runNTimes).map(i => s" Run $i ") :+ "Mean "
    var prevDataset = ""
    val rows = executionTimes.flatMap(t => {
      var mean = 0d
      if (runNTimes >= 3)
        mean = (t._2.sum - t._2.max) / (runNTimes - 1)
      else
        mean = t._2.sum / runNTimes

      val r = List(Seq(s" ${t._1._1} ", s" ${t._1._2} ") ++ t._2.map(formatExecution(_)) :+ formatExecution(mean))
      if (prevDataset != t._1._2 && !prevDataset.isEmpty) {
        prevDataset = t._1._2
        1.to(runNTimes + 3, 1).map(_ => "") +: r
      }
      else {
        prevDataset = t._1._2
        r
      }
    }).toSeq
    println(s"\nExecution times replicating ${Util.replicateNTimes} time(s)\n" + Util.Tabulator.format(header +: rows))
  }

  def formatExecution(value: Double): String = {
    f" ${value / 1000d}%1.2f "
  }

}
