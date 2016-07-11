package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.PullChoiseView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddServiceFragment extends BaseFunctionFragment implements PullChoiseView.OnItemChooseListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.tv_hint)
    TextView tv_hint;

    @Bind(R.id.input_service)
    PullChoiseView inputService;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_service_name)
    RegItemView inputServiceName;

    @Bind(R.id.input_service_detail)
    RegItemView inputServiceDetail;

    @Bind(R.id.input_service_height)
    PullChoiseView inputServiceHeight;

    @Bind(R.id.input_service_wigth)
    PullChoiseView inputServiceWigth;

    @Bind(R.id.ll_hint)
    LinearLayout llHint;

    @Bind(R.id.save)
    Button save;
    private String serviceNamee;

    @OnClick(R.id.ll_hint)
    void hint() {
        tv_hint.setEnabled(!tv_hint.isEnabled());
    }

    private List<Service> allService;

    private String crumb_title;
    private String serviceId;
    private String wigth;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> wigthList = new ArrayList<String>();

    @Override
    public View setView() {
        Bundle bundle = getArguments();
        crumb_title = bundle==null ? mCx.getString(R.string.service_add): mCx.getString(R.string.service_edit);
        View view = inflater.inflate(R.layout.fragment_add_servcice, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        setSettingData();
        getServiceData();
    }

    private void setSettingData() {
        wigth="1";
        wigthList.add("1");
        wigthList.add("2");
        inputServiceWigth.setShow_data("1");
        inputServiceHeight.setShow_data("1");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCx, android.R.layout.simple_list_item_1, wigthList);
        inputServiceWigth.init(position -> {
            inputServiceWigth.setShow_data(wigthList.get(position));
            wigth = wigthList.get(position);
        }, adapter);
    }

    private void getServiceData() {
        rootActivity.addSubscription(ApiFactory.getServicess(), new PgSubscriber<List<Service>>(rootActivity) {
            @Override
            public void on_Next(List<Service> servicess) {
                if (servicess.size()==0){ToastHelper.get(App.app).showShort("服务流为空");return;}
                allService = servicess;
                for(Service service :servicess){
                    list.add(service.getTitle());
                }
                Logger.d("list size " +list.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCx, android.R.layout.simple_list_item_1, list);
                inputService.init(AddServiceFragment.this, adapter);
            }

        });
    }

    @Override
    public void OnItemChoose(int position) {
        inputService.setShow_data(list.get(position));
        serviceId = allService.get(position).getId();
        serviceNamee = allService.get(position).getTitle();
    }

    @OnClick(R.id.save)
    void save() {
        //构造一个服务
        String serviceName = inputServiceName.getInput();
        String serviceDesc = inputServiceDetail.getInput();

        if (checkData(serviceName,serviceDesc) && !TextUtils.isEmpty(orgiId)){
            OrganizationService.SettingsBean setting  = new OrganizationService.SettingsBean();
            OrganizationService service = new OrganizationService();

            service.setTitle(serviceName);
            service.setId(orgiId+":"+serviceName);
            service.setServiceId(serviceId);
            service.setServiceName(serviceNamee);
            service.setOrganizationId(orgiId+ Constants.special_orgi_service);
            Logger.d("service parent id  = " +orgiId+ Constants.special_orgi_service);
            service.setDescription(serviceDesc);



            setting.setVisible(!tv_hint.isEnabled()+"");
            setting.setWigth(wigth);
            service.setSettings(setting);

            rootActivity.addSubscription(ApiFactory.postOrgiServices(service), new PgSubscriber<OrganizationService>(rootActivity) {
                @Override
                public void on_Next(OrganizationService  services) {
                    if (Boolean.parseBoolean(setting.getVisible())){
                        Logger.d("post a service success");
                        EventBus.getDefault().post(new Event.postService(services));
                    }
                    rootActivity.backStack();
                }
            });
        }
    }

    private boolean checkData(String serviceName, String serviceDesc) {
        if (TextUtils.isEmpty(serviceName)  || TextUtils.isEmpty(serviceDesc)){
            ToastHelper.get(mCx).showShort("输入不能为空");
            return false;
        }
        return true;
    }


}