package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by youke on 2016/6/22.
 */
public class AddDepartmentFragment extends BaseFunctionFragment {


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
    private Organization orgna;
    private View view;
    private Bitmap icon;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.add_department));
        view = inflater.inflate(R.layout.fragment_add_department, null);
        return view;
    }

    @OnClick(R.id.iv_head)
    void choiseHead() {
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }


    @Subscribe
    public void choisHead(Event.choiseHeadPic event) {
        Bitmap bitmap = event.getBitmap();
        if (bitmap != null) {
            StringUtils.getBytesFromBitmap(bitmap);
            icon = bitmap;
            ivHead.setImageBitmap(bitmap);

        }
    }



    @OnClick(R.id.save)
    void save(){
        String depName = inputDepartmentName.getInput();
        String depDesc= inputDepartmentDesc.getInput();
        if(TextUtils.isEmpty(depName)  || TextUtils.isEmpty(depDesc)){
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.not_orginame_empty));
            return;
        }
        orgna = new Organization();
        orgna.setName(depName);
        orgna.setDescription(depDesc);
        orgna.setNew(true);
//        orgna.setId(orgiId + Constants.special_orgi_department + "/" + depName);
//        orgna.setParentId(orgiId + Constants.special_orgi_department);

        EventBus.getDefault().post(new Event.addNewDepartment(orgna,icon==null?null:icon));
        rootActivity.backStack();

    }

}
