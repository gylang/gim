package com.gylang.spring.netty;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.*;
import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
import com.gylang.spring.netty.register.*;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gylang
 * data 2020/11/11
 * @version v0.0.1
 */
@Configuration
@Import({IMContextConfig.class, DataConverterRegister.class, MessageProviderRegister.class,
        IMNotifyRegister.class, IMAdapterRegister.class, ChannelInitializerRegister.class})
@ComponentScan("com.gylang.spring.netty.custom")
public class IMAutoConfigration implements InitializingBean {


    /**
     * 消息发送器
     */
    @Autowired(required = false)
    private MessageProvider messageProvider;
    /**
     * netty配置
     */
    @Autowired(required = false)
    private NettyConfig nettyConfig;

    /**
     * 数据类型转换器
     */
    @Autowired(required = false)
    private DataConverter dataConverter;
    /**
     * 消息发送器
     */
    @Autowired(required = false)
    private NotifyProvider notifyProvider;
    /**
     * 消息通知上下文
     */
    @Autowired(required = false)
    private NotifyContext notifyContext;

    @Resource(name = "imRepository")
    private Object sessionRepository;
    @Resource(name = "imGroupRepository")
    private Object groupRepository;

    @Resource(name = "imAdapterDispatch")
    private IMRequestAdapter adapter;
    @Autowired
    private IMContext imContext;

    @Autowired(required = false)
    private List<CircularProcess> circularProcessList;

    @Autowired(required = false)
    private FillUserInfoContext fillUserInfoContext;
    @Autowired(required = false)
    private List<IMRequestAdapter> requestAdapterList;


    /**
     * spring 容器
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${im.converterType:com.gylang.netty.sdk.conveter.JsonConverter}")
    private String converterType;

    @Override
    public void afterPropertiesSet() throws Exception {

        // netty 服务启动器
        ImApplicationContext imApplicationContext = initApplication();
        imApplicationContext.start();
        // 构建bean
        BeanUtils.copyProperties(imApplicationContext.imContext(), this.imContext);

        // 新加的adapter
        if (null != requestAdapterList) {
            requestAdapterList.remove(adapter);
            adapter.register(requestAdapterList);
        }
        // 解决循环依赖
        List<Object> autowired = new ArrayList<>();

        List<?> objects = adapter.mappingList();
        List<IMRequestAdapter> adapterList = adapter.mappingList().stream()
                .map(o -> (IMRequestAdapter) o)
                .collect(Collectors.toList());

        List<?> handler = adapterList.stream()
                .flatMap(imRequestAdapter -> imRequestAdapter.mappingList().stream())
                .collect(Collectors.toList());


        addCircular(autowired, fillUserInfoContext);
        addCircular(autowired, objects);
        addCircular(autowired, handler);
        if (CollUtil.isNotEmpty(circularProcessList)) {
            List<Object> collect = circularProcessList.stream()
                    .flatMap(circularProcess -> circularProcess.autowired().stream())
                    .collect(Collectors.toList());
            addCircular(autowired, collect);
        }
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        autowired.forEach(factory::autowireBean);
        System.out.println(imContext);
    }

    private void addCircular(List<Object> autowired, List<?> objects) {

        if (CollUtil.isNotEmpty(objects)) {
            autowired.addAll(objects);
        }
    }

    private void addCircular(List<Object> autowired, Object obj) {

        if (null != obj) {
            autowired.add(obj);
        }
    }

    private ImApplicationContext initApplication() {

        ImFactoryBuilder build = ImFactoryBuilder.builder()
                .dataConverter(dataConverter)
                .groupRepository(groupRepository)
                .sessionRepository(sessionRepository)
                .messageProvider(messageProvider)
                .dispatchAdapter(adapter)
                .messageContext(notifyContext)
                .nettyConfig(null == nettyConfig ? new NettyConfig() : nettyConfig)
                .notifyProvider(notifyProvider)
                .build();
        DefaultImApplicationContext defaultImApplicationContext = new DefaultImApplicationContext();
        defaultImApplicationContext.doInit(build);
        return defaultImApplicationContext;
    }


}
