# gylang-middleware

#### 介绍
中间件

#### 软件架构

##### 调用逻辑图

![image-20210303172307540](README.assets/image-20210303172307540.png)



##### 消息驱动

消息发送监听，用于扩展跨服消息发送，消息离线存储。

**通过实现接口**

```
MessageEventListener<T> 
```

**结合注解**

```
@MessageEvent("test") 
```

**实现事件的监听**

```java
@Slf4j
public class OfflineMsgEvent implements MessageEventListener<MessageWrap> {


    @Override
    @MessageEvent(EventTypeConst.OFFLINE_MSG_EVENT)
    public void onEvent(String key, MessageWrap message) {
      log.info("收到离线消息请求 ： {}", message);
    }
}

```

**发送事件**

```java
eventProvider.sendEvent(EventTypeConst.OFFLINE_MSG_EVENT, messageWrap)
```



##### 内置适配器

内置处理器，**通过判断当前返回值是否未null，进行判断是否需要调用下一个处理器**，进行业务分发。

**排序**：每个适配通过order进行判断实现处理器的顺序，如Qos服务的优先级比较高，可以过滤客户端的ACK，实现业务消息的后传，**order默认值：Integer.MAX_VALUE >> 1**

**DefaultRequestHandlerAdapter**

实现 IMRequestHandler类型的消息分发，实现很简单，就是一个kv map

```java
@Override
public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

    IMRequestHandler requestHandler = handlerMap.get(message.getCmd());
    if (null == requestHandler) {
        return null;
    }
    Object result = requestHandler.process(me, message);
    return null == result ? NlllSuccess.getInstance() : result;
}

----
    
@NettyHandler("chat")
public class ChatHandler implements IMRequestHandler {


    @Resource
    private MessageProvider messageProvider;
    @Resource
    private FillUserInfo fillUserInfo;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setContent("ACK");
        messageWrap.setQos(true);
        messageProvider.sendMsg(me, me, messageWrap);
        return message;
    }
}
```

**DefaultNettyControllerAdapter**

实现数据的类型转化，提取消息体，将数据转成我们需要的格式

```java
@Override
public Object process(ChannelHandlerContext ctx, IMSession me, MessageWrap message) {

    if (null != nettyControllerMap && null != paramTypeMap) {

        NettyController<?> gimController = nettyControllerMap.get(message.getCmd());
        Class<?> paramType = paramTypeMap.get(message.getCmd());
        if (null == gimController || null == paramType) {
            return null;
        }

        Object result = ((NettyController<Object>) gimController)
                .process(me, dataConverter.converterTo(paramType, message));
        return null == result ? NlllSuccess.getInstance() : result;
    }
    return null;
}
-----------

@NettyHandler("login")
public class TouristLoginHandler implements NettyController<Long> {

    @Resource
    private MessageProvider messageProvider;
    @Resource
    private IMGroupSessionRepository groupSessionRepository;
    private static final KeyLock<String> keyLock = new KeyLock<>();

    @Override
    public Object process(IMSession me, Long requestBody) {

        me.setAccount(requestBody);
        AbstractSessionGroup defaultGroup = getAndCreateGroup(me, groupSessionRepository);
        boolean join = defaultGroup.join(me);
        if (join) {
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setSender(me.getAccount());
            messageWrap.setContent(requestBody + "加入群聊组");
            messageProvider.sendGroup(me, "default", messageWrap);

        return messageWrap;
        }
        return null;
    }
}
```

##### 全局配置类

