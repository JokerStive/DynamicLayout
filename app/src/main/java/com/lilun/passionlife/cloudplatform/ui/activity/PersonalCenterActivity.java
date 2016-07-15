package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;

public class PersonalCenterActivity extends BaseFunctionActivity {



    @Override
    public View setContent() {
        title.setText(getString(R.string.person_center_title));
        View view = inflater.inflate(R.layout.activity_personal_center, null);
        return view;
    }
}
