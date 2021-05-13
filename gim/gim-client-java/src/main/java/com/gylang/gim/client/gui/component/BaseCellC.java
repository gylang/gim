package com.gylang.gim.client.gui.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

/**
 * @author gylang
 * data 2021/4/2
 */
public abstract class BaseCellC<T> extends ListCell<T> implements Callback<Class<?>, Object> {

    private final URL fxmlUrl;

    public BaseCellC(URL fxmlUrl) {
        this.fxmlUrl = fxmlUrl;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            // 如果这是一个空行
            setText(null);
            setGraphic(null);
        } else if (getGraphic() == null) {
            // 如果尚未创建需要显示的节点
            if (fxmlUrl == null) {
                // 不设置自定义的节点，使用默认的显示方式显示信息
                setText(item.toString());
            } else {
                // 创建节点，并绑定数据
                try {
                    FXMLLoader loader = new FXMLLoader(fxmlUrl);
                    loader.setController(this);
                    setGraphic(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bindData(item);
        } else {
            // 已经创建过节点，更新数据显示
            bindData(item);
        }

    }

    /**
     * 子类需要实现的用于绑定数据的方法。除了空行之外，该方法都会得到调用，可以使用这个方法覆盖默认行为。
     * 在新建Cell或者滚动{@link javafx.scene.control.ListView}时，这个方法会被反复调用以更新数据，
     * 但应用程序只会创建有限的Cell，此后持续复用它们，因此必须谨慎地处理Cell重用带来的一些问题，
     * 例如控件中会残留着已经消失在视野中的某行的信息。
     *
     * @param item 当前位置需要展示的类，不会为空
     */
    public abstract void bindData(T item);

    @Override
    public Object call(Class<?> param) {
        return this;
    }
}