```java
/**
 * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
 */
private CustomInitializer<?> serverChannelInitializer;
/** 事件监听 用于发送消息 */
private EventProvider eventProvider;
/** 事件上下文 map，用于调用messageEventListener.OnEvent */
private EventContext eventContext;
/** 数据类型转化 本来想实现protobuf的适配，但是实现protobuf，扩展比较麻烦，所以暂时舍弃 */
private DataConverter dataConverter;
/** 单用户会话工厂 用于存储会话 */
private IMSessionRepository sessionRepository;
/** 用户组会话工厂 会话组，群组，房间组 */
private IMGroupSessionRepository groupSessionRepository;
/** 消息发送provider 消息发送，消息主动推送给客户端 */
private MessageProvider messageProvider;
/** 事件监听列表 所有的事件监听器（后面可以使用ObjectWrap，如果对象被代理可能就拿不到注解）*/
private List<MessageEventListener<?>> messageEventListener;
/** 业务请求适配器  业务是配置 qos，controller，Handler... */
private List<BizRequestAdapter<?>> bizRequestAdapterList;
/** 适配分发器 用于第一层对adapter的链式调用*/
private DispatchAdapterHandler dispatchAdapterHandler;
/** 线程池 （开始考虑使用在消息组发时进行异步发送）*/
private ThreadPoolExecutor poolExecutor;
/** 填充客户信息 这个可能也应该删除，可以直接通过拦截器实现就好了，
如果不使用拦截器，可以用这个进行消息填充，甚至通过Adapter也是可以 */
private NettyUserInfoFillHandler nettyUserInfoFillHandler;
/** 消息处理拦截器 消息进来、出去进行拦截调用 */
private List<NettyIntercept> nettyInterceptList;
/** 上下文可能使用到的对象包装类集合 主要用于适配业务调用的bean注入 */
private List<ObjectWrap> objectWrapList;
/** qos 发送 处理实现（可以不耦合到这里） */
private IMessageReceiveQosHandler iMessageReceiveQosHandler;
/** qos 接收 处理实现 （可以不耦合到这里） */
private IMessageSenderQosHandler iMessageSenderQosHandler;

/** 配置属性 用于存储配置 */
private NettyProperties gimProperties;
/**
 * 配置信息
 */
@Getter
private Map<String, Object> properties;
```



#### 安装教程

##### 整合spring

参考netty-im-server



**启动注解**

```java
@SpringBootApplication
@EnableIM
public class IMApplication {

    public static void main(String[] args) {

        SpringApplication.run(IMApplication.class, args);
    }
}
```

**编写业务处理类**

```java
@NettyHandler("chat")
@Component
@Slf4j
public class ChatHandler implements IMRequestHandler {


    @Resource
    private MessageProvider messageProvider;

    @Override
    public Object process(IMSession me, MessageWrap message) {


        MessageWrap messageWrap = new MessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setContent("ACK");
        messageWrap.setQos(true);
        messageProvider.sendMsg(me, me, messageWrap);
        return message;
    }
}
```

**客户端发送消息**

![image-20210303180009499](README.assets/image-20210303180009499.png)

**vue的测试小demo**在 vue-admin.zip



##### 非framework

**配置config**

参考 netty-console-chat

```java
gimGlobalConfiguration.setServerChannelInitializer(new WebJsonInitializer());
gimGlobalConfiguration.setEventProvider(new DefaultEventProvider());
gimGlobalConfiguration.setEventContext(new EventContext());
gimGlobalConfiguration.setDataConverter(new JsonConverter());
gimGlobalConfiguration.setSessionRepository(new DefaultIMRepository());
gimGlobalConfiguration.setGroupSessionRepository(new DefaultGroupRepository());
gimGlobalConfiguration.setMessageProvider(new DefaultMessageProvider());
gimGlobalConfiguration.setMessageEventListener(new ArrayList<>());
List<BizRequestAdapter<?>> bizRequestAdapterList = new ArrayList<>();
bizRequestAdapterList.add(new DefaultNettyControllerAdapter());
bizRequestAdapterList.add(new DefaultRequestHandlerAdapter());
gimGlobalConfiguration.setBizRequestAdapterList(bizRequestAdapterList);
gimGlobalConfiguration.setDispatchAdapterHandler(new DefaultAdapterDispatch());
gimGlobalConfiguration.setNettyInterceptList(new ArrayList<>());
gimGlobalConfiguration.setIMessageReceiveQosHandler(new DefaultIMessageReceiveQosHandler());
gimGlobalConfiguration.setIMessageSenderQosHandler(new DefaultIMessageSendQosHandler());

// 启动服务
NettyConfigHolder.init();
//业务执行参数
NettyConfiguration gimGlobalConfiguration = NettyConfigHolder.getInstance();
gimGlobalConfiguration.addObjectWrap(ObjectWrapUtil.resolver(JoinGroupHandler.class, new JoinGroupHandler()));
gimGlobalConfiguration.addObjectWrap(ObjectWrapUtil.resolver(SimpleChatGroupHandler.class, new SimpleChatGroupHandler()));
gimGlobalConfiguration.setMessageEventListener(CollUtil.newArrayList(new TestEventListener()));
new SimpleNettyConfigurationInitializer().initConfig(gimGlobalConfiguration);
IMServer imServer = new IMServer();
imServer.setNettyConfig(gimGlobalConfiguration);
imServer.start();
```

#### 使用说明

**学习为主，有啥好想法和不足可联系我，出于爱好开发的一个小框架**

各位大佬（爱好者v( •̀ ω •́ )✧）指点方式：QQ：1179346492，交流群：179493777