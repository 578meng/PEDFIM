package biye

import sequential.Apriori.Itemset

/**
 * @Author：ytl
 * @Package：biye
 * @Project：ruoyi
 * @name：Rule
 * @Date：2024/3/13 13:38
 * @Filename：Rule
 */
class Rule(inpreitemset:String,inenditemset:String,insupport:Double,inconfidence:Double,inlift:Double) extends Serializable{
  var preitemset:String = inpreitemset
  var enditemset:String = inenditemset
  var support:Double = insupport
  var confidence:Double = inconfidence
  var lift:Double = inlift
}
