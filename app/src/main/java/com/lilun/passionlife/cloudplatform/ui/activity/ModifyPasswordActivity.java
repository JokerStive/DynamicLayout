package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ${hyz} on 2016/7/21.
 */
public class ModifyPasswordActivity extends BaseFunctionActivity {

    @Bind(R.id.et_old_password)
    EditText oldPassword;

    @Bind(R.id.et_new_password)
    EditText newPassword;

    public String oldPasswordStr;
    public String newPasswordStr;

    @Override
    public View setContent() {
        title.setText(getString(R.string.change_password));
        return inflater.inflate(R.layout.activity_modify_password,null);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.change)
    public void changePassword(){
        oldPasswordStr = oldPassword.getText().toString();
        newPasswordStr = newPassword.getText().toString();
        checkInput();


    }

    private void checkInput() {

        if(oldPasswordStr == null || oldPasswordStr.equals("")){
            Toast.makeText(this,"旧密码不能为空",Toast.LENGTH_SHORT).show();

        }else {
            if (oldPasswordStr != SpUtils.getString("password")) {
                Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
            }
        }
        if( newPasswordStr == null || newPasswordStr.equals("")){
            Toast.makeText(this,"新密码不能为空",Toast.LENGTH_SHORT).show();

        }else {
                if(newPasswordStr.length() < 6 ){
                    Toast.makeText(this,"输入的密码小于六位数",Toast.LENGTH_SHORT).show();
                }else if( newPasswordStr.length() > 20){
                    Toast.makeText(this,"输入的密码大于二十位数",Toast.LENGTH_SHORT).show();
                }
        }

    }

}
