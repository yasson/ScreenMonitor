package com.ys.sm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ys.sm.R;
import com.ys.sm.model.AppRecordModel;
import com.ys.sm.utils.ImageLoader;
import com.ys.sm.utils.Tool;
import com.ys.sm.utils.Utils;

import java.util.List;

/**
 * app record
 * Created by ys on 2015/12/17.
 */
public class AdapterAR extends BaseAdapter {
    class ViewHolder {
        ImageView logo;
        TextView  name;
        TextView  count;
        TextView  time;
    }

    Context              mContext;
    List<AppRecordModel> list;
    LayoutInflater       mInf;

    public AdapterAR(Context context, List<AppRecordModel> list) {
        this.mContext = context;
        this.list = list;
        this.mInf = LayoutInflater.from(context);
    }

    public void setListAndNotify(List<AppRecordModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInf.inflate(R.layout.item_app_record, null);
            vh.logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            vh.name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.count = (TextView) convertView.findViewById(R.id.tv_count);
            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);
        }
        AppRecordModel model = list.get(position);
        String count = mContext.getResources().getString(R.string.times) + ":" + model.count;
        String duation = mContext.getResources().getString(R.string.light_remain_time) + ":" + Tool.getDuation(model.duation);
        vh = (ViewHolder) convertView.getTag();
        vh.logo.setTag(model.pName);
        ImageLoader.getInstance().loadImg(vh.logo, model.pName);
        vh.name.setText(Utils.getPachageName(mContext, model.pName));
        vh.count.setText(count);
        vh.time.setText(duation);
        return convertView;
    }
}
