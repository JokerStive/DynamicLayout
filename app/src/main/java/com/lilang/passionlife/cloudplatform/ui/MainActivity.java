package com.lilang.passionlife.cloudplatform.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lilang.passionlife.R;
import com.lilang.passionlife.cloudplatform.base.BaseActivity;
import com.lilang.passionlife.cloudplatform.bean.Account;
import com.lilang.passionlife.cloudplatform.custom_view.SelectPicPopupWindow;
import com.lilang.passionlife.cloudplatform.net.User.UserApi;
import com.lilang.passionlife.cloudplatform.net.rxjava.OnNext;
import com.lilang.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilang.passionlife.cloudplatform.utils.LogUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String TAG="OkHttp";

    @Bind(R.id.btn_dynamic)
    Button btnDynamic;
    @Bind(R.id.tv_content)
    TextView tvContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        EventBus.getDefault().register(this);
    }



    public void next(View v) {
//        SelectPicPopupWindow sp =  new SelectPicPopupWindow(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        sp.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
//       new SVProgressHUD(this).show();

        subscription.add(new UserApi()
                .getAllUser()
                .subscribe(new PgSubscriber<>(new OnNext<List<Account>>() {
                    @Override
                    public void onNext(List<Account> accounts) {
                        tvContent.setText(accounts.size() + "------------");
                        LogUtils.D("OkHttp","success");
                    }
                }, MainActivity.this)));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri uri = data.getData();
            switch (requestCode){
                case SelectPicPopupWindow.PICK_FROM_CAMERA:
                    //从相机获取uri
                    LogUtils.D("file",uri.toString());
                    CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                    break;
                case SelectPicPopupWindow.PICK_FROM_FILE:
                    //从相册获取uri
                    LogUtils.D("file",uri.toString());
                    CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                    break;
            }
        }
    }
}