# 数据导入
import pandas as pd
import numpy as np
def dataInput(filename):
    data=pd.read_excel(filename)
    data=np.array(data)
    data=data.tolist()
    return data

# 关联规则挖掘函数申明
import typing
from efficient_apriori.itemsets import itemsets_from_transactions, ItemsetCount
from efficient_apriori.rules import generate_rules_apriori
def apriori(
        transactions: typing.Union[typing.List[tuple], typing.Callable],
        min_support: float = 0.5,
        min_confidence: float = 0.5,
        max_length: int = 8,
        verbosity: int = 0,
        output_transaction_ids: bool = False,
):
        itemsets, num_trans = itemsets_from_transactions(
                transactions,
                min_support,
                max_length,
                verbosity,
                output_transaction_ids,
        )

        if itemsets and isinstance(next(iter(itemsets[1].values())), ItemsetCount):
                itemsets_for_rules = _convert_to_counts(itemsets)
        else:
                itemsets_for_rules = itemsets

        rules = generate_rules_apriori(
                itemsets_for_rules, min_confidence, num_trans, verbosity
        )
        return itemsets, list(rules)

def _convert_to_counts(itemsets):
        itemsets_counts = {}
        for size, sets in itemsets.items():
                itemsets_counts[size] = {i: c.itemset_count for i, c in sets.items()}
        return itemsets_counts

def fim(data,sup,con,ftype):
    itemsets, rules = apriori(data, min_support=sup, min_confidence=con)
    row = []
    if(ftype == 'rules'):
        for r in rules:
            item = str(r.__str__).replace('<bound method Rule.__str__ of ','').replace('>','').replace('} - {','} -> {')
            confidence = format(r.confidence, '.6f')
            lift = format(r.lift, '.6f')
            support = format(r.support, '.6f')
            row.append([item,support,confidence,lift])
    else :
        for r in rules:
            item = str(r.__str__).replace('<bound method Rule.__str__ of ','').replace('>','')
            support = format(r.support, '.6f')
            row.append([item,support])
    return row
    
import sys
from datetime import datetime
if __name__ == "__main__":
    args = sys.argv[1:]
    taskName = args[0]
    fileName = args[1]
    ftype = args[2]
    sup = float(args[3])
    con = float(args[4])
    taskId= args[5]
    path = "C:/Users/wxhllj/Desktop/SPEFIMSYSTEM/upload"
    data = dataInput(fileName.replace('/profile',path))
    row = fim(data,sup,con,ftype)

    ctime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    current_time = ctime.replace("-","_")
    current_time = current_time.replace(" ","_")
    current_time = current_time.replace(":","_")
    newFilePath = path + "/result/"
    newFileName = taskName + "_" + current_time
    if(ftype == 'rules'):
        newFileName += "_关联规则.xlsx"
        pd.DataFrame(data=row, columns=['项集','支持度','置信度','提升度']).to_excel(newFilePath + newFileName,index=False)
    else:
        newFileName += "_频繁项集.xlsx"
        pd.DataFrame(data=row, columns=['项集','支持度']).to_excel(newFilePath + newFileName,index=False)

    print(taskId)
    print("success")
    print(newFileName)
    print(newFilePath.replace(path,"/profile")+newFileName)
    print(ctime)


        