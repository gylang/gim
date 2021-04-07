package com.gylang.gim.web.client.gui.core;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/3
 */
public interface InitializerCallBack extends Initializable {



    @Override
    default void initialize(URL location, ResourceBundle resources) {
        beforeInit(location, resources);
        init(location, resources);
        afterInit(location, resources);
    }

    void beforeInit(URL location, ResourceBundle resources);

    void init(URL location, ResourceBundle resources);

    void afterInit(URL location, ResourceBundle resources);
}
