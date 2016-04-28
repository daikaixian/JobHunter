<!DOCTYPE html>
<html>
<header>
    <meta charset="utf-8">
    <#--<script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>-->
    <link rel="stylesheet" href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>


    <script type="text/javascript">
        // alert("test");
        var lagouCurrentPage = 0;
        var neituiCurrentPage = 0;

        function lagouPrePage(){
            var city = $("#sel1").val();
            var keyword = $("#sel2").val();
            var workYear = $("#sel3 option:selected").text();

            if (lagouCurrentPage > 1) {
                lagouCurrentPage-- ;
                $.get("/job/data/lagou/?ct=" + city
                      + "&kw=" + keyword +"&wy=" + workYear + "&pn=" + lagouCurrentPage,
                      function(data, status) {
                          var object_list = data.data.object_list
                          $("#lagoudiv").empty();
                          $.each(object_list, function(i, item) {
                              $("#lagoudiv").append(
                                      '<li><a href= "' + item.detailPage
                                      +'" target="_blank">' + item.companyName + ':'
                                      + item.positionName + ':' + item.salary +'</a> </li>');
                          });
                      });
            } else {
                alert("已经是第一页");
            }



        }

        function neituiPrePage() {
            var city = $("#sel1").val();
            var keyword = $("#sel2").val();
            var workYear = $("#sel3").val();

            if (neituiCurrentPage > 1) {
                neituiCurrentPage-- ;
                $.get("/job/data/neitui/?ct=" + city
                      + "&kw=" + keyword +"&wy=" + workYear + "&pn=" + neituiCurrentPage,
                      function(data, status) {
                          var object_list = data.data.object_list
                          $("#neituidiv").empty();
                          $.each(object_list, function(i, item) {
                              $("#neituidiv").append(
                                      '<li><a href= "' + item.detailPage
                                      +'" target="_blank">' + item.companyName + ':'
                                      + item.positionName + ':' + item.salary +'</a> </li>');
                          });
                      });
            } else {
                alert("已经是第一页");
            }



        }

        function lagouNextPage() {

            var city = $("#sel1").val();
            var keyword = $("#sel2").val();
            var workYear = $("#sel3 option:selected").text();

            lagouCurrentPage++ ;
            $.get("/job/data/lagou/?ct=" + city
                  + "&kw=" + keyword +"&wy=" + workYear + "&pn=" + lagouCurrentPage,
                  function(data, status) {
                      var object_list = data.data.object_list;
                      if (object_list.length == 0){
                          alert("下一页没有数据了");
                          return;
                      }
                      $("#lagoudiv").empty();
                      $.each(object_list, function(i, item) {
                          $("#lagoudiv").append(
                                  '<li><a href= "' + item.detailPage
                                  +'" target="_blank">' + item.companyName + ':'
                                  + item.positionName + ':' + item.salary +'</a> </li>');
                      });
                  });
        }

        function neituiNextPage() {
            var city = $("#sel1").val();
            var keyword = $("#sel2").val();
            var workYear = $("#sel3").val();

            neituiCurrentPage++;
            $.get("/job/data/neitui/?ct=" + city
                  + "&kw=" + keyword +"&wy=" + workYear + "&pn=" + neituiCurrentPage,
                  function(data, status) {
                      var object_list = data.data.object_list;
                      if (object_list.length == 0){
                          alert("下一页没有数据了");
                          return;
                      }

                      $("#neituidiv").empty();
                      $.each(object_list, function(i, item) {
                          $("#neituidiv").append(
                                  '<li><a href= "' + item.detailPage
                                  +'" target="_blank">' + item.companyName + ':'
                                  + item.positionName + ':' + item.salary +'</a> </li>');
                      });
                  });

        }


        function selectChange() {
            lagouCurrentPage = 0;
            neituiCurrentPage = 0;
        }
    </script>
</header>



<body>

<div>
<div class="col-xs-3">
    <label for="sel1">工作城市:</label>
    <select class="form-control" id="sel1" onchange="selectChange()">
        <option value="上海">上海</option>
        <option value="杭州">杭州</option>
        <option value="北京">北京</option>
        <option value="深圳">深圳</option>
    </select>
</div>

<div class="col-xs-3">
    <label for="sel2">岗位关键词:</label>
    <select class="form-control" id="sel2" onchange="selectChange()">>
        <option value="Java">Java</option>
        <option value="Python">Python</option>
        <option value="Php">Php</option>
        <option value="测试">测试</option>
    </select>
</div>

<div class="col-xs-3">
    <label for="sel3">工作经验:</label>
    <select class="form-control" id="sel3" onchange="selectChange()">>
        <option value="1">应届毕业生</option>
        <option value="2">1-3年</option>
        <option value="3">3-5年</option>
        <option value="4">5-10年</option>
    </select>
</div>


</div>

<br>
<div>
    <br>
</div>
<h3>拉勾网职位</h3>
<button type="button" class="btn btn-success" onclick="lagouPrePage()">上一页</button>

<button type="button" class="btn btn-success" onclick="lagouNextPage()">下一页</button>

<div id="lagoudiv">
</div>

<p>-------------------------------------------------------------------------------------------</p>

<h3>内推网职位</h3>
<button type="button" class="btn btn-success" onclick="neituiPrePage()">上一页</button>

<button type="button" class="btn btn-success" onclick="neituiNextPage()">下一页</button>

<div id="neituidiv">

</div>

<p>-------------------------------------------------------------------------------------------</p>

<a href="/charts/">查看报表</a>

</body>

</html>