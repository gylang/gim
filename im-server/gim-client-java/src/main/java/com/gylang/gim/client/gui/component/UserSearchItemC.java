package com.gylang.gim.client.gui.component;

import com.gylang.gim.api.constant.cmd.NotifyChatCmd;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.dto.PtUserDTO;
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
public class UserSearchItemC extends BaseCellC<PtUserDTO> implements Initializable {

    @FXML
    private ImageView avatar;
    @FXML
    private TextField nickname;
    @FXML
    private TextField username;
    @FXML
    private TextField message;


    private GimCallBack<MessageWrap> messageNotify;
    FriendApi friendApi = HttpUtil.getApi(FriendApi.class);

    public UserSearchItemC() {
        super(GuiClientApplication.class.getResource("/fxml/component/UserSearchItem.fxml"));
    }


    @Override
    public void bindData(PtUserDTO item) {

        nickname.setText(item.getNickname());
        username.setText(item.getUsername());


    }


    @FXML
    public void applyEvent(ActionEvent event) {
        UserApplyRequest request = new UserApplyRequest();
        request.setApplyId(UserStore.getInstance().getUid());
        request.setAnswerId(getItem().getId().toString());
        request.setLeaveWord(UserStore.getInstance().getNickname() + "申请添加好友");
        Call<CommonResult<Boolean>> answer = friendApi.applyFriend(request);
        answer.enqueue(new ICallback<CommonResult<Boolean>>() {
            @Override
            public void success(Call<CommonResult<Boolean>> call, Response<CommonResult<Boolean>> response) {
                CommonDialog.getInstance()
                        .showMsg("申请成功!");
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
         messageNotify = messageWrap -> {

         };
        SocketHolder.getInstance()
                .bind(ChatTypeEnum.NOTIFY, NotifyChatCmd.MESSAGE_NOTIFY, messageNotify);
    }

}
