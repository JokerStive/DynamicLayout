package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.BelongOrgasAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PersonalCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.belong_orga)
    TextView belongOrga;

    @Bind(R.id.rl_belong)
    RelativeLayout rlBelong;


    @Bind(R.id.pop_contain)
    FrameLayout popContain;

    @Bind(R.id.iv_head)
    ImageView ivHead;

    @Bind(R.id.title)
    TextView title;

    @OnClick(R.id.back)
    void back(){finish();};

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
        title.setText(App.app.getString(R.string.person_center_title));
        String defOrgaName = SpUtils.getString(Constants.key_currentBelongOrgaName);
        belongOrga.setText(defOrgaName == null ? "" : defOrgaName);
    }

    /**
     * 个人资料
     */
    @OnClick(R.id.personal_info)
    void setPersonalInfo() {
//        IntentUtils.startAct(mAc, PersonalInfoActivity.class);
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
        if (pop == null && belongOrgs!=null) {
            popAdapter = new BelongOrgasAdapter(belongOrgs);
            View view = LayoutInflater.from(mCx).inflate(R.layout.item_change_belong_orga, null);
            // 设置 listview
            listView = (ListView) view.findViewById(R.id.first_list);
            listView.setOnItemClickListener(PersonalCenterActivity.this);
            listView.setFocusableInTouchMode(true);
            listView.setFocusable(true);
            listView.setAdapter(popAdapter);
            pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setFocusable(true);
            // 设置允许在外点击消失
            pop.setOutsideTouchable(true);
            // 刷新状态
            pop.update();
            pop.showAsDropDown(belongOrga);

            //  }
        } else {
            pop.showAsDropDown(belongOrga);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrganizationAccount oa = belongOrgs.get(position);
        belongOrga.setText(StringUtils.belongOgaName(oa.getOrganizationId()));
        pop.dismiss();
    }



}
