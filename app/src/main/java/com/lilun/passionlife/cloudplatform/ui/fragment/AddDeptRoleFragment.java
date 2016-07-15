package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by youke on 2016/6/22.
 */
public class AddDeptRoleFragment extends BaseFunctionFragment {


    @Bind(R.id.head)
    TextView head;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.input_department_name)
    RegItemView inputDepartmentName;

    @Bind(R.id.save)
    Button save;
    @Bind(R.id.input_department_desc)
    RegItemView inputDepartmentDesc;
    private String orgId;
    private Role orgna;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.role_add));
        return inflater.inflate(R.layout.fragment_add_department, null);
    }



    @OnClick(R.id.save)
    void save(){
        String depName = inputDepartmentName.getInput();
        String depDesc= inputDepartmentDesc.getInput();
        if(TextUtils.isEmpty(depName)  || TextUtils.isEmpty(depDesc)){
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.not_orginame_empty));
            return;
        }
        orgna = new Role();
        orgna.setTitle(depName);
        orgna.setDescription(depDesc);
        orgna.setNew(true);

        EventBus.getDefault().post(new Event.addNewRole(orgna));
        rootActivity.backStack();

    }

}
