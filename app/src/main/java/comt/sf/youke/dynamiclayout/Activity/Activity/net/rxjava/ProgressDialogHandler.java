package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.View;


import comt.sf.youke.dynamiclayout.Activity.Activity.custom_view.ProgressDialog;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ProgressDialogHandler  extends android.os.Handler{
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog pd;


    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private View v;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){

        if (pd == null) {
            pd = new ProgressDialog(context,"加载中...");
        }
        pd.setOnCancelListener(dialog -> {
            dismissProgressDialog();
        });
        pd.show();


    }

    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            mProgressCancelListener.onCancelProgress();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }




}
