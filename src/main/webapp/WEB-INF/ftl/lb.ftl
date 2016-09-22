<!DOCTYPE html>
<html>
<header>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
    <script src="/js/echarts.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>


</header>

<body>


<div id="lbimgae" style="width: 1000px;height:400px;"></div>

<script type="text/javascript">

    var myChart3 = echarts.init(document.getElementById('lbimgae'));
    var option3 = {

        title: {
            text: 'LoadBalance 测试'
        },
        tooltip: {},
        legend: {
            data:['node获得调用次数']
        },
        xAxis: {
            data: [
          <#list nodes as node>
           "${node.caption}",
          </#list>

            ]
        },
        yAxis: {},
        series: [{
            name: '次数',
            type: 'bar',
            data: [
            <#list nodes as node>
                ${node.count},
            </#list>

            ]
        }]
    };

    myChart3.setOption(option3);


</script>





</body>

</html>