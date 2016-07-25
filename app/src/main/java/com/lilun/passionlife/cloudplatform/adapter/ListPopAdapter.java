package com.lilun.passionlife.cloudplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListPopAdapter<T> extends BaseAdapter {

    private String TAG = ListPopAdapter.class.getCanonicalName();

    private ArrayList<HashMap<String, String>> spinnerValue;
    private Context context;

    public ListPopAdapter(Context con) {
        this.context = con;
    }

    public void setSpinnerValue(ArrayList<HashMap<String, String>> spinnerValue) {
        this.spinnerValue = spinnerValue;
    }

    @Override
    public int getCount() {
        return spinnerValue.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem(int position) {
        return (T) spinnerValue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemValueId(int position) {
        return spinnerValue.get(position).get("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.pomenu_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);

            holder.spinnerLayout = (LinearLayout) convertView
                    .findViewById(R.id.spinner_layout);

            holder.spinnerName = (TextView) convertView
                    .findViewById(R.id.spinner_name);
            holder.spinnerID = (TextView) convertView
                    .findViewById(R.id.spinner_id);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> t = spinnerValue.get(position);

        holder.spinnerName.setText(t.get("name"));
      //  holder.spinnerID.setText(t.get("id"));

//        holder.spinnerLayout.setBackgroundColor(context.getResources().getColor(
//                t.get("selectID").equals(t.get("id"))
//                        ? R.color.default_white : R.color.default_bg_gray));
        holder.spinnerLayout.setBackgroundColor(context.getResources().getColor(R.color.default_white));
        return convertView;
    }

    private final class ViewHolder {
        TextView spinnerName;
        TextView spinnerID;
        LinearLayout spinnerLayout;
    }
}
