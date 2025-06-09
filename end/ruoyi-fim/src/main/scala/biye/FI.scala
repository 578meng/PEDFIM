package biye

import sequential.Apriori.Itemset

/**
 * @Author：ytl
 * @Package：biye
 * @Project：ruoyi
 * @name：FI
 * @Date：2024/3/13 13:37
 * @Filename：FI
 */
class FI(inItemset:Itemset,inSupport:Double) extends Serializable{
  var itemset:Itemset = inItemset
  var support:Double = inSupport
}
