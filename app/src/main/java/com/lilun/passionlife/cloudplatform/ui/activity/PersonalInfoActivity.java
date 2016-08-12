package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;

import butterknife.Bind;

/**
 * Created by zhangpeng on 2016/7/22.
 */
public class PersonalInfoActivity extends BaseFunctionActivity {


    @Bind(R.id.person_change_photo)
    CircleImageView personChangePhoto;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.belong_dept)
    TextView belongDept;

    @Bind(R.id.belong_role)
    TextView belongRole;


    private View view;

    @Override
    public View setContent() {
        title.setText(getText(R.string.personal_info));
        view = inflater.inflate(R.layout.activity_personal_info, null);
        return view;
    }



}
