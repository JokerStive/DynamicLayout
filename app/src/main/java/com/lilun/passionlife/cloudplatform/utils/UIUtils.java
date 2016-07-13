package com.lilun.passionlife.cloudplatform.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by youke on 2016/7/11.
 */
public class UIUtils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int dip2px(Context context,float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int px2sp(Context context,float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
