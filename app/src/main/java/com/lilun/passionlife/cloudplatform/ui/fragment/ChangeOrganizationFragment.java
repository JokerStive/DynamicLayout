package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseLoadingFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.custom_view.FlowLayout;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.activity.ChangeOrganizationActivity;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by youkr on 2016/7/22.
 */
public class ChangeOrganizationFragment extends BaseLoadingFragment {


    private ChangeOrganizationActivity activity;
    private LayoutInflater inflater;
    private Bundle bundle;
    private String current_id;
    private FlowLayout fl_Container;
    private List<Organization> organizations;
    private String current_orga;


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
//        this.inflater = inflater;
//
//        return  view;
//    }

    @Override
    protected View createSuccessView() {
        View view = inflater.inflate(R.layout.fragment_change_organization, null);
        fl_Container = (FlowLayout) view.findViewById(R.id.container);
        fl_Container.setHorizontalSpacing(UIUtils.dip2px(App.app, 10));
        fl_Container.setVerticalSpacing(UIUtils.dip2px(App.app, 10));
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        inflater = LayoutInflater.from(App.app);
        activity = (ChangeOrganizationActivity) getActivity();
        if (bundle != null) {
            current_id = (String) bundle.get("id");
            current_orga = (String) bundle.get("name");
        }
        getCurrentOrgaChildren();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (organizations!=null){
            setData(organizations);
        }
    }


    @Override
    protected void onErrorPageClick() {
        super.onErrorPageClick();
        getCurrentOrgaChildren();
    }

    private void getCurrentOrgaChildren() {
        if (current_id==null){return;}
        if (loadingPager!=null){
            loadingPager.showLoading();
        }
        activity.setSaveShow(false);
        activity.addSubscription(ApiFactory.getOrgiChildren(current_id), new PgSubscriber<List<Organization>>() {
            @Override
            public void on_Next(List<Organization> oass) {
                for(int i=0;i<oass.size();i++){
                    if (StringUtils.isSpecialOrga(oass.get(i).getId())){
                     oass.remove(i);
                        i--;
                    }
                }
                if (oass.size()==0){
                    showEmpty();
                    return;
                }
                organizations = oass;
                setData(organizations);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });


//        activity.getOrgiChildren(orgaId, orgis -> {
//            organizations = orgis;
//            setData(organizations);
//        });
    }


    /**
     * 设置显示数据
     */
    private void setData(List<Organization> organizations) {
        showSuccess();
        fl_Container.removeAllViews();
        for (Organization orga : organizations) {
            String name = orga.getName();
            String id = orga.getId();
            View view = inflater.inflate(R.layout.item_change_orga_container, null);
            TextView tv_name = (TextView) view.findViewById(R.id.item_name);
            tv_name.setText(name);
            tv_name.setOnClickListener(v -> {
                openNewFragment(name, id);
                activity.setCurrentOrga(name, id);

            });
            fl_Container.addView(view);
        }
        activity.setSaveShow(true);
        activity.setCurrentOrga(current_orga, current_id);
    }

    private void openNewFragment(String name, String id) {
        bundle = new Bundle();
        bundle.putSerializable("id", id);
        bundle.putSerializable("name", name);
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new ChangeOrganizationFragment(), name);
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
