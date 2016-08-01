package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;

/**
 * Created by youke on 2016/6/21.
 */
public class PullChoiseView extends RelativeLayout{

    private  Context cx;
    private TextView show_data;
    private ImageView icon;
    private View view;
    private PopupWindow pop;
    private BaseAdapter adapter;
    private OnItemChooseListener listener;

    public PullChoiseView(Context context) {
        super(context);
        this.cx=context;
        init();
    }


    public PullChoiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        init();
    }
    private void init() {
        view = View.inflate(cx, R.layout.custom_pull_choise, this);
        show_data = (TextView) view.findViewById(R.id.show_data);
        icon = (ImageView) view.findViewById(R.id.pull_choise_icon);
        view.setOnClickListener(listen ->{
            setEnable(!icon.isEnabled());
        });
    }

    private void setEnable(boolean isEnable){
        //开始 是 true
        icon.setEnabled(isEnable);
        if (null!=listener){
            if (null!=adapter){
                getPopupWindow();
            }
        }
    }

    public void init(OnItemChooseListener listener,BaseAdapter adapter){
        this.adapter=adapter;
        this.listener =listener;
    }

    public void setShow_data(String data){
        show_data.setText(data);
    }




        private void initPopupWindow(Context cx, OnItemChooseListener listener, View targetView, BaseAdapter adapter) {
            View view = LayoutInflater.from(cx).inflate(R.layout.custom_pull_choise_popup, null);
            ListView lv_pull_popup = (ListView) view.findViewById(R.id.lv_pull_popup);
            //listview设置数据
            lv_pull_popup.setAdapter(adapter);
            //条目点击回调
            lv_pull_popup.setOnItemClickListener((parent, view1, position, id) -> {
                if (pop !=null){
                    icon.setEnabled(true);
                    dis();
                }
                listener.OnItemChoose(position);
            });

            //初始化popup
            pop = new PopupWindow(view,targetView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT,true);
            pop.setBackgroundDrawable(new ColorDrawable(0));
            pop.setOnDismissListener(() -> {
                icon.setEnabled(true);
            });
            pop.showAsDropDown(targetView,0,0);
        }

        private  void  getPopupWindow(){
            if (pop!=null &&pop.isShowing()){
                pop.dismiss();
            }else{
                initPopupWindow(cx,listener,this,adapter);
            }
        }
//            if (null!= pop){
//                Logger.d("dismiss popup ");
//                dis();
//            }else {
//                Logger.d("show popup ");
//                initPopupWindow(cx,listener,this,adapter);
//            }}

    private void dis() {
        pop.dismiss();
//        pop=null;
    }
//    }

    public interface OnItemChooseListener{
        void OnItemChoose(int position);
    }
}
