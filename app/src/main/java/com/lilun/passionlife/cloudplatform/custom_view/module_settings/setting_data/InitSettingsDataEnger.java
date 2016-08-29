package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.InputX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.IsX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.EnumX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by youke on 2016/8/15.
 * 初始化 配置项 显示数据
 */
public class InitSettingsDataEnger {
    private final String INT_TYPE="int";
    private final String TYPE="type";
    private final String MENU_TYPE="menu";
    private final String STRING_TYPE="string";
    private final String BOOLEAB_TYPE="boolean";
    private  Map<String, String> dataInit;
    private Map<String,Map<String,Object>> data;
    private List<IsX> list_isX;
    private List<InputX> list_inputX;
    private List<EnumX> list_menuX;

    public InitSettingsDataEnger(Map<String,Map<String,Object>> data, Map<String,String> dataInit) {
        if (data!=null){
            this.data = data;
            this.dataInit = dataInit;
            parserData();
        }
    }

    private void parserData() {
        list_isX = new ArrayList<>();
        list_inputX = new ArrayList<>();
        list_menuX = new ArrayList<>();

            for(String key:data.keySet()){
                Map<String, Object> map = data.get(key);
                String type = map.get(TYPE).toString();
                if (type.equals(BOOLEAB_TYPE)){
                    String title = getTitle(map);
                    if (title!=null){
                        boolean is = Boolean.parseBoolean(dataInit.get(key));
                        IsX isX = new IsX(key,title);
                        isX.setX(is);
                        list_isX.add(isX);
                    }
                }else if (type.equals(INT_TYPE) || type.equals(STRING_TYPE)){
                    String title = getTitle(map);
                    if (title!=null){
                        String input = dataInit.get(key);
                        InputX inputX = new InputX(key,title);
                        inputX.setInputX(input);
                        list_inputX.add(inputX);
                    }

                }else if (type.equals(MENU_TYPE)){
                    String title = null;
                    List<String> menuOptions = null;
                    for (String key1:map.keySet()){
                        if (key1.equals("title")){
                             title = (String) map.get(key1);
                        }

                        if (key1.equals("menu")){
                            menuOptions = (List<String>) map.get(key1);
                        }
                    }

                    if (title!=null && menuOptions!=null){
                        String selectedOption = dataInit.get(key);
                        EnumX menuX = new EnumX(key,title,menuOptions);
                        menuX.setSelectedOption(selectedOption);
                        list_menuX.add(menuX);
                    }

                }

            }


    }


    public List<IsX> getIsXs(){
        if (list_isX==null || list_isX.size()==0){
            return null;
        }
        return list_isX;
    }

    public List<InputX> getInputXs(){
        if (list_inputX==null || list_inputX.size()==0){
            return null;
        }
        return list_inputX;
    }

    public List<EnumX> getMenuXs(){
        if (list_menuX==null || list_menuX.size()==0){
            return null;
        }
        return list_menuX;
    }

    private String getTitle(Map<String, Object> map) {
        String title = null;
        for(String key1:map.keySet()){
            if (key1.equals("title")){
                title = (String) map.get(key1);
            }
        }
        return title;
    }
}
