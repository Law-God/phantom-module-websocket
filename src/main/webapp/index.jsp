<!DOCTYPE html>
    <head>
        <meta charset="utf-8"/>
        <title>websocket测试</title>
    </head>
    <body>
        <h2>Hello World!</h2>
        <script>
            var WS = new WebSocket("ws://localhost:8080/websocket");

            WS.onopen = function(){
                console.log("open");
                WS.send("hello");
            }

            WS.onmessage = function(evt){
                console.log(evt.data);
            }

            WS.onclose = function(evt){
                console.log("close");
            }

            WS.onerror = function(evt){
                console.log("error");
            }
        </script>
    </body>
</html>


<%--
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/socket.js"></script>
    <title>无标题文档</title>
</head>
<script language="javascript">

</script>
<body>
<table>
    <tr>
        <td>Message</td>
        <td><input type="text" id="message"></td>
    </tr>
    <tr>
        <td>Name</td>
        <td><input type="text" id="othername"></td>
    </tr>
    <tr>
        <td><input id="sendbutton" type="button" value="send" onClick="click"  disabled="true">
            </input></td>
    </tr>
</table>
<script>
    var username = window.prompt("输入你的名字:");

    document.write("Welcome<p id=\"username\">"+username+"</p>");

    if (!window.WebSocket && window.MozWebSocket)
        window.WebSocket=window.MozWebSocket;
    if (!window.WebSocket)
        alert("No Support ");
    var ws;

    $(document).ready(function(){

        $("#sendbutton").attr("disabled", false);
        $("#sendbutton").click(sendMessage);

        startWebSocket();
    })

    function sendMessage()
    {
        var othername=$("#othername").val();
        var msg="MSG\t"+username+"_"+othername+"_"+$("#message").val();
        send(msg);
    }
    function send(data)
    {
        console.log("Send:"+data);
        ws.send(data);
    }
    function startWebSocket()
    {
        ws = new WebSocket("ws://localhost:8080/websocket");
        ws.onopen = function(){
            console.log("success open");
            $("#sendbutton").attr("disabled", false);
        };
        ws.onmessage = function(event)
        {
            console.log("RECEIVE:"+event.data);
            handleData(event.data);
        };
        ws.onclose = function(event) {
            console.log("Client notified socket has closed",event);
        };

    }

    function handleData(data)
    {
        var vals=data.split("\t");
        var msgType=vals[0];
        switch(msgType)
        {
            case "NAME":
                var msg=vals[1];
                var mes="NAME"+"\t"+msg+"_"+ username;
                send(mes);
                break;
            case "MSG":
                var val2s=vals[1].split("_");
                var from=val2s[0];
                var message=val2s[2];
                alert(from+":"+message);
                break;
            default:
                break;

        }
    }

</script>
</body>
</html>--%>
