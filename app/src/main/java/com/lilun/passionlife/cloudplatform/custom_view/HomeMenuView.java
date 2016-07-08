package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/6/3.
 */
public class HomeMenuView  extends RelativeLayout{
    private Context context;
    private Float wigth;
    private Float height;

    public HomeMenuView(Context context, Float wigth, Float height) {
        super(context);
        this.context = context;
        this.wigth = wigth;
        this.height = height;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//        layoutParams.
    }
}
