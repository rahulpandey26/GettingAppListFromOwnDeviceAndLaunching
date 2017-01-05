package com.robosoft.applauncher;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private PackageManager mPackageManager;
    private List<ApplicationInfo> mApplist;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        new GetApplications().execute();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mPackageManager = getPackageManager();
    }

    private class GetApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgress = null;

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(HomeActivity.this, null, getResources().getString(R.string.dialog_msg));
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mApplist = checkForApp(mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            setAdapter();
            mProgress.dismiss();
            super.onPostExecute(result);
        }
    }

    private void setAdapter() {
        AppListAdapter appListAdapter = new AppListAdapter(HomeActivity.this, mApplist);
        mListView.setAdapter(appListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = mPackageManager.getLaunchIntentForPackage(mApplist.get(position).packageName);

                    if (null != intent)
                        startActivity(intent);

                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private List<ApplicationInfo> checkForApp(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != mPackageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return applist;
    }
}
