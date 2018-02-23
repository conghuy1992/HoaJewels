package com.hoa.jewels.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoa.jewels.R;
import com.hoa.jewels.dto.UserDto;

import java.util.List;

/**
 * Created by maidinh on 5/30/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    List<UserDto> ListUser;

    public SpinnerAdapter(Context context, List<UserDto> ListUser) {
        this.context = context;
        this.ListUser = ListUser;
    }

    @Override
    public int getCount() {
        return ListUser.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(ListUser.get(position).getUsername().trim());
        return convertView;
    }
}
