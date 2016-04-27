<!DOCTYPE html>
<html>
<header>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
    <script src="/js/echarts.min.js" type="text/javascript"></script>


</header>

<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的Dom -->

<h1>charts</h1>

<div id="zhuzhuangtu" style="width: 600px;height:400px;"></div>

<div id="binzhunagtu" style="width: 600px;height:400px;"></div>

<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('zhuzhuangtu'));
    var myChart2 = echarts.init(document.getElementById('binzhunagtu'));

    // 指定图表的配置项和数据
    var option1 = {
        title: {
            text: '${result1.city}, ${result1.keyword},平均工资,单位:k'
        },
        tooltip: {},
        legend: {
            data:['平均工资,单位:k']
        },
        xAxis: {
            data: ["${result1.workYear}","${result2.workYear}","${result3.workYear}",
                   "${result4.workYear}","${result5.workYear}"]
        },
        yAxis: {},
        series: [{
            name: '平均工资',
            type: 'bar',
            data: [${result1.averageSalary}, ${result2.averageSalary}, ${result3.averageSalary},
            ${result4.averageSalary}, ${result5.averageSalary}]
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option1);

    var option2 = {
        title : {
            text: '参与计算平均工资的岗位对工作经验要求饼状图',
            subtext: '以${result1.city}市 ${result1.keyword}为检索条件',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ["${result1.workYear}","${result2.workYear}","${result3.workYear}",
                   "${result4.workYear}","${result5.workYear}"]
        },
        series : [
            {
                name: '参与计算的岗位数量',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:${result1.totalCount}, name:'${result1.workYear}'},
                    {value:${result2.totalCount}, name:'${result2.workYear}'},
                    {value:${result3.totalCount}, name:'${result3.workYear}'},
                    {value:${result4.totalCount}, name:'${result4.workYear}'},
                    {value:${result5.totalCount}, name:'${result5.workYear}'}
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