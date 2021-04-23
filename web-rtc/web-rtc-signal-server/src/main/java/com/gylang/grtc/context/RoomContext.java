package com.gylang.grtc.context;

import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.gylang.grtc.domain.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author gylang
 * data 2021/4/23
 */
@Component
public class RoomContext {

    private static final String ROOM_WORK_THREAD_NAME_PREFIX = "ROOM_WORK";

    private LRUCache<String, Room> roomContainer = new LRUCache<>(100000);

    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000), ThreadFactoryBuilder.create().setNamePrefix(ROOM_WORK_THREAD_NAME_PREFIX).build());

    public Room create(String key, Room room) {

        Callable<Void> call = () -> {
            roomContainer.put(key, room);
            return null;
        };
        executorService.submit(call);
        try {
            call.call();
            return room;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Room getOrSet(String key, Room room) {
        Callable<Room> call = () -> {
            Room r = roomContainer.get(key);
            if (null == r) {
                roomContainer.put(key, room);
                r = room;
            }
            return r;
        };
        executorService.submit(call);
        try {
            return call.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void close(String key) {

        Callable<Room> call = () -> {
            roomContainer.remove(key);
            return null;
        };
        executorService.submit(call);
        try {
            call.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Room get(String key) {
        return roomContainer.get(key);
    }

    public List<Room> roomList() {
        return CollUtil.newArrayList(roomContainer.iterator());
    }


}
