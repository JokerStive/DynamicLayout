package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lilun.passionlife.cloudplatform.ui.App;

/**
 * Created by Administrator on 2016/8/11.
 */
public class HorizontalScrollLieanLayout  extends HorizontalScrollView{

    private LinearLayout linearLayout;

    public HorizontalScrollLieanLayout(Context context) {
        super(context);

        init();
    }

    private void init() {
        linearLayout = new LinearLayout(App.app);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        linearLayout.setp
//        LayoutInflater.from(App.app).inflate()
//        setApapter();
    }

    public void setAdapter(BaseAdapter adapter){
        for(int i=0;i<adapter.getCount();i++){
            linearLayout.addView(adapter.getView(i,null,null));
        }
    }


}
