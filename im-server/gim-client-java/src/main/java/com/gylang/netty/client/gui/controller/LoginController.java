package com.gylang.netty.client.gui.controller;

/**
 * @author gylang
 * data 2021/4/1
 */

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.request.LoginRequest;
import com.gylang.im.api.domain.request.RegistryRequest;
import com.gylang.im.api.domain.response.LoginResponse;
import com.gylang.netty.client.api.AuthApi;
import com.gylang.netty.client.call.ICallback;
import com.gylang.netty.client.gui.GuiStore;
import com.gylang.netty.client.gui.dialog.CommonDialog;
import com.gylang.netty.client.gui.util.GuiUtil;
import com.gylang.netty.client.util.HttpUtil;
import com.gylang.netty.client.util.Store.UserStore;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.net.URL;

@Slf4j
public class LoginController extends Application {

    @FXML
    public Button loginBtn;
    @FXML
    private Button registerBtn;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private Stage current;


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            current = primaryStage;
            GuiStore.getGuiStore().setMainStage(current);
            // Read file fxml and draw interface.
            URL resource = getClass().getResource("/fxml/Login.fxml");
            log.info("[start] : {}", resource);
            Parent root = FXMLLoader.load(resource);
            primaryStage.setTitle("My Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }


    public void doLogin(ActionEvent actionEvent) {
        log.info(username.getText());
        log.info(password.getText());
        log.info("[doLogin] : {}", actionEvent);

        AuthApi authApi = HttpUtil.getApi(AuthApi.class);
        LoginRequest request = new LoginRequest();
        request.setUsername(username.getText());
        request.setPassword(password.getText());
        Call<CommonResult<LoginResponse>> resultCall = authApi.login(request);


        resultCall.enqueue(new ICallback<CommonResult<LoginResponse>>() {
            @Override
            public void success(Call<CommonResult<LoginResponse>> call, Response<CommonResult<LoginResponse>> response) {
                CommonResult<LoginResponse> body = response.body();
                LoginResponse data = body.getData();
                log.info("[登录成功] : " + data.getUsername());
                log.info("[登录成功] : " + data.getToken());
                UserStore instance = UserStore.getInstance();
                instance.setToken(data.getToken());
                instance.setUsername(data.getUsername());
                instance.setNickname(data.getNickname());
                data.setUsername("111");
                log.info(data.getUsername());
                // 连接socket
//                CommonDialog.getInstance().showMsg(body.getMsg());
                GuiUtil.openNewView(MainController.class);
                GuiUtil.update(() -> GuiStore.getGuiStore().getMainStage().hide());
            }

            @Override
            public void fail(Call<CommonResult<LoginResponse>> call, CommonResult<?> response) {
                log.info("[登录失败] : " + response.getMsg());

                CommonDialog.getInstance().showMsg(response.getMsg());
            }
        });
    }

    public void doRegister(ActionEvent actionEvent) {

        log.info(username.getText());
        log.info(password.getText());
        log.info("[doLogin] : {}", actionEvent);

        AuthApi authApi = HttpUtil.getApi(AuthApi.class);
        RegistryRequest request = new RegistryRequest();
        request.setUsername(username.getText());
        request.setPassword(password.getText());
        Call<CommonResult<LoginResponse>> resultCall = authApi.registry(request);


        resultCall.enqueue(new ICallback<CommonResult<LoginResponse>>() {
            @Override
            public void success(Call<CommonResult<LoginResponse>> call, Response<CommonResult<LoginResponse>> response) {
                CommonResult<LoginResponse> body = response.body();
                LoginResponse data = body.getData();
                log.info("[注册成功] : " + data.getUsername());
                log.info("[注册成功] : " + data.getToken());
                UserStore.getInstance()
                        .setToken(data.getToken());
                data.setUsername("111");
                log.info(data.getUsername());
                // 连接socket
                CommonDialog.getInstance().showMsg(body.getMsg());
                current.close();
            }

            @Override
            public void fail(Call<CommonResult<LoginResponse>> call, CommonResult<?> response) {
                log.info("[注册失败] : " + response.getMsg());

                CommonDialog.getInstance().showMsg(response.getMsg());
            }
        });
    }
}
