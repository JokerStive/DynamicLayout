package comt.sf.youke.dynamiclayout.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import comt.sf.youke.dynamiclayout.Activity.Activity.base.BaseActivity;
import comt.sf.youke.dynamiclayout.Activity.Activity.utils.ProgressDialogHelper;
import comt.sf.youke.dynamiclayout.R;

public class MainActivity extends BaseActivity {

    private static final String TAG="OkHttp";

    @Bind(R.id.btn_dynamic)
    Button btnDynamic;
    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        EventBus.getDefault().register(this);
    }

//    on


    public void next(View v) {
        ProgressDialogHelper.createLoadingDialog(this,"加载中...").show();
        /*subscription.add(new UserApi().getAllUser()
                .subscribe(new ProgressSubscriber<List<Account>>(new SubscriberOnNextListener<List<Account>>() {
                    @Override
                    public void onNext(List<Account> accounts) {
                        Log.i(TAG, "success" + "---");
                    }
                },MainActivity.this)));*/
    /*    Account user = new Account();
        user.setUsername("huhu");
        subscription.add(new UserApi().register(user)
                        .subscribe(account -> {
                            Log.i(TAG, account.getUsername() + "---");
                        }, throwable -> {
                            Log.i(TAG, throwable.getMessage() + "---");
                        })
        );

        subscription.add(new UserApi().delete("0").subscribe());*/



        /*subscription.add(new UserApi().getAllUser()
                        .subscribe(accounts -> {
                            tvContent.setText(accounts.get(0).getId() + "----");
                        }, throwable -> {
                            Log.e("OkHttp",throwable.getMessage()+"----");
                            tvContent.setText(throwable.getMessage() + "----");
                        }));*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            setAttrs(btnDynamic);
        }
    }

    private void setAttrs(Button btnDynamic) {
        ViewGroup.LayoutParams lp = btnDynamic.getLayoutParams();
        lp.height = 60;
        lp.width = 60;
        btnDynamic.setLayoutParams(lp);
        btnDynamic.setVisibility(View.VISIBLE);
    }



}
