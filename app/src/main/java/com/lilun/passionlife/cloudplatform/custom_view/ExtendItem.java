package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youke on 2016/6/21.
 */
public class ExtendItem extends RelativeLayout {

    private final Context cx;
    @Bind(R.id.item_title)
    TextView itemTitle;
    @Bind(R.id.item_btn)
    ImageView itemBtn;
    @Bind(R.id.item_extend)
    TextView itemExtend;
    @Bind(R.id.extend_content)
    ListView extendContent;
    @Bind(R.id.add_what)
    TextView addWhat;

    @Bind(R.id.ll1)
    LinearLayout ll1;

    private onClickListener listen;

    public ExtendItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;

        init();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ExtendItem);
        String tit = attr.getString(R.styleable.ExtendItem_extend_title);
        String hint = attr.getString(R.styleable.ExtendItem_extend_hint);
        String add = attr.getString(R.styleable.ExtendItem_add_btn);
        boolean isShow = attr.getBoolean(R.styleable.ExtendItem_is_show_add_btn, true);
        if (tit != null) {
            itemTitle.setText(tit);
        }
        if (hint != null) {
            itemExtend.setText(hint);
        }

        addWhat.setVisibility(isShow ? VISIBLE : GONE);
        if (add != null) {
            addWhat.setText(add);
        }

    }

    @OnClick(R.id.ll1)
    /**
     *圆框被点击的回调
     */
    void choise() {
        extendContent.setVisibility(GONE);
        setBtnChoise();
        if (listen != null) {
            listen.onBtnChoise(itemBtn.isEnabled());
        }

        setListViewBg(!itemBtn.isEnabled());

    }


    public void setListviewData(BaseAdapter adapter) {
        extendContent.setVisibility(VISIBLE);
        extendContent.setAdapter(adapter);
    }

    private void setListViewBg(boolean isEnable) {
        extendContent.setBackgroundResource(isEnable ? R.drawable.shape_green : R.drawable.shape_corner_gray);
        addWhat.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.add_what)
    /**
     *新增what被点击的回调
     */
    void setAddWhat() {
        if (listen != null) {
            Logger.d(" 新增部门 click");
            listen.onAddWhatClick();
        }
    }

    /**
     * 设置监听
     */
    public void setOnClickListen(onClickListener listen) {
        this.listen = listen;
    }

    /**
    *设置勾选框是否勾选
    */
    private void setBtnChoise() {
        itemBtn.setEnabled(!itemBtn.isEnabled());
    }

    /**
     *设置勾选框是否勾选
     */
    public void setBtnCheck(boolean btnCheck) {
        setListViewBg(!btnCheck);
        itemBtn.setEnabled(btnCheck);
    }


    public boolean isInherited(){
        return itemBtn.isEnabled();
    }

    /**
    *设置勾选框能够被点击
    */
    public void setLlenable(boolean llenable) {
        ll1.setClickable(llenable);
    }

    public interface onClickListener {
        void onBtnChoise(boolean enabled);

        void onAddWhatClick();
    }


    private void init() {
        View view = View.inflate(cx, R.layout.custom_add_orgi, this);
        ButterKnife.bind(view);
        itemBtn.setEnabled(false);
    }




    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


}
