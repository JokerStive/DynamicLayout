package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.content.Context;
import android.widget.Toast;

import comt.sf.youke.dynamiclayout.Activity.Activity.utils.ToastHelper;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private OnNext mOnNext;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public ProgressSubscriber(OnNext mOnNext, Context context) {
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
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
//        ToastHelper.get(context).showShort("");
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        ToastHelper.get(context).showShort(e.getMessage());
//        Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
