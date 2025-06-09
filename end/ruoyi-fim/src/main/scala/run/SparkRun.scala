package run

import biye.{FI, Rule}
import biye.SPEFIM.{getClass, spefimfindFrequentItemsets}
import biye.PEFIM.{getClass, pefimfindFrequentItemsets}
import biye.Rules.{findRules, getClass}
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import sequential.Apriori.Itemset
import sequential.Util
import sequential.Util.absoluteSupport

import java.io.{File, FileInputStream, FileOutputStream}
import scala.collection.mutable.ListBuffer

/**
 * @Author：ytl
 * @Package：run
 * @Project：ruoyi
 * @name：SparkRun
 * @Date：2024/3/12 17:19
 * @Filename：SparkRun
 */
class SparkRun {

  val path_temp = "E:/desktop/SPEFIMSYSTEM/upload"

  def run(args: Array[String]): String ={
    /**
     * 创建spark
     */
    var spark: SparkSession = null
    val appName = Util.appName
    spark
      = SparkSession.builder()
      .appName(appName)
      .master("local[8]")
      .getOrCreate()

    println(spark)
    val sc
    = spark
      .sparkContext
    sc.setLogLevel("WARN")
    println(sc)
    /**
     * 文件读取
     */
    var transactionsRDD: RDD[Itemset] = null

    val path = args(1).replace("/profile",path_temp)


    var file = ""
    var fileRDD: RDD[String] = null
    if(args(2) == "xlsx"){
      transactionsRDD = sc.parallelize(readExcel(path))
        .cache()
        .repartition(10)
    }
    else if(args(2) == "txt" ||args(2) == "csv"){
      file = List.fill(Util.replicateNTimes)(path).mkString(" ")
      fileRDD = sc.textFile(file)
      transactionsRDD
        = fileRDD
        .filter(!_.trim.isEmpty)
        .map(_.split(" " + "+"))
        .map(l => l.map(_.trim).toList)
        .cache()
        .repartition(10)
    }



    val support
    = absoluteSupport(args(4).toDouble, transactionsRDD.count().toInt)



    val sortedSingletons
    = transactionsRDD
      .flatMap(identity)
      .map(item => (item, 1))
      .reduceByKey(_ + _)
      .filter(_._2 >= support)

    val start =System.currentTimeMillis()
    var res = List[FI]()
    if(args(7).equals("spefim")){
      res = spefimfindFrequentItemsets(transactionsRDD,sortedSingletons,support,transactionsRDD.count().toInt,spark,sc)
    }
    else{
      println("pefim:"+support)
      println("total:"+transactionsRDD.count().toInt)
      res = pefimfindFrequentItemsets(transactionsRDD.collect(),sortedSingletons.collect(),support,transactionsRDD.count().toInt)
    }
    println("fim-number:"+res.length)
    var frules = List.empty[Rule]
    if(args(3) == "rules"){
      frules = findRules(res,args(5).toDouble,spark,sc)
    }
    println("rules-number:"+frules.length)
    val end =System.currentTimeMillis()

//    res.foreach(t=>{println(t.itemset+":"+t.support)})

    println("rules-number:"+frules.length)
    println("time:"+(end-start)*1.0/1000)
    var result = ""
    if(args(3) == "fim"){
      result = fimtoexcel(res,end+"_"+args(0))
    }
    else{
      result = rulestoexcel(frules,end+"_"+args(0))
    }

    sc.stop()
    result+",-,"+"/profile/result/"+result+",-,"+end
  }

  def readExcel(path:String): List[List[String]] ={
    val workbook = new XSSFWorkbook(new FileInputStream(new File(path)))

    val sheet = workbook.getSheetAt(0) // 获取第一个工作表
    val rows = sheet.getPhysicalNumberOfRows() // 获取行数
    val res = ListBuffer[List[String]]()
    val dataFormatter = new DataFormatter()
    for (i <- 0 until rows) {
      val row = sheet.getRow(i)
      val temp = ListBuffer[String]()
      for (j <- 0 until row.getLastCellNum()) {
        val cell = row.getCell(j)
        val cellValue = dataFormatter.formatCellValue(cell)
        temp.append(cellValue)
      }
      res.append(temp.toList)
    }
    res.toList
  }

