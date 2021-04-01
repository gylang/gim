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
import com.gylang.netty.client.util.HttpUtil;
import com.gylang.netty.client.util.Store.UserStore;
import com.sun.deploy.net.HttpRequest;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import sun.rmi.transport.proxy.HttpReceiveSocket;

import javax.naming.NamingEnumeration;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.util.Collection;

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
    private Stage primaryStage;
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
            URL resource = getClass().getResource("/Login.fxml");
            System.out.println(resource);
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
        System.out.println(username.getText());
        System.out.println(password.getText());
        System.out.println(actionEvent);

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
                System.out.println("[登录成功] : " + data.getUsername());
                System.out.println("[登录成功] : " + data.getToken());
                UserStore.getInstance()
                        .setToken(data.getToken());
                data.setUsername("111");
                System.out.println(data.getUsername());
                // 连接socket

            }

            @Override
            public void fail(Call<CommonResult<LoginResponse>> call, CommonResult<?> response) {
                System.out.println("[登录失败] : " + response.getMsg());

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text(response.getMsg()));
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
    }
}
