package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;

/**
 * Created by youke on 2016/6/10.
 */
public class CrumbView extends HorizontalScrollView{

    private final int tv_color;
    private int LIGHT_COLOR, DARK_COLOR;
    private Resources mRes;
    private LinearLayout mContainer;
    private FragmentManager mFragmentManager;
    private  int backGround;
    private final boolean isTvBg;
    private final float tv_size;

    public CrumbView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setHorizontalScrollBarEnabled(false);
        mRes = context.getResources();
        TypedArray typedArray = mRes.obtainAttributes(attrs, R.styleable.CrumbViewAttrs);
        try{
            tv_color = typedArray.getColor(R.styleable.CrumbViewAttrs_tv_color,-1);

            tv_size = typedArray.getDimension(R.styleable.CrumbViewAttrs_tv_size, 40);
            isTvBg = typedArray.getBoolean(R.styleable.CrumbViewAttrs_is_tv_bg, true);
            backGround = typedArray.getColor(R.styleable.CrumbViewAttrs_back_ground, -1);
        }finally {
            typedArray.recycle();
        }

        initView(context);
    }

    private void initView(Context context) {
        mContainer = new LinearLayout(context);
//        mContainer.setBackgroundResource(default_white);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setPadding(mRes.getDimensionPixelOffset(R.dimen.crumb_view_padding), 0,
                mRes.getDimensionPixelOffset(R.dimen.crumb_view_padding), 0);
        mContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mContainer);
    }

    public void setActivity(FragmentActivity activity){
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(() -> updateCrumbs());
        updateCrumbs();
    }

    private void updateCrumbs() {
        // 嵌套的fragment数量
        int numFrags = mFragmentManager.getBackStackEntryCount();
        // 面包屑的数量
        int numCrumbs = mContainer.getChildCount();

        for(int i = 0; i < numFrags; i++){
            final FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(i);
            if(i < numCrumbs){
                View view = mContainer.getChildAt(i);
                Object tag = view.getTag();
                if(tag != backStackEntry){
                    for(int j = i; j < numCrumbs; j++){
                        mContainer.removeViewAt(i);
                    }
                    numCrumbs = i;
                }
            }
            if(i >= numCrumbs){
                TextView itemView=new TextView(getContext());
                itemView.setGravity(Gravity.CENTER);
                itemView.setTextColor(Color.WHITE);
//                itemView.setcolo

                if (isTvBg){
                    itemView.setBackgroundResource(i <= 0 ? R.drawable.crumbs1 : R.drawable.crumbs2);
                }
                itemView.setTextColor(tv_color);
                itemView.setTextSize(UIUtils.px2sp(App.app,tv_size));
                itemView.setText(backStackEntry.getBreadCrumbTitle());
                itemView.setTag(backStackEntry);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager.BackStackEntry bse;
                        if (v.getTag() instanceof FragmentManager.BackStackEntry) {
                            bse = (FragmentManager.BackStackEntry) v.getTag();
                            mFragmentManager.popBackStack(bse.getId(), 0);
                        } else {
                            //全部回退
                            int count = mFragmentManager.getBackStackEntryCount();
                            if (count > 0) {
                                bse = mFragmentManager.getBackStackEntryAt(0);
                                mFragmentManager.popBackStack(bse.getId(), 0);
                            }
                        }
                    }
                });
                mContainer.addView(itemView);
            }
        }
        numCrumbs = mContainer.getChildCount();
        while(numCrumbs > numFrags){
            mContainer.removeViewAt(numCrumbs - 1);
            numCrumbs--;
        }



        // 滑动到最后一个
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }


}
