package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.PullChoiseView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class EditServiceFragment extends BaseFunctionFragment implements PullChoiseView.OnItemChooseListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.tv_hint)
    ImageView tv_hint;

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
    private OrganizationService service;
    private List<Service> allService;
    private String serviceId;
    private List<String> wigthList;
    private String orgaServiceId;

    @OnClick(R.id.ll_hint)
    void hint() {
        tv_hint.setEnabled(!tv_hint.isEnabled());
    }

    private String crumb_title;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    public View setView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            service = (OrganizationService) bundle.get(Constants.orgaService);

        }
        crumb_title = mCx.getString(R.string.service_edit);
        View view = inflater.inflate(R.layout.fragment_add_servcice, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
//        Logger.d("on star");
        setInitData();
        getData();
        setSettingData();
    }


    private void setSettingData() {
        wigthList = new ArrayList<>();
        wigthList.add("1");
        wigthList.add("2");
        inputServiceWigth.setShow_data(service.getSettings()==null?"1":service.getSettings().getWigth());
        inputServiceHeight.setShow_data("1");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCx, R.layout.item_change_belong_orga, wigthList);
        inputServiceWigth.init(position -> {
            inputServiceWigth.setShow_data(wigthList.get(position));
        }, adapter);
    }

    private void setInitData() {
        Picasso.with(App.app).load(PicloadManager.orgaServiceIconUrl(service.getId()))
                .error(R.drawable.head_portrait)
                .into(ivHead);

        inputServiceName.setInput(service.getTitle()==null?"":service.getTitle());
        inputServiceDetail.setInput(service.getDescription()==null?"":service.getDescription());
        inputService.setShow_data(StringUtils.getServiceName(service.getServiceId()));
        if (service.getSettings()==null){
            tv_hint.setEnabled(false);
        }else if (service.getSettings().getVisible()==null){
            tv_hint.setEnabled(false);
        }else {
            tv_hint.setEnabled(!Boolean.parseBoolean(service.getSettings().getVisible()));
        }
//        tv_hint.setEnabled(service.getSettings()== null && service.getSettings().getVisible() != null && !Boolean.parseBoolean(service.getSettings().getVisible()));

        serviceId = service.getServiceId();
        orgaServiceId = service.getId();
    }

    private void getData() {
        rootActivity.addSubscription(ApiFactory.getServicess(), new PgSubscriber<List<Service>>(rootActivity) {
            @Override
            public void on_Next(List<Service> servicess) {
                if (servicess.size() == 0) {
                    ToastHelper.get(App.app).showShort("服务流为空");
                    return;
                }
                allService = servicess;
                list.clear();
                for (Service service : servicess) {
                    list.add(service.getTitle());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCx, R.layout.item_change_belong_orga, list);
                inputService.init(EditServiceFragment.this, adapter);
            }

        });
    }

    @OnClick(R.id.save)
    void save() {
        //构造一个服务
        String serviceName = inputServiceName.getInput();
        String serviceDesc = inputServiceDetail.getInput();

        if (checkData(serviceName, serviceDesc) && orgiId!=null) {
            OrganizationService.SettingsBean setting = new OrganizationService.SettingsBean();
            OrganizationService service = new OrganizationService();

//            service.setId(orgiId+":"+serviceName);
            service.setTitle(serviceName);
            service.setServiceId(serviceId);
//            service.setOrganizationId(orgiId);
            service.setDescription(serviceDesc);


            setting.setVisible(!tv_hint.isEnabled() + "");
            service.setSettings(setting);

            rootActivity.addSubscription(ApiFactory.putOrgaService(orgaServiceId, service), new PgSubscriber<OrganizationService>(rootActivity) {
                @Override
                public void on_Next(OrganizationService services) {
                    EventBus.getDefault().post(new Event.putService(services));
                    EventBus.getDefault().post(new Event.reflashServiceList());
                    rootActivity.backStack();
                }
            });
        }
    }


    private boolean checkData(String serviceName, String serviceDesc) {
        if (TextUtils.isEmpty(serviceName) || TextUtils.isEmpty(serviceDesc)) {
            ToastHelper.get(mCx).showShort("输入不能为空");
            return false;
        }
        return true;
    }


    @Override
    public void OnItemChoose(int position) {
        serviceId = allService.get(position).getId();
        inputService.setShow_data(list.get(position));
    }
}
