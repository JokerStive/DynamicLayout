package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.graphics.Bitmap;
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
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.InputView;
import com.lilun.passionlife.cloudplatform.custom_view.PullChoiseView;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.EnumX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.InputX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.IsX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data.EncapSettingDataEnger;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data.ParserSettingsDataEnger;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.RxBus;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * Created by youke on 2016/6/22.
 */
public class AddServiceFragment extends BaseFunctionFragment implements PullChoiseView.OnItemChooseListener {


    @Bind(R.id.head)
    TextView head;
//
//    @Bind(R.id.tv_hint)
//    ImageView tv_hint;

    @Bind(R.id.input_service)
    PullChoiseView inputService;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_service_name)
    InputView inputServiceName;

    @Bind(R.id.input_service_detail)
    InputView inputServiceDetail;

    //    @Bind(R.id.input_service_height)
//    PullChoiseView inputServiceHeight;
//
//    @Bind(R.id.input_service_wigth)
//    PullChoiseView inputServiceWigth;
//
    @Bind(R.id.service_settings)
    LinearLayout serviceSettings;


    private String serviceNamee;
    private View view;
    private Bitmap icon;
    int padding = 5;



    private List<Service> allService;

    private String crumb_title;
    private String serviceId;
    private ArrayList<String> list = new ArrayList<String>();
    private List<IsX> isXs;
    private List<InputX> inputXs;
    private List<EnumX> menuXs;

    @Override
    public View setView() {
        crumb_title = App.app.getString(R.string.service_add);
        view = inflater.inflate(R.layout.fragment_add_servcice, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(mCx.getString(R.string.service_add));
        getServiceData();
    }

    @OnClick(R.id.iv_head)
    void headPic() {
        Logger.d(" 默认组织id = " + orgiId);
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event) {
        Bitmap bitmap = event.getBitmap();
        if (bitmap != null) {
            icon = bitmap;
            ivHead.setImageBitmap(bitmap);

        }
    }



    private void getServiceData() {
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
                inputService.init(AddServiceFragment.this, adapter);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void OnItemChoose(int position) {
        inputService.setShow_data(list.get(position));
        serviceId = allService.get(position).getId();
        serviceNamee = allService.get(position).getTitle();


        //先清除所有配置项
        serviceSettings.removeAllViews();
        //显示该服务的可配置项
        if (allService.get(position).getSettings() != null) {
            showServiceSettings((Map) allService.get(position).getSettings());
        }
    }

    /**
     * 显示服务的可配置项
     */
    private void showServiceSettings(Map settings) {
        ParserSettingsDataEnger enger = new ParserSettingsDataEnger(settings);
        isXs = enger.getIsXs();
        inputXs = enger.getInputXs();
        menuXs = enger.getMenuXs();


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


    /**
     * 新增一个irgaService
     */
    @Override
    protected void save() {
        String serviceName = inputServiceName.getInput();
        String serviceDesc = inputServiceDetail.getInput();

        if (checkData(serviceName, serviceDesc)) {
//            OrganizationService.SettingsBean setting = new OrganizationService.SettingsBean();
            EncapSettingDataEnger enger = new EncapSettingDataEnger();
            Map<String, String> setting = enger.encapData(isXs, inputXs, menuXs);
            OrganizationService service = new OrganizationService();

            service.setSettings(setting);
            service.setTitle(serviceName);
            service.setId(orgiId + ":" + serviceName);
            service.setServiceId(serviceId);
            service.setServiceName(serviceNamee);

            service.setOrganizationId(StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_service);

            Logger.d("service parent id  = " + orgiId + Constants.special_orgi_service);
            service.setDescription(serviceDesc);




            rootActivity.addSubscription(ApiFactory.postOrgiServices(service), new PgSubscriber<OrganizationService>(rootActivity) {
                @Override
                public void on_Next(OrganizationService services) {
                    ToastHelper.get(mCx).showShort(App.app.getString(R.string.add_orgservice_success));
                    String isConfig = setting.get(KnownServices.ISCONFIG_Service);
                    if (Boolean.parseBoolean(isConfig) || TextUtils.isEmpty(isConfig)){
                        RxBus.getDefault().send(new RxbusEvent.addModule(service));
//                        EventBus.getDefault().post(new RxbusEvent.postService(service));
                    }
                    EventBus.getDefault().post(new Event.reflashServiceList(services));
                    saveIcon(service.getId());
                    rootActivity.backStack();
                }
            });
        }
    }

    /**
     * 上传icon
     */
    private void saveIcon(String orgaServiceId) {
        if (icon == null) {
            return;
        }
        RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
        rootActivity.addSubscription(ApiFactory.postOrgaServiceIcon(orgaServiceId, requestBody), new PgSubscriber() {
            @Override
            public void on_Next(Object o) {
                ToastHelper.get(mCx).showShort(App.app.getString(R.string.add_orgservice_icon_success));
            }

            @Override
            public void on_Error() {
                super.on_Error();
                ToastHelper.get(mCx).showShort(App.app.getString(R.string.add_orgservice_icon_false));
            }
        });

    }

    private boolean checkData(String serviceName, String serviceDesc) {
        if (TextUtils.isEmpty(serviceName) || TextUtils.isEmpty(serviceDesc)) {
            ToastHelper.get(mCx).showShort("输入不能为空");
            return false;
        }
        return true;
    }


}
