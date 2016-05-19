package comt.sf.youke.dynamiclayout.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import comt.sf.youke.dynamiclayout.Activity.Activity.base.BaseActivity;
import comt.sf.youke.dynamiclayout.Activity.Activity.bean.AclUser;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.User.UserApi;
import comt.sf.youke.dynamiclayout.R;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

        subscription.add(new UserApi()
                        .getAllUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<List<AclUser>>>() {
                            @Override
                            public void call(Response<List<AclUser>> listResponse) {
                                tvContent.setText(listResponse.body().get(0).getId()+"-------------------");
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
