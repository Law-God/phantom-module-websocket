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
    <input id="text" type="text" /><button onclick="WS.sendMessage(document.getElementById('text').value)">Send</button>
    <button onclick="WS.close()">Close</button>
    <div id="message" style="width:300px;height:200px;border:1px solid red;overflow-y: auto">
    </div>
    </body>


    </body>
    <script>
        var WS = null;//定义websocket变量
        var lockReconnect = false;//避免重连
        var reconnectCount = 5;//重连次数
        var WSUrl = 'ws://192.25.102.187:8080/phantom/websocket';//websocket服务器地址

        //创建websocket连接
        function createWebSocket(url){
            try{
                if('WebSocket' in window){
                    WS = new WebSocket(url);
                    initEventHandle();
                }else{
                    console.log('您的浏览器不支持websockt');
                }
            }catch (e){
                console.log(e);
                reconnect(WSUrl);
            }
        }

        //初始化websocket各类处理事件
        function initEventHandle(){
            //连接打开时
            WS.onopen = function(event){
                setMessageInnerHTML("open connect readyState：" + WS.readyState );
                //hearCheck.start();
                //reconnectCount = 5;
            };

            //连接关闭时
            WS.onclose = function(){
                setMessageInnerHTML("close connect readyState：" + WS.readyState );
                //reconnect(WSUrl);
            };

            //连接发生错误时
            WS.onerror = function(){
                setMessageInnerHTML("error connect readyState：" + WS.readyState );
                reconnect(WSUrl);
            }

            //接收到消息时
            WS.onmessage = function(event){
                setMessageInnerHTML(event.data);
                /*if(event.data == '心跳包'){
                    hearCheck.reset().start();
                }*/
            };

            //发送消息
            WS.sendMessage = function(msg){
                if(!!WS){
                    if(WS.readyState === 1){//链接已经建立
                        WS.send(msg);
                    }else if(WS.readyState === 3){//连接已经关闭或不可用
                        setMessageInnerHTML("send connect readyState：" + WS.readyState);
                    }else if(WS.readyState === 2){//连接正在关闭
                        setMessageInnerHTML("send connect readyState：" + WS.readyState);
                    }else if(WS.readyState === 0){//连接尚未建立
                        setMessageInnerHTML("send connect readyState：" + WS.readyState);
                    }
                }else{
                    setMessageInnerHTML('未建立连接');
                }

            };
        }

        //重连服务器
        function reconnect(url){
            if(lockReconnect) return;
            lockReconnect = true;

            if(--reconnectCount > 0){
                setTimeout(function(){
                    createWebSocket(url);
                    lockReconnect = false;
                },1000);
            }
        }

        var hearCheck ={
            timeOut : 10000,//10秒
            timeOutObject : null,
            reset : function(){
                clearTimeout(this.timeOutObject);
                return this;
            },
            start : function(){
                this.timeOutObject = setTimeout(function(){
                    //这里发送一个心跳，后端收到，返回一个心跳信息
                    //onmessage拿到返回的心跳信息，说明连接正常
                    WS.sendMessage("心跳包");
                },this.timeOut)
            }
        }

        createWebSocket(WSUrl);

        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML){
            document.getElementById('message').innerHTML += innerHTML + '<br/>';
        }
    </script>

</html>
