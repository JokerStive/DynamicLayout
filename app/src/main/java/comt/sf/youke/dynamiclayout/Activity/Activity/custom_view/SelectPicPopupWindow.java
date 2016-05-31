package comt.sf.youke.dynamiclayout.Activity.Activity.custom_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import java.io.File;

import comt.sf.youke.dynamiclayout.R;

/**
 * Created by Administrator on 2016/5/31.
 */
public class SelectPicPopupWindow  extends PopupWindow implements OnClickListener{
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private View mMenuView;
    private Activity context;
    public static  final int   PICK_FROM_FILE=0;
    public static  final int   PICK_FROM_CAMERA=1;

    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_carmera, null);
        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_take_photo:
                //相机
                 intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "avatar_"+ String.valueOf(System.currentTimeMillis())+ ".png"));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                context.startActivityForResult(intent, PICK_FROM_CAMERA);
                dismiss();
                break;
            case R.id.btn_pick_photo:
                //相册
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image");
                context.startActivityForResult(intent, PICK_FROM_FILE);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

        }
    }
}
