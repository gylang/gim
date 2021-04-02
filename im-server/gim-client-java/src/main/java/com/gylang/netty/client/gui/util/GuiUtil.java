package com.gylang.netty.client.gui.util;

import cn.hutool.core.util.ReflectUtil;
import com.gylang.netty.client.gui.GuiStore;
import javafx.application.Application;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

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

    /**
     * 打开新窗口
     *
     * @param view 新窗口
     */
    public static void openNewView(Class<? extends Application> view) {

        // Read file fxml and draw interface.

        GuiUtil.update(() -> {
            try {
                Application application = ReflectUtil.newInstance(view);
                application.start(GuiStore.getGuiStore().getMainStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
