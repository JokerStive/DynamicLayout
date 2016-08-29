package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.BaseX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view.IsView;

/**
 * Created by youke on 2016/8/15.
 * 判断类型的配置项
 */
public class IsX extends BaseX {
    private boolean isX;
    private IsView isView;
    public IsX(String id, String title) {
        this.id = id;
        this.title = title;
        this.isView = new IsView();
        isView.setTitle(title);

        isView.setOnIsXChoiseListener(isXChoise -> isX = isXChoise);
    }

    public IsView getIsView() {
        return isView;
    }


    public void setX(boolean x) {
        isX = x;
        isView.setChoised(isX());
    }

    public boolean isX() {
        return isX;
    }


}
