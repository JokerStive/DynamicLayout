package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddAuthrovityFragment extends BaseFunctionFragment {


    @Bind(R.id.lv_add_auth_list)
    ListView lvAddAuthList;
    @Bind(R.id.save)
    Button save;

    @Override
    public View setView() {
        View view = inflater.inflate(R.layout.fragment_add_authority, null);
        return view;
    }



}
