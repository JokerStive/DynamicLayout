package com.lilun.passionlife.cloudplatform.utils;

import android.app.Activity;
import android.content.Intent;

import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerDeptActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerMessageActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerModuleActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerOrganizationActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerRoleActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.ManagerStaffActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.SystemConfigActivity;

/**
 * Created by Administrator on 2016/6/3.
 */
public class IntentUtils {
    public static void startAct(Activity old,Class<?> clazz){
        Intent intent = new Intent(old,clazz);
        old.startActivity(intent);
    }


    public static void startModuleActivity(Activity mAc,String serviceId){
        //模块管理
        if (serviceId.equals(KnownServices.Module_Service)) {
            IntentUtils.startAct(mAc, ManagerModuleActivity.class);
        }

        //员工管理
        else if (serviceId.equals(KnownServices.Account_Service)) {
            IntentUtils.startAct(mAc, ManagerStaffActivity.class);
        }

        //角色管理
        else if (serviceId.equals(KnownServices.Role_Service)) {
//                    Logger.d("角色管理");
            IntentUtils.startAct(mAc, ManagerRoleActivity.class);
        }

        //组织机构管理
        else if (serviceId.equals(KnownServices.Organization_Service)) {
            IntentUtils.startAct(mAc, ManagerOrganizationActivity.class);
        }

        //系统配置
        else if (serviceId.equals(KnownServices.SysConfig_Service)) {
            IntentUtils.startAct(mAc, SystemConfigActivity.class);
        }


        //部门管理
        else if (serviceId.equals(KnownServices.Department_Service)) {
            IntentUtils.startAct(mAc, ManagerDeptActivity.class);
        }

        //消息管理
        else if (serviceId.equals(KnownServices.Information_Service)) {
            IntentUtils.startAct(mAc, ManagerMessageActivity.class);
//                    ToastHelper.get().showShort("暂未实现");
        }
    }
}
