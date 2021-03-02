/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.gylang.netty.sdk.domain.model;

import com.gylang.netty.sdk.constant.CommConst;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IoSession包装类,集群时 将此对象存入表中
 */
@Data
public class IMSession implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String HOST = "HOST";
    public static final int STATE_ENABLED = 0;
    public static final int STATE_DISABLED = 1;
    public static final int APNS_ON = 1;
    public static final int APNS_OFF = 0;

    public static final String CHANNEL_IOS = "ios";
    public static final String CHANNEL_ANDROID = "android";
    public static final String CHANNEL_WINDOWS = "windows";
    private transient Channel session;


    /**
     * session绑定的用户账号
     */
    private String account;

    /**
     * session在本台服务器上的ID
     */
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */
    private String deviceId;

    /**
     * session绑定的服务器IP
     */
    private String serverIp;

    /**
     * 终端设备类型
     */
    private String channel;

    /**
     * 终端设备型号
     */
    private String deviceModel;

    /**
     * 终端应用版本
     */
    private String clientVersion;

    /**
     * 终端系统版本
     */
    private String systemVersion;

    /**
     * 登录时间
     */
    private Long bindTime;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 位置
     */
    private String location;

    /**
     * APNs推送状态
     */
    private int apns;

    /**
     * 状态
     */
    private int state;
    /**
     * 加入的群组
     */
    private List<AbstractSessionGroup> groupList = new ArrayList<>();

    public IMSession(Channel session) {
        this.session = session;
        this.nid = session.id().asLongText();
    }


    public IMSession() {

    }

    public String getNid() {
        return session.id().asLongText();
    }

    public String getAccount() {
        this.account = (String) getAttribute(CommConst.KEY_ACCOUNT);
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;

        setAttribute(CommConst.KEY_ACCOUNT, account);
    }


    public void setAttribute(String key, Object value) {
        if (session != null) {
            session.attr(AttributeKey.valueOf(key)).set(value);
        }
    }

    public boolean containsAttribute(String key) {
        if (session != null) {
            return session.hasAttr(AttributeKey.valueOf(key));
        }
        return false;
    }

    public Object getAttribute(String key) {
        if (session != null) {
            return session.attr(AttributeKey.valueOf(key)).get();
        }
        return null;
    }

    public void removeAttribute(String key) {
        if (session != null) {
            session.attr(AttributeKey.valueOf(key)).set(null);
        }
    }

    public SocketAddress getRemoteAddress() {
        if (session != null) {
            return session.remoteAddress();
        }
        return null;
    }


    public boolean isConnected() {
        return (session != null && session.isActive()) || state == STATE_ENABLED;
    }

    public void closeNow() {
        if (session != null) {
            session.close();
        }
    }

    public void closeOnFlush() {
        if (session != null) {
            session.close();
        }
    }

    public boolean isIOSChannel() {
        return Objects.equals(channel, CHANNEL_IOS);
    }

    public boolean isAndroidChannel() {
        return Objects.equals(channel, CHANNEL_ANDROID);
    }

    public boolean isWindowsChannel() {
        return Objects.equals(channel, CHANNEL_WINDOWS);
    }

    public boolean isApnsEnable() {
        return Objects.equals(apns, APNS_ON);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IMSession) {
            IMSession target = (IMSession) o;
            return Objects.equals(target.deviceId, deviceId) && Objects.equals(target.nid, nid)
                    && Objects.equals(target.serverIp, serverIp);
        }
        return false;
    }


}
