package com.gylang.gim.client.gui;

import javafx.stage.Stage;
import lombok.Data;

/**
 * @author gylang
 * data 2021/4/2
 */
@Data
public class GuiStore {

    private Stage mainStage;


    private GuiStore() {
    }

    private static GuiStore guiStore = new GuiStore();


    public static GuiStore getGuiStore() {
        return guiStore;
    }

}
