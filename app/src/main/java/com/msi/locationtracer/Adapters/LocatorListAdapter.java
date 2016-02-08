package com.msi.locationtracer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msi.locationtracer.Data_Model.UserInfo;
import com.msi.locationtracer.R;

import java.util.List;

/**
 * Created by sahid_000 on 2/3/2016.
 */
public class LocatorListAdapter extends BaseAdapter {

    private Context context;
    private List<UserInfo> guestInfoData;

    private LayoutInflater layoutInflater;
    private View guestInfoView;

    public LocatorListAdapter(Context context, List<UserInfo> guestInfoData) {
        this.context = context;
        this.guestInfoData = guestInfoData;
    }

    @Override
    public int getCount() {
        return guestInfoData.size();
    }

    @Override
    public Object getItem(int position) {
        return guestInfoData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = LayoutInflater.from(context);
        guestInfoView = layoutInflater.inflate(R.layout.locators_list_contents, null, false);
        final UserInfo userInfo = guestInfoData.get(position);

        TextView guestName = (TextView) guestInfoView.findViewById(R.id.guestName);

        guestName.setText(userInfo.getUserName());
        return guestInfoView;
    }
}
