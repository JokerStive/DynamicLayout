package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.BaseX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view.InputView1;

/**
 * Created by youke on 2016/8/15.
 * "输入"类型的配置项
 */
public class InputX extends BaseX {
    private String inputX;
    private InputView1 inputView;
    public InputX(String id, String title) {
        this.id = id;
        this.title = title;
        this.inputView = new InputView1();
        inputView.setTitle(title);
//        inputView.setHint(title);
    }

    public InputView1 getInputView() {
        return inputView;
    }

    public String getInputX() {
        return inputView.getInput();
    }

    public void setInputX(String inputX) {
        this.inputX = inputX;
        inputView.setInput(inputX);
    }
}
