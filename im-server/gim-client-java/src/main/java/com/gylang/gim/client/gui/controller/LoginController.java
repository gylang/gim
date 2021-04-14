package com.gylang.gim.client.gui.controller;

/**
 * @author gylang
 * data 2021/4/1
 */

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.AdminChatCmd;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.request.LoginRequest;
import com.gylang.gim.api.domain.request.RegistryRequest;
import com.gylang.gim.api.domain.response.LoginResponse;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.client.gui.GuiStore;
import com.gylang.gim.client.util.HttpUtil;
import com.gylang.gim.client.util.store.UserStore;
import com.gylang.gim.client.api.AuthApi;
import com.gylang.gim.client.call.ICallback;
import com.gylang.gim.client.gui.dialog.CommonDialog;
import com.gylang.gim.client.gui.util.GuiUtil;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;
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
                SocketManager socketManager = SocketHolder.getInstance();

                MessageWrap messageWrap = MessageWrap.builder()
                        .cmd(AdminChatCmd.REMOTE_LOGIN)
                        .type(ChatTypeEnum.PUSH_CHAT.getType())
                        .clientMsgId(IdUtil.getSnowflake(1, 1).nextIdStr())
                        .qos(QosConstant.ACCURACY_ONE_ARRIVE)
                        .content(JSON.toJSONString(data.getToken()))
                        .build();

                socketManager.connect("127.0.0.1", 46001,messageWrap, str -> {
                    if ("1".equals(str)) {
                        System.out.println("连接成功！");
                    } else {
                        System.out.println(str);
                    }
                });
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
