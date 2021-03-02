# netty-sdk

#### 介绍
netty-sdk 将业务代码抽离分发, 简化业务开发过程

#### 软件架构
软件架构说明




#### 使用说明

1.  客户端简单使用 websocket 协议(默认)
2.  服务端使用 java版 netty-console-chat spring版 netty-im-server

#### 整合spring
1. 在启动类中使用 @EnableIM 
2. 新建一个继承 IMRequestHandler接口 并声明 @NettyHandler 和 @Component 即完成最简单的hello world
3. 实现NettyController 接口 并声明 @NettyHandler 和 @Component, 通过泛型来实现对json数据转成泛型类型
4. 基于方法的方式实现对业务处理(改方式是在netty-sdk-spring-starter中实现) 通过@SpringNettyController 和 @NettyMapping来实现业务分发

### demo 可查看
netty-console-chat
netty-im-server
### 交互结构
当前仅实现了了json格式 参考 com/gylang/netty/sdk/domain/MessageWrap.java
当前demo只用到两个属性
```json
{
  "cmd" : "业务分发key",
  "content" : "字符串或者对象数组, 交互的实际类容"
}
```
 

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
