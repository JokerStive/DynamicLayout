package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ShowmessageAdapter;
import com.lilun.passionlife.cloudplatform.adapter.ShowmessageTypeAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseLoadingFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerMessageActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.MessageDetailActivity;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SnackbarHelper;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ListMessageFragment extends BaseLoadingFragment {


    RecyclerView message_type;

    RecyclerView messages;
    private ManagerMessageActivity activity;
    private List<OrganizationInformation> msgTypelist;
    private List<OrganizationInformation> msglist;
    private int parentId = -1;
    private Bundle bundle;
    private List<OrganizationInformation> infos;
    private RelativeLayout message_empty;
    private ShowmessageAdapter showmessageAdapter;
    private ShowmessageTypeAdapter showmessageTypeAdapter;
    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        activity = (ManagerMessageActivity) getActivity();
        bundle = new Bundle();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.get("parentId") != null) {
            parentId = (int) bundle.get("parentId");
        }

        msglist = new ArrayList<>();
        msgTypelist = new ArrayList<>();
        getOassInfos();
    }

    @Override
    public void onStart() {
        super.onStart();
        App.parentId = parentId;
        if (infos != null) {
            showSuccess();
            showMessage();
        }
    }

    @Subscribe
    public void reflashInfo(Event.reflashInfo event) {
        showSuccess();
        OrganizationInformation info = event.getInfo();
        if (showmessageTypeAdapter==null ||showmessageAdapter==null ){
            getOassInfos();
        }

        if (info.isIsCat()) {
            msgTypelist.add(info);
            showmessageTypeAdapter.add(info);
        } else {
            msglist.add(info);
            showmessageAdapter.add(info);
        }

    }

    @Subscribe
    public void setMessageDeleteShow(Event.setMessageDeleteShow event) {
        if (showmessageAdapter != null) {
            showmessageAdapter.setIsDeleteShow();
        }else{
            SnackbarHelper.makeShort(activity.getCoordinatorLayout(),"没有任何信息", Color.GREEN);
        }
    }


    @Override
    protected void onErrorPageClick() {
        super.onErrorPageClick();
        getOassInfos();
    }

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(App.app).inflate(R.layout.fragment_manager_message, null);
        message_type = (RecyclerView) view.findViewById(R.id.rv_message_type);
        message_type.setLayoutManager(new LinearLayoutManager(App.app, LinearLayoutManager.HORIZONTAL, false));
        messages = (RecyclerView) view.findViewById(R.id.rv_messages);
        messages.setLayoutManager(new LinearLayoutManager(App.app, LinearLayoutManager.VERTICAL, false));

        message_empty = (RelativeLayout) view.findViewById(R.id.message_empty);
        return view;
    }


    /**
     * 获取当前组织下的所有信息
     */
    private void getOassInfos() {
        if (loadingPager!=null){
            showLoading();
        }
        String filter = FilterUtils.orgaInfoFilter(SpUtils.getString(Constants.key_currentOrgaId), parentId);
        activity.addSubscription(ApiFactory.getOrgaInfos(filter), new PgSubscriber<List<OrganizationInformation>>() {
            @Override
            public void on_Next(List<OrganizationInformation> ois) {
                if (ois.size() == 0) {
                    showEmpty();
                    return;
                }
                infos = ois;
                showSuccess();
                filteMessage(ois);
                showMessage();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });

    }

    /**
     * 过滤信息那些是类别，那些是基本信息
     */
    private void filteMessage(List<OrganizationInformation> ois) {
        msgTypelist.clear();
        msglist.clear();
        for (OrganizationInformation oi : ois) {
            if (oi.isIsCat()) {
                msgTypelist.add(oi);
            } else {
                msglist.add(oi);
            }
        }
    }


    /**
     * 展示信息
     */
    private void showMessage() {
//        filteMessage(ois);
        //展示信息类别

        showmessageTypeAdapter = new ShowmessageTypeAdapter(msgTypelist, R.layout.item_message_type);
        showmessageTypeAdapter.setOnItemClickListener(position -> {
            double id = (double) infos.get(position).getId();
            bundle.putSerializable("parentId", (int) id);
            Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new ListMessageFragment(), msgTypelist.get(position).getTitle());
            event.setBundle(bundle);
            EventBus.getDefault().post(event);
        });
        showmessageTypeAdapter.setOnItemLongClickListener(position -> {
            showEditMsgTypeDialog(position);
        });

        message_type.setAdapter(showmessageTypeAdapter);


        //展示信息
        message_empty.setVisibility(msglist.size() != 0 ? View.GONE : View.VISIBLE);
        if (message_empty.getVisibility() == View.GONE) {

            showmessageAdapter = new ShowmessageAdapter(msglist, R.layout.item_message, R.layout.page_empty);
            showmessageAdapter.setOnItemClickListener(new OnItemClickListerer());
            messages.setAdapter(showmessageAdapter);
        }
    }


    private void showEditMsgTypeDialog(int position) {

    }


    /**
     * 信息list 条目点击监听
     */
    private class OnItemClickListerer implements ShowmessageAdapter.OnItemClickListener {

        @Override
        public void onItemClick(int position) {
            //信息详情页面
            intent = IntentUtils.getBundleIntent(MessageDetailActivity.class, "message", msglist.get(position));
            IntentUtils.startAct(activity, intent);
        }

        @Override
        public void onItemDelete(int position) {
            //删除信息
            showmessageAdapter.removeAt(position);
            showmessageAdapter.setIsDeleteShow();
            activity.addSubscription(ApiFactory.deleteInfo((double) msglist.get(position).getId()), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                    Logger.d("删除成功");
                    msglist.remove(position);
                }
            });
        }

        @Override
        public void onEmptyData() {
            message_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingPager.isLoadingShow()) {
            activity.onUnsubscribe();
            showError();
        }
        EventBus.getDefault().unregister(this);
    }
}
