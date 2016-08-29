package com.lilun.passionlife.cloudplatform.net.rxjava;

import android.content.Context;

import com.lilun.passionlife.cloudplatform.custom_view.ProgressDialog;
import com.lilun.passionlife.cloudplatform.utils.DealErrorUtils;
import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Created by youke on 2016/5/27.
 * 数据统一处理，只暴露onNext()的subscribe
 */
public abstract class PgSubscriber<T> extends Subscriber<T>  {

    private Context context;
    private ProgressDialog pd;


    public PgSubscriber(Context context) {
        this.context = context;
    }

    public PgSubscriber() {
    }

    public abstract void on_Next(T t);
    public  void on_Error(){};




    @Override
    public void onStart() {

        if (context!=null){
            initProgressDialog();
        }

    }

    @Override
    public void onCompleted()
    {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        try {
            dismissProgressDialog();
            DealErrorUtils.dealError(e);
            on_Error();
        }catch (Throwable e1) {
//            ToastHelper.get().showShort(App.app.getString(R.string.net_time_out));
            Logger.d(e1.toString());
        }

    }

    @Override
    public void onNext(T t) {
        dismissProgressDialog();
        on_Next(t);
    }



    private void initProgressDialog(){
        if (pd == null) {
            pd = new ProgressDialog(context,"加载中...");
            pd.setCancelable(false);
        }
        pd.showDialog();


    }

    public void dismissProgressDialog(){
        if (pd != null) {
            pd.dissmissDialog();
            pd = null;
        }
    }
}
