package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;

/**
 * Created by Administrator on 2016/6/21.
 */
public class InputView1 extends RelativeLayout {



    TextView title;

    EditText desc;

    public InputView1() {
        super(App.app);
        init();
    }



    private void init() {
        View view = LayoutInflater.from(App.app).inflate(R.layout.item_input_view, this);
        title = (TextView) view.findViewById(R.id.reg_title);
        desc = (EditText) view.findViewById(R.id.reg_hint);
    }

    public String getInput(){
        return desc.getText().toString();
    }


    public void setInput(String s){
        desc.setText(s);
    }

    public void setTitle(String s){
        title.setText(s);
    }

    public void setHint(String s){
        desc.setHint(s);
    }
}
