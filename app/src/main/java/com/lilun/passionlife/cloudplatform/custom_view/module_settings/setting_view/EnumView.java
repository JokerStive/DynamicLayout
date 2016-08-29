package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class EnumView extends RelativeLayout{

    private TextView menuView_title;
    private RadioGroup menuView_group;
    private OnSelectedListener listener;

    public EnumView() {
        super(App.app);
        init();
    }

    private void init() {
        View view = View.inflate(App.app, R.layout.item_menu_view, this);
        menuView_title = (TextView) view.findViewById(R.id.menuView_title);
        menuView_group = (RadioGroup) view.findViewById(R.id.menuView_group);
    }

    public void setMenuView_title(String title){
        menuView_title.setText(title);
    }

    public void setMenuView_group(List<String> menuOpions){
        for (int i=0;i<menuOpions.size();i++){
            RadioButton radioButton = (RadioButton) LayoutInflater.from(App.app).inflate(R.layout.item_radiobutton, null);
            radioButton.setText(menuOpions.get(i));
            menuView_group.addView(radioButton);
        }

        menuView_group.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton button = (RadioButton) group.findViewById(checkedId);
//            button.setChecked(!button.isChecked());
            if (listener!=null){
                listener.onSelected(button.getText().toString());
            }

        });
    }


    public void setOnSelectedListener(OnSelectedListener listener){
        this.listener =listener;
    }
    public interface OnSelectedListener{
        void onSelected(String selectedOption);
    }


    public void setRadioSelected(String selectedOption){
        int childCount = menuView_group.getChildCount();
        for (int i=0;i<childCount;i++){
            RadioButton radioButton = (RadioButton) menuView_group.getChildAt(i);
            if (radioButton.getText().equals(selectedOption)){
                radioButton.setChecked(true);
            }
        }
    }
}
