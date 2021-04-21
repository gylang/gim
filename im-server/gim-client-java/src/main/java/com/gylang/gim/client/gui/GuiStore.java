package com.gylang.gim.client.gui;

import com.gylang.gim.client.gui.util.GuiUtil;
import javafx.stage.Stage;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gylang
 * data 2021/4/2
 */
@Data
public class GuiStore {

    private Stage mainStage;

    private static final Map<Object, Stage> stageMap = new ConcurrentHashMap<>();

    private GuiStore() {
    }

    private static GuiStore guiStore = new GuiStore();

    public static void close(Object obj) {

        Stage stage = stageMap.get(obj);
        if (null != stage) {
            GuiUtil.update(stage::close);
        }
    }

    public static void store(Object obj, Stage stage) {
        stageMap.put(obj, stage);
    }

    public static GuiStore getGuiStore() {
        return guiStore;
    }

}
