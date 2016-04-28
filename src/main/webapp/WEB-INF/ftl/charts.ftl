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


<br>
<!-- 为 ECharts 准备一个具备大小（宽高）的Dom -->
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



<div>

    <form action="/charts/" method="get">
        <div>
            <div class="col-xs-3">
                <label for="sel1">工作城市:</label>
                <select class="form-control" id="sel1" name="city">
                    <option value="上海">上海</option>
                    <option value="杭州">杭州</option>
                    <option value="北京">北京</option>
                    <option value="深圳">深圳</option>
                </select>
            </div>

            <div class="col-xs-3">
                <label for="sel2">岗位关键词:</label>
                <select class="form-control" id="sel2" name="keyword">
                    <option value="Java">Java</option>
                    <option value="Python">Python</option>
                    <option value="Php">Php</option>
                    <option value="测试">测试</option>
                </select>
            </div>
            <div class="col-xs-3">
                <label for="sel2"> 查看报表</label><br>
                <button type="submit" class="btn btn-success">确认</button>
            </div>
        </div>
    </form>

</div>

</body>

</html>