package com.gylang.netty.client.woker;

/**
 * @author gylang
 * data 2021/3/30
 */
public class SocketHolder {

    private SocketHolder() {
    }

    public static SocketManager getInstance() {
        return HolderLoad.SOCKET_MANAGER;
    }

    private static class HolderLoad {
        private HolderLoad() {
        }

        private static final SocketManager SOCKET_MANAGER = new SocketManager();
    }
}
