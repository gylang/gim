package com.gylang.netty.client.gui.dialog;

import com.gylang.netty.client.gui.util.GuiUtil;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author gylang
 * data 2021/4/2
 */
public class CommonDialog {

    private CommonDialog() {
    }

    private static final CommonDialog COMMONDIALOG = new CommonDialog();

    public static CommonDialog getInstance() {
        return COMMONDIALOG;
    }

    public void showMsg(String msg) {
        GuiUtil.update(() -> () -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text(msg));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
    }
}
