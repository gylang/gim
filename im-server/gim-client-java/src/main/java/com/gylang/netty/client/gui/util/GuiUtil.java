package com.gylang.netty.client.gui.util;

import javafx.application.Platform;

import java.util.function.Supplier;

/**
 * @author gylang
 * data 2021/4/2
 */
public class GuiUtil {

    /**
     * 更新状态
     *
     * @param show 更新代码
     */
    public static void update(Supplier<Runnable> show) {

        Platform.runLater(show.get());
    }
}
