package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PersonalCenterActivity extends BaseFunctionActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.belong_orga)
    TextView belongOrga;

    @Bind(R.id.rl_belong)
    RelativeLayout rlBelong;



    @Bind(R.id.pop_contain)
    FrameLayout popContain;

    private PopupWindow pop;
    private List<String> data = new ArrayList<>();
    private List<OrganizationAccount> belongOrgs;

    @Override
    public View setContent() {
        title.setText(getString(R.string.person_center_title));
        View view = inflater.inflate(R.layout.activity_personal_center, null);
        return view;
    }


    @Override
    protected void onStart() {
        super.onStart();
        String defOrgaName = SpUtils.getString(Constants.key_defOrgina);
        belongOrga.setText(defOrgaName == null ? "" : defOrgaName);
    }

    /**
     * 个人资料
     */
    @OnClick(R.id.personal_info)
    void setPersonalInfo() {

    }

    /**
     * 账户安全
     */
    @OnClick(R.id.account_safe)
    void setAccountSafe() {

    }

    /**
     * 切换组织机构
     */
    @OnClick(R.id.rl_belong)
    void setBelongOrga() {
        listBelongOrga();
    }

    private void listBelongOrga() {
        if (pop == null) {
            Logger.d("-------");
            belongOrgs = (List<OrganizationAccount>) CacheUtils.getCache(Constants.BELONG_ORGA);
            if (belongOrgs != null) {
                for (OrganizationAccount a : belongOrgs) {
                    data.add(a.getOrganization().getName());
                }
                ListView lv = new ListView(mCx);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCx, android.R.layout.simple_list_item_1, data);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(PersonalCenterActivity.this);

                pop = new PopupWindow(lv, rlBelong.getWidth(), 300, true);
//                pop.setContentView(lv);
                pop.setBackgroundDrawable(new ColorDrawable(0));

                pop.showAtLocation(popContain, Gravity.RIGHT | Gravity.CENTER_VERTICAL,0,0);
            }
        } else {
            pop.showAtLocation(popContain, Gravity.RIGHT | Gravity.CENTER_VERTICAL,0,0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //设置切换的默认组织

    }





}
