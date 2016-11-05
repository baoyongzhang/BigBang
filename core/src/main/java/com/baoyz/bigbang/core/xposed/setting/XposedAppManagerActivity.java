package com.baoyz.bigbang.core.xposed.setting;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.baoyz.bigbang.core.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static com.baoyz.bigbang.core.xposed.XposedConstant.*;

public class XposedAppManagerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "XposedApp";
    private List<AppInfo> mAllAppInfo = new ArrayList<>();
    private List<AppInfo> mResultAppInfo = new ArrayList<>();
    private RecyclerView mRV;
    private SharedPreferences mPreferences;
    private Set<String> mDisAppSet = new HashSet<>();
    private String mKeyWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xposed_app_manager);
        setTitle("App 管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPreferences = getSharedPreferences(SP_NAME, MODE_WORLD_READABLE);
        mRV = (RecyclerView) findViewById(R.id.rv);
        mRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRV.setAdapter(new Adapter.AppInfoAdapter(mResultAppInfo));
        new LoadAppInfoClass().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_manger, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                search(searchView.getQuery().toString());
            }
        });
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    private void search(String query) {
        if (mKeyWord.equals(query)) {
            return;
        }
        mKeyWord = query;
        mResultAppInfo.clear();
        if (TextUtils.isEmpty(query)) {
            mResultAppInfo.addAll(mAllAppInfo);
        } else {
            for (AppInfo appInfo : mAllAppInfo) {
                if (appInfo.appName.contains(query)) {
                    mResultAppInfo.add(appInfo);
                }
            }
        }
        updateResultAppInfo();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    class LoadAppInfoClass extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDisAppSet = mPreferences.getStringSet(SP_DISABLE_KEY, mDisAppSet);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PackageManager packageManager = XposedAppManagerActivity.this.getPackageManager();
            List<ApplicationInfo> packageInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo applicationInfo : packageInfoList) {
                mAllAppInfo.add(new AppInfo(applicationInfo, applicationInfo.loadLabel(packageManager).toString(),
                        applicationInfo.packageName, !mDisAppSet.contains(applicationInfo.packageName)));
            }
            mResultAppInfo.addAll(mAllAppInfo);
            Collections.sort(mResultAppInfo, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    if (o1.enable != o2.enable) {
                        return o1.enable ? 1 : -1;
                    }
                    return 0;
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateResultAppInfo();
        }

    }

    private void updateResultAppInfo() {
        ((Adapter.AppInfoAdapter) mRV.getAdapter()).loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        } else if (i == R.id.search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void finish() {
        Set<String> disableAppList = new HashSet<>();
        for (AppInfo appInfo : mAllAppInfo) {
            if (!appInfo.enable) {
                disableAppList.add(appInfo.packageName);
            }
        }
        mPreferences.edit().putStringSet(SP_DISABLE_KEY, disableAppList).apply();
        super.finish();
    }
}
