package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.InputX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.IsX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.EnumX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youke on 2016/8/16.
 * 封装数据
 */
public class EncapSettingDataEnger {
    private Map<String, String> data = new HashMap<>();


    public Map<String, String> encapData(List<IsX> isXes, List<InputX> inputXes, List<EnumX> menuXes) {
        if (menuXes != null) {
            for (EnumX menuX : menuXes) {
                data.put(menuX.getId(), menuX.getSelectedOption());
            }
        }

        if (inputXes != null) {
            for (InputX inputX : inputXes) {
                data.put(inputX.getId(), inputX.getInputX());
            }
        }

        if (isXes != null) {
            for (IsX isX : isXes) {
                data.put(isX.getId(), isX.isX() + ""
                );
            }
        }

        return data;
    }

}
