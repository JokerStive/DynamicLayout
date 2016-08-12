package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ShowmessageAdapter;
import com.lilun.passionlife.cloudplatform.adapter.ShowmessageTypeAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerMessageActivity;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ListMessageFragment  extends StatedFragment {
    private LayoutInflater inflater;
    private RecyclerView message_type;
    private RecyclerView messages;
    private ManagerMessageActivity activity;
    private List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_manager_message, null);
        message_type = (RecyclerView) view.findViewById(R.id.rv_message_type);
        message_type.setLayoutManager(new LinearLayoutManager(App.app,LinearLayoutManager.HORIZONTAL,false));
        messages = (RecyclerView) view.findViewById(R.id.rv_messages);
        messages.setLayoutManager(new LinearLayoutManager(App.app,LinearLayoutManager.VERTICAL,false));
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (ManagerMessageActivity)getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        setInitData();
        setData();
    }

    private void setData() {
        ShowmessageTypeAdapter showmessageTypeAdapter = new ShowmessageTypeAdapter(data, R.layout.item_message_type);
        showmessageTypeAdapter.setOnItemClickListener(position -> {
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListMessageFragment(), data.get(position)));
        });
        message_type.setAdapter(showmessageTypeAdapter);

        ShowmessageAdapter adapter = new ShowmessageAdapter(data,R.layout.item_message);
        adapter.setOnItemClickListener(position -> {
            ToastHelper.get().showShort("item position = "+position);
        });

        messages.setAdapter(adapter);
    }

    private void setInitData() {
        data = new ArrayList<>();
        for (int i=0;i<10;i++){
            data.add("类别-"+i);
        }

    }
}
