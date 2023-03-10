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
package com.android.car.dialer.ui.contact;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.android.car.dialer.R;
import com.android.car.dialer.ui.common.DialerUtils;
import com.android.car.telephony.common.Contact;
import com.android.car.telephony.common.TelecomUtils;
import com.android.car.ui.recyclerview.CarUiRecyclerView;
import com.android.car.ui.recyclerview.ContentLimitingAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

/**
 * Adapter for contact list.
 */
class ContactListAdapter extends ContentLimitingAdapter<ContactListViewHolder>
        implements CarUiRecyclerView.OnAttachListener {
    private static final String TAG = "CD.ContactListAdapter";

    private final Context mContext;
    private final ContactListViewHolder.Factory mViewHolderFactory;
    private final List<Contact> mContactList = new ArrayList<>();

    private Integer mSortMethod;
    private CarUiRecyclerView mRrecyclerView;
    private int mLimitingAnchorIndex = 0;

    @Inject
    ContactListAdapter(
            @ActivityContext Context context,
            ContactListViewHolder.Factory viewHolderFactory) {
        mContext = context;
        mViewHolderFactory = viewHolderFactory;
    }

    /**
     * Sets {@link #mContactList} based on live data.
     */
    public void setContactList(Pair<Integer, List<Contact>> contactListPair) {
        mContactList.clear();
        if (contactListPair != null) {
            mContactList.addAll(contactListPair.second);
            mSortMethod = contactListPair.first;
        }
        updateUnderlyingDataChanged(mContactList.size(),
                DialerUtils.validateListLimitingAnchor(mContactList.size(), mLimitingAnchorIndex));
        notifyDataSetChanged();
    }

    @Override
    public int computeAnchorIndexWhenRestricting() {
        mLimitingAnchorIndex = DialerUtils.getFirstVisibleItemPosition(mRrecyclerView);
        return mLimitingAnchorIndex;
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolderImpl(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, parent,
                false);
        return mViewHolderFactory.create(itemView);
    }

    @Override
    public void onBindViewHolderImpl(@NonNull ContactListViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        String header = getHeader(contact);

        boolean showHeader = position == 0
                || (!header.equals(getHeader(mContactList.get(position - 1))));
        holder.bind(contact, showHeader, header, mSortMethod);
    }

    @Override
    public int getUnrestrictedItemCount() {
        return mContactList.size();
    }

    @Override
    public int getConfigurationId() {
        return R.id.contact_list_uxr_config;
    }

    @Override
    public void onViewRecycledImpl(@NonNull ContactListViewHolder holder) {
        // Calling super.onViewRecycled() will cause an infinite loop.
        holder.recycle();
    }

    private String getHeader(Contact contact) {
        String label;
        if (TelecomUtils.SORT_BY_LAST_NAME.equals(mSortMethod)) {
            label = contact.getPhonebookLabelAlt();
        } else {
            label = contact.getPhonebookLabel();
        }

        return !TextUtils.isEmpty(label) ? label
                : mContext.getString(R.string.header_for_type_other);
    }

    @Override
    public void onAttachedToCarUiRecyclerView(@NonNull CarUiRecyclerView recyclerView) {
        mRrecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromCarUiRecyclerView(@NonNull CarUiRecyclerView recyclerView) {
        mRrecyclerView = null;
    }
}
