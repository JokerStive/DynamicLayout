package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import comt.sf.youke.dynamiclayout.Activity.Activity.utils.ProgressDialogHelper;
import comt.sf.youke.dynamiclayout.R;

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
            pd = new ProgressDialogHelper().createLoadingDialog(context, "加载中...");
        }
//                pd = new ProgressDialog(context);
//            pd.setCancelable(cancelable);

          /*  if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
*/
            pd.show();
          /*  if (!pd.isShowing()) {
                pd.show();
            }*/

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

    public  Dialog createLoadingDialog(Context context, String msg) {


        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.loading_dialog, null);
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
//        loadingDialog.show();
        return loadingDialog;

    }


}
