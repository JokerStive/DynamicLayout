package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.BaseX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_view.EnumView;

import java.util.List;

/**
 * Created by you on 2016/8/15.
 * 枚举类型的可配置项
 */
public class EnumX extends BaseX {
    private List<String> menuOptions;
    private String selectedOption;
    private EnumView enumView;

    public EnumX(String id, String title, List<String> menuOptions) {
        this.id = id;
        this.title = title;
        this.menuOptions = menuOptions;
        this.enumView = new EnumView();
        enumView.setMenuView_group(menuOptions);
        enumView.setMenuView_title(title);
        enumView.setOnSelectedListener(selectedOption1 -> {
            selectedOption =selectedOption1;
        });
    }

    public EnumView getEnumView() {
        return enumView;
    }


    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        enumView.setRadioSelected(selectedOption);
    }
}
