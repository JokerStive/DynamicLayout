package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ShowOrgiChildrenAdapter;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by youke on 2016/7/4.
 */
public class ShowOrgiPopupwindow  extends View{
    private final int wihth;
    private final int height;
    private final onItemClick listener;
    private  PopupWindow pop;
    private Context cx;
    private  TextView tv_location;
    private  GridView gv_orgis;
    private List<Organization> data;

    public ShowOrgiPopupwindow(Context context,int wihth,int height,onItemClick listener) {
        super(context);
        this.cx = context;
        this.wihth = wihth;
        Logger.d("height =  "+height);
        this.height = height;
        this.listener = listener;
        init(context);
    }

    public void init(Context cx) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.custom_showorgi_popup, null);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        gv_orgis = (GridView) view.findViewById(R.id.gv_orgis);
        tv_location.setText(SpUtils.getString(Constants.key_child_OrginaName));
        pop = new PopupWindow(view,wihth, height,true);
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.setOnDismissListener(() -> {
            Logger.d("pop dismiss");
        });

        pop.setOnDismissListener(() -> {
            listener. onDismiss();
        });
//        showAsDropDown(targetView,0,0);
    }


    public void setData(List<Organization> data){
        this.data = data;
        if (data!=null){
            gv_orgis.setAdapter(new ShowOrgiChildrenAdapter(cx,data));
            gv_orgis.setOnItemClickListener((parent, view, position, id) -> {
                tv_location.setText(data.get(position).getName());
                listener.onItemClick(data.get(position).getId(),data.get(position).getName());
            });
        }
    }

    public interface onItemClick{
        void onItemClick(String orgId,String orgName);
        void onDismiss();
    }


    public void  getPopupWindow(View ta){
        if (pop!=null &&pop.isShowing()){
            pop.dismiss();
        }else{
//            pop.showAtLocation();
            pop.showAsDropDown(ta,0,60);
        }
    }

}
