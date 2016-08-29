package com.lilun.passionlife.cloudplatform;

import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/8/29.
 */
public class BaseDataSourceImp {
    private CompositeSubscription mCompositeSubscription;
    private PgSubscriber pgSubscriber;

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (subscriber instanceof PgSubscriber) {
            pgSubscriber = (PgSubscriber) subscriber;
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
