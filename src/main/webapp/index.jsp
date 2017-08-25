<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%
    String sessionId =  request.getSession().getId();
    request.setAttribute("sessionId",sessionId);
%>
<!DOCTYPE html>
    <head>
        <meta charset="utf-8"/>
        <title>websocket测试</title>
    </head>
    <body>
    Welcome<br/>
    <input id="text" type="text" /><button onclick="send()">Send</button>
    <button onclick="closeWebSocket()">Close</button>
    <div id="message">
    </div>
    </body>

<script type="text/JavaScript">
    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://192.25.102.187:8080//phantom/websocket");
    }
    else{
        alert('Not support websocket');
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){
        setMessageInnerHTML("open");
    };

    //接收到消息的回调方法
    websocket.onmessage = function(){
        setMessageInnerHTML(event.data);
    };

    //连接关闭的回调方法
    websocket.onclose = function(){
        setMessageInnerHTML("close");
        websocket = new WebSocket("ws://192.25.102.187:8080//phantom/websocket");
    };

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    };

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    //发送消息
    function send(){
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>
    </body>
</html>
