package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.content.Context;
import android.widget.Toast;

import comt.sf.youke.dynamiclayout.Activity.Activity.exception.ApiException;
import comt.sf.youke.dynamiclayout.Activity.Activity.exception.NetException;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.User.DealErrorUtils;
import comt.sf.youke.dynamiclayout.Activity.Activity.utils.NetUtil;
import comt.sf.youke.dynamiclayout.Activity.Activity.utils.ToastHelper;
import comt.sf.youke.dynamiclayout.R;
import rx.Subscriber;

/**
 * Created by youke on 2016/5/27.
 * 数据统一处理，只暴露onNext()的subscribe
 */
public class PgSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private OnNext mOnNext;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public PgSubscriber(OnNext mOnNext, Context context) {
        this.mOnNext = mOnNext;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }



    @Override
    public void onStart() {

        showProgressDialog();
       /* if(!NetUtil.checkNet(context)){
            try {
                throw new NetException();
            }  catch (NetException e) {
//                e.printStackTrace();
            }
        }else{
            showProgressDialog();
        }*/
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        DealErrorUtils.dealError(e,context);


    }

    @Override
    public void onNext(T t) {
        dismissProgressDialog();
        mOnNext.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
