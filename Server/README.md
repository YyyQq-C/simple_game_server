# Server 游戏整体框架
### 目录结构  
     ├─Core 游戏核心框架 
     │  └─src  
     │     └─main  
     │       ├─java  
     │       │  ├─agent 热更相关    
     │       │  ├─constant 系统常量类 
     │       │  ├─message 消息协议相关 
     │       │  ├─net  
     │       │  │  ├─handler tcp、http相关Handler 
     │       │  │  ├─http  
     │       │  │  │  ├─client 异步http客户端 
     │       │  │  │  └─server http服务器 
     │       │  │  └─tcp  
     │       │  │      └─coder 编解码器 
     │       │  ├─script 脚本相关 
     │       │  ├─thread 线程相关 
     │       │  └─utils 工具类 
     │       └─resources 资源 
     ├─Game  
     │  ├─config 游戏相关配置文件 
     │  └─src   
     │      └─main  
     │        ├─java  
     │        └─resources  
     └─Message  
         └─src  
             └─main  
               ├─java  
               │  ├─binary 二进制协议 
               │  └─protocol protocolBuf 协议 
               └─resources  

---
### 协议支持
* google protocolBuf
* binary-message(自研)
### redis支持
* redis单机版
* redis集群版
### 热更方式
* 使用Agent。局限于方法块的修改，不能对变量、方法进行任何修改，不能增加、删除变量、方法。
* 使用逻辑和数据分离方式，将逻辑代码以脚本方式实现。脚本内部不能有数据存在。

_后续将添加RPC服务器_