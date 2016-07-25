package com.lilun.passionlife.cloudplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.SelectPicPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangpeng on 2016/7/22.
 */
public class PersonalInfoActivity extends BaseFunctionActivity {
    View view;
    private SelectPicPopupWindow popupWindow;
    @Bind(R.id.person_change_photo)
    CircleImageView personPhoto;
    @OnClick(R.id.personal_img)
    public void choicePhoto(){
       choiseHeadPic(view);
    }

    @Override
    public View setContent() {
        title.setText(getText(R.string.personal_info));
        view = inflater.inflate(R.layout.activity_personal_info, null);
        return view;
    }

//    =====================================================================================================================
//    选择头像

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case PicloadManager.TAKE_PICTURE:
                    startPhotoZoom(popupWindow.tempUri); // 开始对图片进行裁剪处理
                    break;
                case PicloadManager.CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case PicloadManager.CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }


    @Subscribe
    public void choisHead(Event.choiseHeadPic event){
        Bitmap bitmap = event.getBitmap();
        if (bitmap!=null){
            personPhoto.setImageBitmap(bitmap);
        }
    }


    public void choiseHeadPic(View parent){
        popupWindow = new SelectPicPopupWindow(mAc);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
        }
        popupWindow.tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PicloadManager.CROP_SMALL_PICTURE);
    }


    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            EventBus.getDefault().post(new Event.choiseHeadPic(photo));
        }
    }

}
