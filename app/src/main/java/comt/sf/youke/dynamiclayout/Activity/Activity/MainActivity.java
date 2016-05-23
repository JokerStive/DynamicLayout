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
import comt.sf.youke.dynamiclayout.Activity.Activity.bean.Account;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.User.UserApi;
import comt.sf.youke.dynamiclayout.R;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

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
        Account user = new Account();
        user.setUsername("xixi");
        user.setPassword("1234");
        subscription.add(new UserApi().register(user)
                        .subscribe(new Action1<Account>() {
                            @Override
                            public void call(Account account) {

                            }
                        })
        );

       /* subscription.add(new UserApi().getAllUser()
                        .subscribe(new Action1<List<Account>>() {
                            @Override
                            public void call(List<Account> accounts) {
                                tvContent.setText(accounts.get(0).getId()+"----");
                            }
                        }));*/
/*
        subscription.add(new UserApi().delete("0").subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Toast.makeText(MainActivity.this,"delete success",Toast.LENGTH_SHORT).show();
            }
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
