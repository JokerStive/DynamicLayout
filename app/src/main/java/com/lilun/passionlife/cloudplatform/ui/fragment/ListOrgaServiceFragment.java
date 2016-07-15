package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaServiceListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ListOrgaServiceFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Service> services;
    private List<OrganizationService> visibleOrgiService;
    private int serviceCount;
    private int userId;
    private OrgaServiceListAdapter adapter;
    private Bundle bundle;

    @Override
    public View setView() {
        //设置编辑框显示
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.d("onCreate");

    }


    @Override
    public void onStart() {
        super.onStart();
//       Logger.d("onStart");
        //从缓存中读取service
        bundle = new Bundle();
        visibleOrgiService = (List<OrganizationService>) ACache.get(App.app).getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService!=null){
            showServices(visibleOrgiService);
        }else{
            Logger.d("showServices  is null");
        }


    }

    public void showServices(List<OrganizationService> data) {
        adapter = new OrgaServiceListAdapter(data,this);
        gvModuleList.setAdapter(adapter);
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddServiceFragment(),mCx.getString(R.string.service_add)));
            }else{
                bundle.putSerializable(Constants.orgaService,visibleOrgiService.get(position-1));
                Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditServiceFragment(), mCx.getString(R.string.service_edit));
                event.setBundle(bundle);
                EventBus.getDefault().post(event);
            }
        });

    }


    /**
    *刷新 “编辑” 显示
    */
    @Subscribe
    public void onEditClick(Event.EditClickEvent event){
        if (adapter!=null){
            setEdText(adapter);
        }
    }


    /**
    *删除一个服务
    */
    @Override
    public void onDeleteClick(int position) {
        if (visibleOrgiService!=null && visibleOrgiService.size()!=0){
            String deleOrgServiceId = visibleOrgiService.get(position).getId();
            Logger.d("deleOrgId = "+deleOrgServiceId);
            rootActivity.addSubscription(ApiFactory.deleteOrganiService(deleOrgServiceId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object integer) {
                    visibleOrgiService.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_orgservice_success));
                    //post一个事件，告诉“首页”刷新数据
                    EventBus.getDefault().post(new Event.deleteOrganiService());
                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootActivity.setIsEditShow(false);
    }

}
