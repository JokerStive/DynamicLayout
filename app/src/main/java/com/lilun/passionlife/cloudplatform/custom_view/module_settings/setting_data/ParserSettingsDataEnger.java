package com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_data;

import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.InputX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.IsX;
import com.lilun.passionlife.cloudplatform.custom_view.module_settings.setting_class.EnumX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by youkr on 2016/8/15.
 * 解析数据类
 */
public class ParserSettingsDataEnger {
    private final String INT_TYPE="int";
    private final String TYPE="type";
    private final String ENUM_TYPE ="enum";
    private final String STRING_TYPE="string";
    private final String BOOLEAB_TYPE="boolean";
    private Map<String,Map<String,Object>> data;
    private List<IsX> list_isX;
    private List<InputX> list_inputX;
    private List<EnumX> list_menuX;

    public ParserSettingsDataEnger(Map<String,Map<String,Object>> data) {
        if (data!=null){
            this.data = data;
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

                //判断类型
                if (type.equals(BOOLEAB_TYPE)){
                    String title = getTitle(map);
                    if (title!=null){
                        IsX isX = new IsX(key,title);
                        list_isX.add(isX);
                    }
                }


                //输入类型
                else if (type.equals(INT_TYPE) || type.equals(STRING_TYPE)){
                    String title = getTitle(map);
                    if (title!=null){
                        InputX inputX = new InputX(key,title);
                        list_inputX.add(inputX);
                    }

                }


                //枚举类型
                else if (type.equals(ENUM_TYPE)){
                    String title = null;
                    List<String> menuOptions = null;
                    for (String key1:map.keySet()){
                        if (key1.equals("title")){
                             title = (String) map.get(key1);
                        }

                        if (key1.equals("enum")){
                            menuOptions = (List<String>) map.get(key1);
                        }
                    }

                    if (title!=null && menuOptions!=null){
                        EnumX menuX = new EnumX(key,title,menuOptions);
                        list_menuX.add(menuX);
                    }

                }


                //

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
