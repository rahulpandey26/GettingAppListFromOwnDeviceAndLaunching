package com.robosoft.applauncher;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rahul on 29/7/16.
 */
public class AppListAdapter extends BaseAdapter {

    private PackageManager mPackageManager;
    private List<ApplicationInfo> mAppList;

    public AppListAdapter(HomeActivity context, List<ApplicationInfo> applist) {
        mAppList = applist;
        mPackageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppListViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_row, parent, false);
            viewHolder = new AppListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (AppListViewHolder) convertView.getTag();

        viewHolder.mAppIcon.setImageDrawable(mAppList.get(position).loadIcon(mPackageManager));
        viewHolder.mAppNameTxt.setText(mAppList.get(position).loadLabel(mPackageManager));

        String pacakageName = mAppList.get(position).packageName;
        PackageInfo packageInfo = null;
        try {
            packageInfo = mPackageManager.getPackageInfo(pacakageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int verCode = 0;
        if (packageInfo != null) {
            verCode = packageInfo.versionCode;
        }
        viewHolder.mAppVersionTxt.setText(parent.getResources().getString(R.string.version_code) + String.valueOf(verCode));

        return convertView;
    }

    public class AppListViewHolder {

        private ImageView mAppIcon;
        private TextView mAppNameTxt, mAppVersionTxt;

        public AppListViewHolder(View view) {
            mAppIcon = (ImageView) view.findViewById(R.id.app_icon);
            mAppNameTxt = (TextView) view.findViewById(R.id.app_name);
            mAppVersionTxt = (TextView) view.findViewById(R.id.app_version);
        }
    }
}
