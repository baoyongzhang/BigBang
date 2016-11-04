package com.baoyz.bigbang.core.xposed.setting;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.baoyz.bigbang.core.R;

import java.util.List;

import static com.baoyz.bigbang.core.xposed.setting.Adapter.Status.*;

/**
 * Created by dim on 16/11/4.
 */

public interface Adapter {


    int VIEW_TYPE_EMPTY = 2;
    int VIEW_TYPE_DATA = 1;
    int VIEW_TYE_LOAD = 0;

    enum Status {
        LOADING, LOAD
    }

     class AppInfoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private List<AppInfo> mData;
        private Status status = Status.LOADING;

        public AppInfoAdapter(List<AppInfo> data) {
            this.mData = data;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_EMPTY:
                    return new EmptyViewHolder(parent);
                case VIEW_TYPE_DATA:
                    return new AppInfoViewHolder(parent);
                case VIEW_TYE_LOAD:
                    return new LoadingViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {

        }

        public void loadData() {
            status = LOAD;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return mData.size() == 0 ? (status == LOADING ? VIEW_TYE_LOAD : VIEW_TYPE_EMPTY) : VIEW_TYPE_DATA;
            }
            return VIEW_TYPE_DATA;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
            AppInfo appInfo = null;
            if (mData.size() > position) {
                appInfo = mData.get(position);
            }
            holder.onBindViewHolder(appInfo);
        }

        @Override
        public int getItemCount() {
            return mData.size() == 0 ? 1 : mData.size();
        }

    }

    class BaseViewHolder<T extends Object> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void onBindViewHolder(T object) {

        }
    }


    class LoadingViewHolder extends BaseViewHolder {

        LoadingViewHolder(ViewGroup vp) {
            super(LayoutInflater.from(vp.getContext()).inflate(R.layout.item_loading, vp, false));
        }
    }

    class EmptyViewHolder extends BaseViewHolder {

        EmptyViewHolder(ViewGroup vp) {
            super(LayoutInflater.from(vp.getContext()).inflate(R.layout.item_empty_info, vp, false));
        }
    }

    class AppInfoViewHolder extends BaseViewHolder<AppInfo> implements CompoundButton.OnCheckedChangeListener {

        ImageView mIcon;
        TextView mAppNameTv;
        SwitchCompat mSwitchCompat;
        AppInfo appInfo;

        AppInfoViewHolder(ViewGroup vp) {
            super(LayoutInflater.from(vp.getContext()).inflate(R.layout.item_app_info, vp, false));
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mAppNameTv = (TextView) itemView.findViewById(R.id.app_name);
            mSwitchCompat = (SwitchCompat) itemView.findViewById(R.id.sc);
        }

        void onBindViewHolder(AppInfo appInfo) {
            this.appInfo = appInfo;
            mIcon.setImageDrawable(appInfo.applicationInfo.loadIcon(itemView.getContext().getPackageManager()));
            mAppNameTv.setText(appInfo.appName);
            mSwitchCompat.setChecked(appInfo.enable);
            mSwitchCompat.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.appInfo.enable = isChecked;
        }
    }
}
