package com.gylang.gim.client.gui.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gylang.gim.client.gui.GuiStore;
import javafx.application.Application;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 打开新窗口需要在内置线程组处理
 *
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class GuiUtil {

    /**
     * 更新状态
     *
     * @param show 更新代码
     */
    public static void update(Runnable show) {

        Platform.runLater(show);
    }

    public static void openNewView(Class<? extends Application> view) {
        openNewView(view, new Object[]{});
    }

    public static void openNewViewAndCloseCurrent(Object current, Class<? extends Application> view) {
        openNewView(view);
        GuiStore.close(current);
    }
    public static void openNewViewAndCloseCurrent(Object current, Class<? extends Application> view, Object... argument) {
        openNewView(view, argument);
        GuiStore.close(current);
    }

    /**
     * 打开新窗口
     *
     * @param view 新窗口
     */
    public static void openNewView(Class<? extends Application> view, Object... argument) {

        // Read file fxml and draw interface.

        try {
            Application application = ReflectUtil.newInstance(view, argument);
            openNewView(application);
        } catch (UtilException e) {
            e.getCause();
        }

    }

    /**
     * 打开新窗口
     *
     * @param application 新窗口
     */
    public static void openNewView(Application application) {

        // Read file fxml and draw interface.

        GuiUtil.update(() -> {
            try {
                application.start(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
