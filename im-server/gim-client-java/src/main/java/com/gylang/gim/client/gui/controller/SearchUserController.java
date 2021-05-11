package com.gylang.gim.client.gui.controller;
/**
 * @author gylang
 * data 2021/4/21
 */

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageRequest;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.client.api.UserApi;
import com.gylang.gim.client.call.ICallback;
import com.gylang.gim.client.gui.component.UserSearchItemC;
import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.gui.dialog.CommonDialog;
import com.gylang.gim.client.util.HttpUtil;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchUserController extends CustomApplication {

    @FXML
    private TextField searchText;
    @FXML
    public ListView<PtUserDTO> userListView;


    public SearchUserController() {
        super("好友搜索", "/fxml/UserSearch.fxml");
    }

    public static void main(String[] args) {

        launch(args);
    }




    @Override
    public void init(URL location, ResourceBundle resources) {
        // 构建listView

        userListView.setCellFactory(callback -> new UserSearchItemC());
        userListView.setItems(FXCollections.observableList(new ArrayList<>()));
    }



    @FXML
    public void searchEvent(Event event) {

        // 点击搜索事件
        PageRequest<PtUserDTO> request = new PageRequest<>();
        // 不管分页
        request.setSize(10000L);
        PtUserDTO ptUserDTO = new PtUserDTO();
        String search = searchText.getText();
        ptUserDTO.setUsername(search);
        ptUserDTO.setNickname(search);
        request.setParam(ptUserDTO);

        // 请求
        Call<CommonResult<PageResponse<PtUserDTO>>> userSearch = HttpUtil.getApi(UserApi.class)
                .userSearch(request);
        userSearch.enqueue(new ICallback<CommonResult<PageResponse<PtUserDTO>>>() {
            @Override
            public void success(Call<CommonResult<PageResponse<PtUserDTO>>> call, Response<CommonResult<PageResponse<PtUserDTO>>> response) {
                userListView.setItems(FXCollections.observableList(response.body().getData().getRecords()));
                userListView.refresh();
            }

            @Override
            public void fail(Call<CommonResult<PageResponse<PtUserDTO>>> call, CommonResult<?> response) {
                CommonDialog.getInstance().showMsg("请求失败");
            }
        });
    }

}
