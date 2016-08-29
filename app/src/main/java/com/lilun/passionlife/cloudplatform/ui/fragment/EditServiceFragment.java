package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.RxbusEvent;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.InputView;
import com.lilun.passionlife.cloudplatform.custom_view.PullChoiseView;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data.EncapSettingDataEnger;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data.InitSettingsDataEnger;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.InputX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.IsX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.EnumX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data.ParserSettingsDataEnger;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.RxBus;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/22.
 */
public class EditServiceFragment extends BaseFunctionFragment implements PullChoiseView.OnItemChooseListener {


    @Bind(R.id.head)
    TextView head;



    @Bind(R.id.input_service)
    PullChoiseView inputService;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_service_name)
    InputView inputServiceName;

    @Bind(R.id.input_service_detail)
    InputView inputServiceDetail;

    @Bind(R.id.service_settings)
    LinearLayout serviceSettings;



    private OrganizationService os;
    private List<Service> allService;
    private String serviceId;
    private String orgaServiceId;
    int padding = 5;



    private String crumb_title;
    private ArrayList<String> list = new ArrayList<String>();
    private List<IsX> isXs;
    private List<InputX> inputXs;
    private List<EnumX> menuXs;

    @Override
    public View setView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            os = (OrganizationService) bundle.get(Constants.orgaService);

        }
        crumb_title = mCx.getString(R.string.service_edit);
        View view = inflater.inflate(R.layout.fragment_add_servcice, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        setInitData();
        getData();
    }




    private void setInitData() {
        Picasso.with(App.app).load(PicloadManager.orgaServiceIconUrl(os.getId()))
                .error(R.drawable.head_portrait)
                .into(ivHead);

        inputServiceName.setInput(os.getTitle()==null?"": os.getTitle());
        inputServiceDetail.setInput(os.getDescription()==null?"": os.getDescription());
        inputService.setShow_data(StringUtils.getServiceName(os.getServiceId()));

        serviceId = os.getServiceId();
        orgaServiceId = os.getId();
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
                showInitSetting(allService);
                list.clear();
                for (Service service : servicess) {
                    list.add(service.getTitle());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mCx, R.layout.item_change_belong_orga, list);
                inputService.init(EditServiceFragment.this, adapter);
            }

        });
    }

    /**
    *显示初始化配置项
    */
    private void showInitSetting(List<Service> allService) {
        if (serviceId!=null && allService!=null){
            for(Service service:allService){
                if (serviceId.equals(service.getId())){
                    InitSettingsDataEnger enger = new InitSettingsDataEnger((Map<String, Map<String, Object>>) service.getSettings(), (Map<String, String>) os.getSettings());
                    isXs = enger.getIsXs();
                    inputXs = enger.getInputXs();
                    menuXs = enger.getMenuXs();
                    showSettings(inputXs,menuXs,isXs);
                }
            }
        }
    }


    /**
    *显示配置项
    */
    private void showSettings(List<InputX> inputXs, List<EnumX> menuXs, List<IsX> isXs) {
        if (inputXs != null) {
            LinearLayout linearLayout = newLineaLayout(serviceSettings.getChildCount() != 0);
            for (InputX inputX : inputXs) {
                linearLayout.addView(inputX.getInputView());
            }

            serviceSettings.addView(linearLayout);
        }


        if (menuXs != null) {
            LinearLayout linearLayout = newLineaLayout(serviceSettings.getChildCount() != 0);
            for (EnumX menuX : menuXs) {
                linearLayout.addView(menuX.getEnumView());
            }

            serviceSettings.addView(linearLayout);
        }

        if (isXs != null) {
            LinearLayout linearLayout = newLineaLayout(serviceSettings.getChildCount() != 0);
            for (IsX isX : isXs) {
                linearLayout.addView(isX.getIsView());
            }

            serviceSettings.addView(linearLayout);
        }


    }

    private LinearLayout newLineaLayout(boolean margin) {

        LinearLayout layout = new LinearLayout(App.app);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (margin) {
            params.setMargins(0, UIUtils.dip2px(App.app, 10), 0, 0);
        }
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(UIUtils.dip2px(App.app, padding), UIUtils.dip2px(App.app, padding), 0, 0);
        layout.setBackgroundResource(R.drawable.shape_green);
        return layout;
    }



    @Override
    public void OnItemChoose(int position) {
        serviceId = allService.get(position).getId();
        inputService.setShow_data(list.get(position));

        //先清除所有配置项
        serviceSettings.removeAllViews();
        //显示该服务的可配置项
        if (allService.get(position).getSettings() != null) {
            Map settings = (Map) allService.get(position).getSettings();
            ParserSettingsDataEnger enger = new ParserSettingsDataEnger(settings);
            isXs = enger.getIsXs();
            inputXs = enger.getInputXs();
            menuXs = enger.getMenuXs();
            showSettings(enger.getInputXs(),enger.getMenuXs(),enger.getIsXs());
        }
    }


    @Override
    protected void save() {
        //构造一个服务
        String serviceName = inputServiceName.getInput();
        String serviceDesc = inputServiceDetail.getInput();

        if (checkData(serviceName, serviceDesc) && orgiId!=null) {
            EncapSettingDataEnger enger = new EncapSettingDataEnger();
            Map<String, String> setting = enger.encapData(isXs, inputXs, menuXs);
            OrganizationService service = new OrganizationService();

            service.setSettings(setting);
            service.setTitle(serviceName);
            service.setServiceId(serviceId);
            service.setDescription(serviceDesc);




            rootActivity.addSubscription(ApiFactory.putOrgaService(orgaServiceId, service), new PgSubscriber<OrganizationService>(rootActivity) {
                @Override
                public void on_Next(OrganizationService services) {
//                    EventBus.getDefault().post(new Event.putService(services));
                    RxBus.getDefault().send(new RxbusEvent.editdModule(service));
                    EventBus.getDefault().post(new Event.reflashServiceList(services));
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




}
