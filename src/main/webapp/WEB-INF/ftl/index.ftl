<html>
<head>
    <script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>
</head>

<script type="text/javascript">
   alert("test");

function clicktest(){

  $.get("/job/data/lagou/?ct=上海&kw=java&pn=1",function(data,status){
    alert("Data: " + data + "\nStatus: " + status);
  });
}

</script>

<body>
<button onclick = "clicktest()">test</button>


haha~

${number}
</body>


</html>