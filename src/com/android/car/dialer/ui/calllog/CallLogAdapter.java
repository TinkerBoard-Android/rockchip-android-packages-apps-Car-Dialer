/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.car.dialer.ui.calllog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.car.widget.PagedListView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.car.dialer.R;
import com.android.car.dialer.log.L;
import com.android.car.dialer.ui.common.entity.UiCallLog;

import java.util.ArrayList;
import java.util.List;

/** Adapter for call history list. */
public class CallLogAdapter extends RecyclerView.Adapter<CallLogViewHolder>
        implements PagedListView.ItemCap {

    private static final String TAG = "CD.CallLogAdapter";
    private int mMaxItems = PagedListView.ItemCap.UNLIMITED;
    private List<UiCallLog> mUiCallLogs = new ArrayList<>();
    private Context mContext;

    public CallLogAdapter(Context context) {
        mContext = context;
    }

    public void setUiCallLogs(List<UiCallLog> uiCallLogs) {
        mUiCallLogs.clear();
        mUiCallLogs.addAll(uiCallLogs);
        notifyDataSetChanged();
    }

    @Override
    public void setMaxItems(int maxItems) {
        L.d(TAG, "setMaxItems %s", maxItems);
        mMaxItems = maxItems;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.call_history_list_item, parent, false);
        return new CallLogViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        holder.onBind(mUiCallLogs.get(position));
    }

    @Override
    public void onViewRecycled(@NonNull CallLogViewHolder holder) {
        holder.onRecycle();
    }

    @Override
    public int getItemCount() {
        return mMaxItems == PagedListView.ItemCap.UNLIMITED
                ? mUiCallLogs.size()
                : Math.min(mMaxItems, mUiCallLogs.size());
    }
}
