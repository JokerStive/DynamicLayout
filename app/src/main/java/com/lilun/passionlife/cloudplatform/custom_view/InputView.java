package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/21.
 */
public class InputView extends RelativeLayout {


    @Bind(R.id.reg_title)
    TextView title;
    @Bind(R.id.reg_hint)
    EditText desc;

    public InputView() {
        super(App.app);
        init();
    }


    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.InputView);
        String tit = attr.getString(R.styleable.InputView_reg_title);
        String hint = attr.getString(R.styleable.InputView_reg_hint);
        if(tit!=null){title.setText(tit);}
        if (hint!=null){desc.setHint(hint);}

        attr.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(App.app).inflate(R.layout.custom_reg, this);
        title = (TextView) view.findViewById(R.id.reg_title);
        desc = (EditText) view.findViewById(R.id.reg_hint);
    }

    public String getInput(){
        return desc.getText().toString();
    }


    public void setInput(String s){
        desc.setText(s);
    }
//
//    public void setTitle(String s){
//        title.setText(s);
//    }
//
//    public void setHint(String s){
//        desc.setHint(s);
//    }
}
