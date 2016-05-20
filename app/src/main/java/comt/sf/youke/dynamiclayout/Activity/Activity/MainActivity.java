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
import comt.sf.youke.dynamiclayout.Activity.Activity.bean.AclUser;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.User.UserApi;
import comt.sf.youke.dynamiclayout.R;
import retrofit2.Response;
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
        /*Intent intent = new Intent(this, SecondActivity.class);
        startActivityForResult(intent, 0);*/
        subscription.add(new UserApi().register(new AclUser())
                        .subscribe(new Action1<Response<AclUser>>() {
                            @Override
                            public void call(Response<AclUser> aclUserResponse) {

                            }
                        })
        );

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
