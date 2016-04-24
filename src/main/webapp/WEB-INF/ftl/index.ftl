<html>
<head>
    <script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>
</head>

<script type="text/javascript">
   // alert("test");
var currentPage = 0;

function prePage(){
	if(currentPage > 1) {
		currentPage-- ;
		$.get("/job/data/lagou/?ct=上海&kw=java&pn=" + currentPage, function(data,status) {
    		var object_list = data.data.object_list
			$("#lagoudiv").empty();
    		$.each(object_list, function(i, item) {
    			$("#lagoudiv").append("<li>positionId:" + item.positionId + "</li>");
    		});
  		});
	} else {
		alert("已经是第一页");
	}
  	
}

function nextPage() {
	currentPage++ ;
	$.get("/job/data/lagou/?ct=上海&kw=java&pn=" + currentPage, function(data, status){
		var object_list = data.data.object_list
		$("#lagoudiv").empty();
    	$.each(object_list, function(i, item) {
    		$("#lagoudiv").append("<li>positionId:" + item.positionId + "</li>");
    	});

  	});
}

</script>

<body>
<button onclick = "prePage()">上一页</button>

<button onclick = "nextPage()">下一页</button>


<br>${number}<br>

<div id="lagoudiv">

</div>


</body>

</html>