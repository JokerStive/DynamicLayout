package com.lilun.passionlife.cloudplatform.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lilun.passionlife.R;

/**
 * Created by youke on 2016/7/20.
 */
public class AlertDiaog extends Dialog {

    private final String title;
    private final onConfigListener listen;
    private final Context cx;
    private static  int theme = R.style.alert_dialog;

    public AlertDiaog(Context cx,String title, onConfigListener listen) {
        super(cx,theme);
        this.title = title;
        this.cx = cx;
        this.listen = listen;
        init();
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        setContentView(view);
        TextView titl = (TextView) view.findViewById(R.id.title);
        TextView config = (TextView) view.findViewById(R.id.config);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        titl.setText(title);
        config.setOnClickListener(v -> {
            listen.onConfig();
            dismiss();
        });

        cancle.setOnClickListener(v -> {
            dismiss();
        });
        show();


    }



    public interface  onConfigListener{
        void onConfig();
    }


}
