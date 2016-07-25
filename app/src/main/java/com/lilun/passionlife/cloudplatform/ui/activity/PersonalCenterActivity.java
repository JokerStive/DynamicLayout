package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListPopAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private List<OrganizationAccount> belongOrgs;
    private ListPopAdapter popAdapter;
    public ListView listView;
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

        IntentUtils.startAct(mAc,AccountSecurityActivity.class);
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
//            belongOrgs = (List<OrganizationAccount>) CacheUtils.getCache(Constants.BELONG_ORGA);
//            if (belongOrgs != null) {
//                for (OrganizationAccount a : belongOrgs) {
//                    data.add(a.getOrganization().getName());
//                }
            HashMap<String, String> map =  new HashMap<String, String>();
            map.put("name","保洁部");
            data.add(map);
            HashMap<String, String> map1 =  new HashMap<String, String>();
            map1.put("name", "保洁1部");
            data.add(map1);
            HashMap<String, String> map2 =  new HashMap<String, String>();
            map2.put("name", "工程部");
            data.add(map2);
            popAdapter = new ListPopAdapter(mCx);
            popAdapter.setSpinnerValue(data);
            View view = LayoutInflater.from(mCx).inflate(R.layout.popmenu_single, null);
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
        //设置切换的默认组织
        HashMap<String, String> map = (HashMap<String, String>) popAdapter.getItem(position);
        //  belongOrga.setText(data.get(position));
        belongOrga.setText(map.get("name"));
        pop.dismiss();
    }





}
