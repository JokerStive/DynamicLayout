package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.custom_view.FlowLayout;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.activity.ChangeOrganizationActivity;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by youkr on 2016/7/22.
 */
public class ChangeOrganizationFragment  extends StatedFragment {

    private String name;
    private String id;
    private ChangeOrganizationActivity activity;
    private LayoutInflater inflater;
    private Bundle bundle;
    private String current_id;
    private FlowLayout fl_Container;
    private boolean restroe;
    private List<Organization> organizations;
    private String current_orga;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_change_organization, null);
        fl_Container = (FlowLayout) view.findViewById(R.id.container);
        fl_Container.setHorizontalSpacing(UIUtils.dip2px(App.app,10));
        fl_Container.setVerticalSpacing(UIUtils.dip2px(App.app,10));
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (ChangeOrganizationActivity)getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            current_id = (String) bundle.get("id");
            current_orga = (String) bundle.get("name");
        }
        if (!restroe){
            if (!TextUtils.isEmpty(current_id)){
                getCurrentOrgaChildren(current_id);
            }
        }
    }

    private void getCurrentOrgaChildren(String orgaId) {
        activity.setSaveShow(false);
        activity.getOrgiChildren(orgaId, orgis -> {
            organizations = orgis;
            setData(organizations);
        });
    }


    /**
    *设置显示数据
    */
    private void setData(List<Organization> organizations) {
        fl_Container.removeAllViews();
        for (Organization orga:organizations){
            String name = orga.getName();
            String id = orga.getId();
            if (StringUtils.isSpecialOrga(id)){continue;}
            View view = inflater.inflate(R.layout.item_change_orga_container, null);
            TextView tv_name = (TextView) view.findViewById(R.id.item_name);
            tv_name.setText(name);
            tv_name.setOnClickListener(v -> {
                openNewFragment(name,id);
//                current_orga = name;
                activity.setCurrentOrga(name,id);

            });

            fl_Container.addView(view);
        }
        activity.setSaveShow(true);
        activity.setCurrentOrga(current_orga,current_id);
    }

    private void openNewFragment(String name, String id) {
        bundle=new Bundle();
        bundle.putSerializable("id",id);
        bundle.putSerializable("name",name);
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new ChangeOrganizationFragment(), name);
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }





    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        restroe=true;
        if(organizations!=null){
            setData(organizations);
        }
    }
}
