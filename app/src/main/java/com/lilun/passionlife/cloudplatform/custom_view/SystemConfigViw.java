package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by youke on 2016/6/20.
 */
public class SystemConfigViw extends LinearLayout {
    @Bind(R.id.module_icon)
    ImageView moduleIcon;
    @Bind(R.id.module_title)
    TextView moduleTitle;
    private Context cx;

    public SystemConfigViw(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        init();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.SystemConfigViw);
        String title = attr.getText(R.styleable.SystemConfigViw_module_title).toString();
        if(title!=null)moduleTitle.setText(title);
        int res = attr.getResourceId(R.styleable.SystemConfigViw_module_icon,0);
        if(res!=-1)moduleIcon.setBackgroundResource(res);
    }

    private void init() {
        View view = View.inflate(cx, R.layout.item_system_config, this);
        ButterKnife.bind(view);
    }


}
