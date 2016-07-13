package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ViewContainer extends FlowLayout {

    private BaseAdapter adapter;
    private String titl;
    private Context context;
    private TextView title;
    private TagFlowLayout content;

    //
    public ViewContainer(Context context, String titl, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.titl = titl;
        this.adapter = adapter;
        init();
    }


    public ViewContainer(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }


    private void init() {
        setHorizontalSpacing(UIUtils.dip2px(App.app,10));
        setVerticalSpacing(UIUtils.dip2px(App.app,10));
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_container, this);
        title = (TextView) view.findViewById(R.id.container_title);
//        content = (TagFlowLayout) view.findViewById(R.id.container_content);
//        title.setText(titl);
        setAdapter(adapter);
    }



}
