package comt.sf.youke.dynamiclayout.Activity.Activity.base;

import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/19.
 */
public class BaseActivity extends AppCompatActivity{
    protected CompositeSubscription subscription = new CompositeSubscription();
}
