package com.gylang.gim.client.gui.component.list;

import com.gylang.gim.api.constant.AnswerType;
import com.gylang.gim.api.constant.cmd.NotifyChatCmd;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.dto.UserApplyDTO;
import com.gylang.gim.api.dto.request.UserApplyRequest;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.client.GuiClientApplication;
import com.gylang.gim.client.api.FriendApi;
import com.gylang.gim.client.call.ICallback;
import com.gylang.gim.client.gui.dialog.CommonDialog;
import com.gylang.gim.client.util.HttpUtil;
import com.gylang.gim.client.util.store.UserStore;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.call.GimCallBack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class FriendApplyNotifyComponent extends BaseCell<UserApplyDTO> implements Initializable {

    @FXML
    private ImageView avatar;
    @FXML
    private TextField nickname;
    @FXML
    private TextField username;
    @FXML
    private TextField message;
    @FXML
    private Button agree;
    @FXML
    private Button reject;


    FriendApi friendApi = HttpUtil.getApi(FriendApi.class);

    public FriendApplyNotifyComponent() {
        super(GuiClientApplication.class.getResource("/fxml/component/FriendApplyNotify.fxml"));
    }


    @Override
    public void bindData(UserApplyDTO item) {

        nickname.setText(item.getNickname());
        username.setText(item.getUsername());
        if (AnswerType.AGREEMENT == item.getAnswerType()) {
            agree.setDisable(true);
            reject.setDisable(true);
            message.setText("已同意");
        } else if (AnswerType.REJECT == item.getAnswerType()) {
            agree.setDisable(true);
            reject.setDisable(true);
            message.setText("已拒绝");
        }

    }


    @FXML
    public void agree(ActionEvent event) {
        UserApplyRequest request = new UserApplyRequest();
        UserApplyDTO item = getItem();
        request.setApplyId(UserStore.getInstance().getUid());
        request.setAnswerId(item.getAnswerId());
        request.setAnswerType(AnswerType.AGREEMENT);
        Call<CommonResult<Boolean>> answer = friendApi.answer(request);
        answer.enqueue(new ICallback<CommonResult<Boolean>>() {
            @Override
            public void success(Call<CommonResult<Boolean>> call, Response<CommonResult<Boolean>> response) {
                CommonDialog.getInstance()
                        .showMsg("已同意");
                item.setAnswerType(AnswerType.AGREEMENT);
            }

            @Override
            public void fail(Call<CommonResult<Boolean>> call, CommonResult<?> response) {
                CommonDialog.getInstance()
                        .showMsg("失败: " + response.getMsg());

            }
        });

    }

    @FXML
    public void reject(ActionEvent event) {
        UserApplyRequest request = new UserApplyRequest();
        UserApplyDTO item = getItem();
        request.setApplyId(UserStore.getInstance().getUid());
        request.setAnswerId(item.getAnswerId());
        request.setAnswerType(AnswerType.REJECT);
        Call<CommonResult<Boolean>> answer = friendApi.answer(request);
        answer.enqueue(new ICallback<CommonResult<Boolean>>() {
            @Override
            public void success(Call<CommonResult<Boolean>> call, Response<CommonResult<Boolean>> response) {
                CommonDialog.getInstance()
                        .showMsg("已拒绝");
                item.setAnswerType(AnswerType.REJECT);
            }

            @Override
            public void fail(Call<CommonResult<Boolean>> call, CommonResult<?> response) {
                CommonDialog.getInstance()
                        .showMsg("失败: " + response.getMsg());
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //
        GimCallBack<MessageWrap> messageNotify = new GimCallBack<MessageWrap>() {
            @Override
            public void call(MessageWrap messageWrap) {

            }
        };
        // todo 一半
        SocketHolder.getInstance()
                .bind(ChatTypeEnum.NOTIFY.getType(), NotifyChatCmd.MESSAGE_NOTIFY, null);
    }
}
