package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;

/**
 * Created by youke on 2016/8/15.
 *
 */
public class IsView extends RelativeLayout {

    private TextView isVew_title;
    private OnIsXChoiseListener listener;
    private ImageView isVew_isX;

    public IsView() {
        super(App.app);
        init();
    }

    private void init() {
        View view = View.inflate(App.app, R.layout.item_is_view, this);
        isVew_isX = (ImageView) view.findViewById(R.id.isView_isX);
        isVew_isX.setEnabled(false);
        RelativeLayout rl_isX = (RelativeLayout) view.findViewById(R.id.rl_isX);
        isVew_title = (TextView) view.findViewById(R.id.isView_title);
        rl_isX.setOnClickListener(v ->{
            isVew_isX.setEnabled(!isVew_isX.isEnabled());
            if (listener!=null){
                listener.onIsXChoise(isVew_isX.isEnabled());
            }
        });
    }


    public void setChoised(boolean choised){
        isVew_isX.setEnabled(choised);
    }

    public void setTitle(String title){
        isVew_title.setText(title);
    }

    public void setOnIsXChoiseListener(OnIsXChoiseListener listener){
        this.listener = listener;
    }

    public interface OnIsXChoiseListener{
        void onIsXChoise(boolean isXChoise);
    }


}
