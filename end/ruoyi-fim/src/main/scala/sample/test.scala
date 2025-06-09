package sample

import run.SparkRun


object test{

  def main(args: Array[String]): Unit = {
//    val res = List("","/profile/upload/2024/02/25/mushroom_20240225133822A001.xlsx","xlsx","rules","0.35","0.5")
//    val res = List("","/profile/upload/2024/03/12/mushroom_20240312164842A001.txt","txt","rules","0.35","0.5")
    val res = List("rules","/profile/upload/2024/03/12/mushroom_20240312164842A001.csv","csv","fim","0.05","0.5")
    new SparkRun().run(res.toArray)
  }

}
