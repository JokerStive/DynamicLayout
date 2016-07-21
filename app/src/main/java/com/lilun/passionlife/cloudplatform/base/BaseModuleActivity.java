package com.lilun.passionlife.cloudplatform.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.custom_view.SelectPicPopupWindow;
import com.lilun.passionlife.cloudplatform.ui.App;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseModuleActivity extends BaseNetActivity {

    public TextView title;

    CrumbView crumbView;
    private ImageView back;
    FrameLayout content;

    public LayoutInflater inflater;
    public Context mCx;
    public Activity mAx;
    protected TextView edit;
    private String orgiId;
    private SelectPicPopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_module);
        //绑定
        EventBus.getDefault().register(this);

        //控件初始化
        mCx = App.app;
        mAx = this;
        inflater = LayoutInflater.from(mCx);

        title = (TextView) findViewById(R.id.title);
        edit = (TextView) findViewById(R.id.tv_edit);
        edit.setOnClickListener(listen -> {
            //编辑框被点击的事件
//            Logger.d("点击编辑框");
            EventBus.getDefault().post(new Event.EditClickEvent());
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(listener -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });


        content = (FrameLayout) findViewById(R.id.frag_container);
        crumbView = (CrumbView) findViewById(R.id.crumb_view);


        crumbView.setActivity(this);
        create();
    }

    protected void create() {
    }




    /**
     * 设置标题
     */
    public void setTitle(String tit) {
        title.setText(tit);
    }

    /**
     * 设置编辑框是否显示
     */
    public void setIsEditShow(boolean isShow) {
        edit.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setEditText(String s){
        edit.setText(s);
    }

    public String getEditText(){
        return edit.getText().toString();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     *接受token过期事件，跳转登录界面
     */
    public void login(Event.AuthoriseEvent event) {
        TokenManager.translateLogin(mAx);
    }


    /**
     * 替换fragment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openFragment(Event.OpenNewFragmentEvent event) {
        Bundle bundle = event.getBundle();
        openNewFragment(event.newFragment, event.cuumb_title,bundle);
    }





    protected void openNewFragment(BaseFunctionFragment newFragment, String crumbTitle, Bundle bundle) {
        if (bundle!=null){
            newFragment.setArguments(bundle);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setBreadCrumbTitle(crumbTitle);
        ft.replace(R.id.frag_container, newFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        setTitle(crumbTitle);

    }


    public void backStack(){
        getSupportFragmentManager().popBackStack();
    }

    /**
     *响应back键，回退fragment还是activity
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        EventBus.getDefault().unregister(this);
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


    public void choiseHeadPic(View parent){
        popupWindow = new SelectPicPopupWindow(mAx);
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
