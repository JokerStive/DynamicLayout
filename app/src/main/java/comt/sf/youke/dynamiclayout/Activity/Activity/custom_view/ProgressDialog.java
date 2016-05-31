package comt.sf.youke.dynamiclayout.Activity.Activity.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import comt.sf.youke.dynamiclayout.R;

/**
 * Created by Administrator on 2016/5/31.
 */
public class ProgressDialog extends Dialog {

    private  String msg;
    private Context mContext;
    private static int mTheme = R.style.loading_dialog;

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ProgressDialog(Context context,String msg) {
        super(context, mTheme);
        this.mContext = context;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

        ImageView spaceshipImage = (ImageView)findViewById(R.id.img);
        TextView tipTextView = (TextView)findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
    }
}