  def rulestoexcelbig(res:List[Rule],name:String): String ={
    val newname = name+"_关联规则.xlsx"
    val filepath = path_temp+"/result/"+newname
    val fileOut = new FileOutputStream(new File(filepath))
    var workbook = new SXSSFWorkbook()

    var sheet = workbook.createSheet("关联规则")
    var excelRow = sheet.createRow(0)
    var cell = excelRow.createCell(0)
    cell.setCellValue("前项")
    cell = excelRow.createCell(1)
    cell.setCellValue("后项")
    cell = excelRow.createCell(2)
    cell.setCellValue("支持度（前项+后项）")
    cell = excelRow.createCell(3)
    cell.setCellValue("置信度（前项->后项）")
    cell = excelRow.createCell(4)
    cell.setCellValue("提升度（前项->后项）")
    var rowNum = 1
    for (row <- res) {
      excelRow = sheet.createRow(rowNum)
      cell = excelRow.createCell(0)
      cell.setCellValue(row.preitemset)
      cell = excelRow.createCell(1)
      cell.setCellValue(row.enditemset)
      cell = excelRow.createCell(2)
      cell.setCellValue(row.support.toString)
      cell = excelRow.createCell(3)
      cell.setCellValue(row.confidence.toString)
      cell = excelRow.createCell(4)
      cell.setCellValue(row.lift.toString)
      rowNum += 1
    }
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    newname
  }

  def rulestoexcel(res:List[Rule],name:String): String ={
    val newname = name+"_关联规则.xlsx"
    var workbook = new SXSSFWorkbook()
    var sheet = workbook.createSheet("关联规则")
    var excelRow = sheet.createRow(0)
    var cell = excelRow.createCell(0)
    cell.setCellValue("前项")
    cell = excelRow.createCell(1)
    cell.setCellValue("后项")
    cell = excelRow.createCell(2)
    cell.setCellValue("支持度（前项+后项）")
    cell = excelRow.createCell(3)
    cell.setCellValue("置信度（前项->后项）")
    cell = excelRow.createCell(4)
    cell.setCellValue("提升度（前项->后项）")
    var rowNum = 1
    for (row <- res) {
      excelRow = sheet.createRow(rowNum)
      cell = excelRow.createCell(0)
      cell.setCellValue(row.preitemset)
      cell = excelRow.createCell(1)
      cell.setCellValue(row.enditemset)
      cell = excelRow.createCell(2)
      cell.setCellValue(row.support.toString)
      cell = excelRow.createCell(3)
      cell.setCellValue(row.confidence.toString)
      cell = excelRow.createCell(4)
      cell.setCellValue(row.lift.toString)
      rowNum += 1
    }

    val fileOut = new FileOutputStream(path_temp+"/result/"+newname)
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    newname
  }

  def fimtoexcel(res:List[FI],name:String): String ={
    val workbook = new SXSSFWorkbook()
    val sheet = workbook.createSheet("频繁项集")
    var excelRow = sheet.createRow(0)
    var cell = excelRow.createCell(0)
    cell.setCellValue("频繁项集")
    cell = excelRow.createCell(1)
    cell.setCellValue("支持度")
    var rowNum = 1
    for (row <- res) {
      excelRow = sheet.createRow(rowNum)
      cell = excelRow.createCell(0)
      cell.setCellValue(row.itemset.toString.replace("List(","").replace(")",""))
      cell = excelRow.createCell(1)
      cell.setCellValue(row.support.toString)
      rowNum += 1
    }
    val newname = name+"_频繁项集.xlsx"
    val fileOut = new FileOutputStream(path_temp+"/result/"+newname)
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    newname
  }
}
