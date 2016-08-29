package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class MessageIssueActivity extends BaseFunctionActivity {


    @Bind(R.id.tv_title)
    EditText etTitle;

    @Bind(R.id.et_content)
    EditText etContent;

    @Bind(R.id.im_add_pic)
    ImageView imAddPic;

    @Bind(R.id.ll_list_pic)
    LinearLayout llListPic;

    @Bind(R.id.im_allow_review)
    ImageView imAllowReview;

    @Bind(R.id.im_is_cat)
    ImageView imIsCat;

    @Bind(R.id.btn_message_publish)
    Button btnMessagePublish;

    private String setCategoryPermission = "Model.setCategory";
//    private int parentId;

    @Override
    public void onCreate() {
        super.onCreate();
        imAllowReview.setEnabled(false);
        imIsCat.setEnabled(false);
//        parentId = getIntent().getIntExtra("parentId", -1);
    }

    @Override
    public View setContent() {
        return inflater.inflate(R.layout.activity_message_issue, null);
    }


    @OnClick(R.id.rl_allow_review)
    void setAllReview(){
        imAllowReview.setEnabled(!imAllowReview.isEnabled());
    }

    @OnClick(R.id.rl_is_cat)
    void setIsaCat(){
        if (isAdmin()){
            imIsCat.setEnabled(!imIsCat.isEnabled());
            return;
        }
        checkHasPermission(userId, setCategoryPermission, hasPermission -> {
            if (hasPermission){
                imIsCat.setEnabled(!imIsCat.isEnabled());
            }else{
                ToastHelper.get().showShort(StringUtils.getString(R.string.no_permission));
            }
        });
    }


    @OnClick(R.id.btn_message_publish)
    void issueMessage(){
        if (checkEmptyData()){
            OrganizationInformation oi = newOassInfo();
            postMessage(oi);
        }
    }

    private void postMessage(OrganizationInformation oi) {
        addSubscription(ApiFactory.postOassInfo(oi), new PgSubscriber<OrganizationInformation>(this) {
            @Override
            public void on_Next(OrganizationInformation info) {
                //TODO 信息提交成功
                EventBus.getDefault().post(new Event.reflashInfo(info));
                finish();
            }

        });
    }


    private OrganizationInformation newOassInfo() {
        OrganizationInformation oi = new OrganizationInformation();
        oi.setTitle(etTitle.getText().toString());
        oi.setContext(etContent.getText().toString());
        oi.setReplyable(imAllowReview.isEnabled());
        oi.setIsCat(imIsCat.isEnabled());
        oi.setOrganizationId(StringUtils.getCheckedOrgaId(orgaId)+ Constants.special_orgi_info);
        oi.setParentId(App.parentId);
        oi.setCreatorId(User.getUsername());
        oi.setUpdatorId(User.getUsername());
        return oi;
    }

    /**
    *检查是否有空数据
    */
    private boolean checkEmptyData() {
        if (TextUtils.isEmpty(etTitle.getText().toString())){
            ToastHelper.get().showShort(StringUtils.getString(R.string.message_title_et));
            return false;
        }

        if (!imIsCat.isEnabled()  && TextUtils.isEmpty(etContent.getText().toString())){
            ToastHelper.get().showShort(StringUtils.getString(R.string.message_content_et));
            return false;
        }
        return true;
    }
}
