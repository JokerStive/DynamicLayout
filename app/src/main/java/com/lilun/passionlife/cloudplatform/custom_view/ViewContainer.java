package com.lilun.passionlife.cloudplatform.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_roleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ViewContainer extends FlowLayout {

    private  ItemDeleteListener listen;
    private addStaff_roleListAdapter adapter;
    private String titl;
    private Context context;
    private TextView title;
    private TagFlowLayout content;

    //
    public ViewContainer(String titl, addStaff_roleListAdapter adapter) {
        super(App.app);
        this.titl = titl;
        this.adapter = adapter;
        init();
    }

    public ViewContainer(String titl, addStaff_roleListAdapter adapter,ItemDeleteListener listen) {
        super(App.app);
        this.titl = titl;
        this.adapter = adapter;
        this.listen = listen;
        init();
    }


    public ViewContainer(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }


    private void init() {
        setHorizontalSpacing(UIUtils.dip2px(App.app,10));
        setVerticalSpacing(UIUtils.dip2px(App.app,10));
        View view = LayoutInflater.from(App.app).inflate(R.layout.custom_view_container, this);
        title = (TextView) view.findViewById(R.id.container_title);
        title.setText(titl);
        TextView delete = (TextView) view.findViewById(R.id.container_delete);
        delete.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) getParent();
            parent.removeView(this);
            listen.onItemDelete();
        });
        setAdapter(adapter);


    }


//    public

    public interface  ItemDeleteListener{
        void onItemDelete();
    }


    public addStaff_roleListAdapter getAdapter(){
        if (adapter!=null){
            return adapter;
        }

        return null;
    }

    public List<Role>  getChoiseRoles(){
       return adapter.getChoiseRoles();
    }

}
