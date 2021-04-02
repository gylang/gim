package com.gylang.netty.client.gui;

/**
 * @author gylang
 * data 2021/4/1
 */

import com.gylang.netty.client.SocketClientApplication;
import com.gylang.netty.client.api.AuthApi;
import com.gylang.netty.client.call.ICallback;
import com.gylang.netty.client.domain.CommonResult;
import com.gylang.netty.client.domain.request.LoginRequest;
import com.gylang.netty.client.domain.response.LoginResponse;
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
public class Login extends Application {

    @FXML
    public Button loginBtn;
    @FXML
    private Button registerBtn;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public static void main(String[] args) {

        SocketClientApplication.main(args);
        launch(Login.class, args);
        startApp();
    }

    private static void startApp() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
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
                UserStore.getInstance()
                        .setToken(data.getToken());
                data.setUsername("111");
                log.info(data.getUsername());
                // 连接socket
                CommonDialog.getInstance().showMsg(body.getMsg());
                GuiUtil.update();
            }

            @Override
            public void fail(Call<CommonResult<LoginResponse>> call, CommonResult<?> response) {
                log.info("[登录失败] : " + response.getMsg());

                CommonDialog.getInstance().showMsg(response.getMsg());
            }
        });
    }
}
