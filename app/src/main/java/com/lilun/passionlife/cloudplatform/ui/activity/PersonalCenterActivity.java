package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.BelongOrgasAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.custom_view.XListView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PersonalCenterActivity extends BaseActivity {


    @Bind(R.id.belong_orga)
    TextView belongOrga;

    @Bind(R.id.rl_belong)
    RelativeLayout rlBelong;


    @Bind(R.id.pop_contain)
    XListView popContain;

    @Bind(R.id.iv_head)
    ImageView ivHead;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.line)
    View line;
    private String name;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Bind(R.id.iv_pull)
    ImageView imPull;

    @Bind(R.id.message)
    ImageView message;

    private PopupWindow pop;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private List<OrganizationAccount> belongOrgs;
    private BelongOrgasAdapter popAdapter;
    public ListView listView;


    @Override
    public int setContentView() {
        return R.layout.activity_personal_center;
    }


    @Override
    protected void onStart() {
        super.onStart();
        popContain.setOrientation(LinearLayout.VERTICAL);
        title.setText(App.app.getString(R.string.person_center_title));
        String defOrgaName = SpUtils.getString(Constants.key_currentBelongOrgaName);
        belongOrga.setText(defOrgaName == null ? "" : defOrgaName);
    }

    /**
     * 个人资料
     */
    @OnClick(R.id.personal_info)
    void setPersonalInfo() {
        IntentUtils.startAct(mAc, PersonalInfoActivity.class);
    }


    /**
     * 账户安全
     */
    @OnClick(R.id.account_safe)
    void setAccountSafe() {

        IntentUtils.startAct(mAc, AccountSecurityActivity.class);
    }


    /**
     * 消息中心
     */
    @OnClick(R.id.account_message)
    void setAccountMessage() {

//        IntentUtils.startAct(mAc, ChangeIpActivity.class);
    }

    /**
     * 切换组织机构
     */
    @OnClick(R.id.rl_belong)
    void setBelongOrga() {
        listBelongOrga();
    }

    private void listBelongOrga() {
        belongOrgs = (List<OrganizationAccount>) CacheUtils.getCache("belongOrgas");
        if (belongOrgs == null) {
            getBelongOrga(new callBack_getBelongOrga() {
                @Override
                public void onGetBelongOrga(List<OrganizationAccount> orgas) {
                    showBelongOrg(belongOrgs);
                }
            });
        } else {
            showBelongOrg(belongOrgs);
        }

    }

    /**
     * 显示有哪些所属组织
     */
    private void showBelongOrg(List<OrganizationAccount> belongOrgs) {
        if (belongOrgs.size() == 0) {
            ToastHelper.get().showShort("没有所属组织");
            return;
        }
        if (imPull.isEnabled()) {
            for (int i = 0; i < belongOrgs.size(); i++) {
                OrganizationAccount oa = belongOrgs.get(i);
                String name = StringUtils.belongOgaName(oa.getOrganizationId());
                if (TextUtils.isEmpty(name)) {
                    name = Admin.name;
                }
                View view = View.inflate(App.app, R.layout.item_change_belong_orga_1, null);
                TextView iv_name = (TextView) view.findViewById(R.id.belong_orga_name);
                iv_name.setText(name);
                final String finalName = name;
                view.setOnClickListener(v -> {
                    if (!finalName.equals(belongOrga.getText())) {
                        changeBelongOrga(oa);
                    }
                    belongOrga.setText(finalName);
                    imPull.setEnabled(!imPull.isEnabled());
                    popContain.removeAllViews();
                    popContain.setVisibility(View.INVISIBLE);
                });
                popContain.addView(view);
            }
            popContain.setVisibility(View.VISIBLE);

        } else {
            popContain.removeAllViews();
            popContain.setVisibility(View.INVISIBLE);
        }

        imPull.setEnabled(!imPull.isEnabled());
    }

    /**
     * 默认所属组织改变，更新本地数据，下一次进来作为默认组织
     */
    private void changeBelongOrga(OrganizationAccount oa) {

        //修改服务器端默认所属组织
        putOldBelongFalse();
        putDefBelongOrga(oa,true);


        //修改本地存储数据
        String oaId = oa.getOrganizationId();
        String name = StringUtils.belongOgaName(oaId);
        String id = StringUtils.belongOgaId(oaId);
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            id = Admin.id;
            name = Admin.name;
        }
        SpUtils.setString(Constants.key_currentBelongOrgaId, id);
        SpUtils.setString(Constants.key_currentBelongOrgaName, name);

        SpUtils.setString(Constants.key_currentOrgaId, id);
        SpUtils.setString(Constants.key_currentOrgaName, name);


        //发送默认所属组织改变的事件，在主页接受，
        EventBus.getDefault().post(new Event.DefOrgaHasChanges(oa));
    }

    /**
     * 修改原来的组织
     */
    private void putOldBelongFalse() {
        String currentBelongOrgaId = SpUtils.getString(Constants.key_currentBelongOrgaId);
        OrganizationAccount currentBelongOrga = null;
        if (belongOrgs != null && belongOrgs.size() != 0) {
            for (OrganizationAccount oa : belongOrgs) {
                String id = StringUtils.belongOgaId(oa.getOrganizationId());
                if (TextUtils.isEmpty(id)) {
                    id = Admin.id;
                }
                if (id.equals(currentBelongOrgaId)) {
                    if (oa.isIsDefault()){
                        currentBelongOrga = oa;
                    }
                }
            }

            //如果没有一个默认的，就是所属组织的第0个
            if (currentBelongOrga==null){
                currentBelongOrga = belongOrgs.get(0);
            }
            putDefBelongOrga(currentBelongOrga,false);

        }
    }

    /**
     * 更新服务器默认所属组织
     */
    private void putDefBelongOrga(OrganizationAccount oa,boolean isDefault) {
        double userId = (double) SpUtils.getInt(TokenManager.USERID);
        oa.setIsDefault(isDefault);
        addSubscription(ApiFactory.putDefBelongOrga(userId, oa.getId() + "", oa), new PgSubscriber<OrganizationAccount>() {
            @Override
            public void on_Next(OrganizationAccount oa) {
                ToastHelper.get().showShort("默认组织修改成功");
//                Logger.d("当前默认组织" + oa.getOrganizationId());
            }
        });
    }

}
