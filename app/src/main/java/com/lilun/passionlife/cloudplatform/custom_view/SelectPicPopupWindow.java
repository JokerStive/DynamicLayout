package com.lilun.passionlife.cloudplatform.custom_view;

import android.app.Activity;
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
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.io.File;


/**
 * Created by youke on 2016/5/31.
 */
public class SelectPicPopupWindow  extends PopupWindow implements OnClickListener{
    private FrameLayout btn_take_photo, btn_pick_photo;
    private View mMenuView;
    private Activity context;

    public Uri tempUri;

    public SelectPicPopupWindow(Activity context) {
        super(context);
        this.context=context;
        mMenuView = LayoutInflater.from(App.app).inflate(R.layout.popup_carmera, null);
        btn_take_photo = (FrameLayout) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (FrameLayout) mMenuView.findViewById(R.id.btn_pick_photo);
        //取消按钮
        btn_take_photo.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
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
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                context.startActivityForResult(openCameraIntent, PicloadManager.TAKE_PICTURE);
                dismiss();
                break;
            case R.id.btn_pick_photo:
                //相册
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                context.startActivityForResult(openAlbumIntent, PicloadManager.CHOOSE_PICTURE);
                dismiss();
                break;

        }
    }
}
