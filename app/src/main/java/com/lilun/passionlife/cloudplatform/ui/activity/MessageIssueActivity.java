package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;

import butterknife.Bind;

public class MessageIssueActivity extends BaseFunctionActivity {


    @Bind(R.id.tv_title)
    EditText tvTitle;

    @Bind(R.id.et_content)
    EditText etContent;

    @Bind(R.id.im_add_pic)
    ImageView imAddPic;

    @Bind(R.id.ll_list_pic)
    LinearLayout llListPic;

    @Bind(R.id.im_allow_review)
    ImageView imAllowReview;

    @Bind(R.id.btn_message_publish)
    Button btnMessagePublish;

    @Override
    public View setContent() {
        return inflater.inflate(R.layout.activity_message_issue, null);
    }


}
