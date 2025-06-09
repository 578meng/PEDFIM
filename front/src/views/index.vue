<template>
  <div class="app-container home">

    <h1>频繁项集挖掘介绍</h1>
    <p>频繁项集挖掘是数据挖掘中的重要技术，用于发现数据集中经常出现在一起的物品组合。</p>
    <p>通过识别频繁项集，可以帮助我们了解数据集中的关联规则，从而支持市场篮子分析、推荐系统等应用。</p>
    <p>常见的频繁项集挖掘算法包括 Apriori 算法和 FP-growth 算法。</p>



    <h1>关联规则挖掘算法介绍</h1>
    <p>关联规则挖掘是一种数据挖掘技术，用于发现数据集中项之间的关联关系。它可以帮助我们找到频繁出现在一起的物品或事件，并根据它们之间的关联性进行分析和预测。</p>
    <p>关联规则是数据挖掘中用于描述不同属性之间关联关系的一种技术。它通常以“如果...那么...”的形式表示，即如果一个集合中的某些属性出现，那么另一些属性也很可能同时出现。关联规则由两部分组成：前件（antecedent）和后件（consequent）。前件是规则的条件部分，后件是规则的结论部分。<p>
    <p>举个例子，假设有一家零售商店的数据集，其中包含顾客购买的商品信息。通过分析这些数据，可以得出类似以下的关联规则：“如果顾客购买牛奶和面包，那么他们也很可能购买黄油。”在这个例子中，牛奶和面包构成了前件，黄油构成了后件。<p>
    <p>关联规则通常会计算支持度和置信度来衡量规则的重要性和可靠性。支持度衡量了规则在整个数据集中出现的频率，而置信度则衡量了规则的可靠程度。通过设定阈值来筛选出高支持度和高置信度的关联规则，可以帮助分析人员发现有意义的模式和关联关系。</p>

    <h1>常见的挖掘算法有哪些？</h1>
    <ul>
      <li>Apriori 算法：基于频繁项集的挖掘算法，通过逐层筛选频繁项集来生成关联规则。</li>
      <li>FP-growth 算法：利用前缀树结构来高效地挖掘频繁项集，避免了多次扫描数据集。</li>
      <li>Eclat 算法：基于垂直数据格式的挖掘算法，适用于稠密数据集的关联规则挖掘。</li>
    </ul>

    <h1>如何挖掘算法？</h1>
    <p>在实际应用中，可以通过将关联规则挖掘算法应用于购物篮分析、市场篮分析、交叉销售分析等场景，从而发现潜在的关联关系并进行业务决策。</p>

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.8.7"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

