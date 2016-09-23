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

<div id="lbimgae2" style="width: 1000px;height:400px;"></div>
<div id="lbimgae" style="width: 1000px;height:400px;"></div>


<script type="text/javascript">

    var myChart = echarts.init(document.getElementById('lbimgae'));
    var option1 = {

        color: ['#3398DB'],
        title: {
            text: 'LoadBalance 验证'
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

    myChart.setOption(option1);

    var myChart2 = echarts.init(document.getElementById('lbimgae2'));

    var option2 = {
        title : {
            text: 'loadbalance',
            subtext: '预测',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: [
            <#list nodes as node>
                "${node.caption}",
            </#list>
            ]
        },
        series : [
            {
                name: '该node被选中的几率',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                <#list nodes as node>
                    {value:${node.possibility}, name:'${node.caption}'},
                </#list>

                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    myChart2.setOption(option2);



</script>





</body>

</html>