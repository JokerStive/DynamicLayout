package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lilun.passionlife.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/21.
 */
public class RegItemView extends RelativeLayout {


    @Bind(R.id.reg_title)
    TextView title;
    @Bind(R.id.reg_hint)
    EditText desc;
    private Context cx;

    public RegItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;

        init();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.RegItemView);
        String tit = attr.getString(R.styleable.RegItemView_reg_title);
        String hint = attr.getString(R.styleable.RegItemView_reg_hint);
        if(tit!=null){title.setText(tit);}
        if (hint!=null){desc.setHint(hint);}
    }

    private void init() {
        View view = View.inflate(cx, R.layout.custom_reg, this);
        ButterKnife.bind(view);
    }

    public String getInput(){
        return desc.getText().toString();
    }


    public void setInput(String s){
        desc.setText(s.toString());
    }
}
