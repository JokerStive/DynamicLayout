package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;

/**
 * Created by youke on 2016/7/28.
 * 眨眼效果的progressDialog
 */
public class WinkView extends View {
    private int mColor = Color.GREEN;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WinkView(Context context) {
        super(context);
        init();
    }


    public WinkView(Context context, AttributeSet attrs, int mColor) {
        super(context, attrs);
        this.mColor = mColor;
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int y = height / 2;

        int radius1=UIUtils.dip2px(App.app,10);
        int radius2=UIUtils.dip2px(App.app,10);
        int x1 = width / 2- UIUtils.dip2px(App.app,15);
        int x2 = width / 2+UIUtils.dip2px(App.app,15);

        canvas.drawCircle(x1,y,radius1,mPaint);
        canvas.drawCircle(x2,y,radius2,mPaint);
    }
}
