package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.custom_view.ExtendItem;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddPositionFragment extends BaseFunctionFragment implements ExtendItem.onClickListener{

    @Override
    public View setView() {
        return inflater.inflate(R.layout.fragment_add_position,null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }


    @Override
    public void onBtnChoise(boolean enabled) {

    }

    @Override
    public void onAddWhatClick() {

    }
}
