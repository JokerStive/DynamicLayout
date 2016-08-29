package com.lilun.passionlife.cloudplatform.custom_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lilun.passionlife.R;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by Administrator on 2016/5/31.
 */
public class ProgressDialog extends Dialog {

    private  String msg;
    private Context mContext;
    private static int mTheme = R.style.loading_dialog;
    private AVLoadingIndicatorView avi_loading;

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ProgressDialog(Context context,String msg) {
        super(context, mTheme);
        this.mContext = context;
        this.msg = msg;
    }

    public ProgressDialog(Activity activity) {
        super(activity, mTheme);
        this.mContext = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

        avi_loading = (AVLoadingIndicatorView) findViewById(R.id.avi_loading);
    }


    public void showDialog(){
        show();
        Logger.d("pbSubscrible start anim");
        avi_loading.show();
    }

    public void dissmissDialog(){
        dismiss();
        Logger.d("pbSubscrible stop anim");
        avi_loading.hide();
    }
}
