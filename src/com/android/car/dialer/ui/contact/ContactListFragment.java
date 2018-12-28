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

import static androidx.car.widget.PagedListView.UNLIMITED_PAGES;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.car.widget.PagedListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.car.dialer.R;
import com.android.car.dialer.entity.Contact;
import com.android.car.dialer.ui.view.VerticalListDividerDecoration;
import com.android.car.dialer.ui.common.DialerBaseFragment;

/**
 * Contact Fragment.
 */
public class ContactListFragment extends DialerBaseFragment implements
        ContactListAdapter.OnShowContactDetailListener {
    private static final String CONTACT_DETAIL_FRAGMENT_TAG = "CONTACT_DETAIL_FRAGMENT_TAG";
    private ContactListAdapter mContactListAdapter;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.contact_list_fragment, container, false);

        mContactListAdapter = new ContactListAdapter(
                getContext(), /* onShowContactDetailListener= */this);
        PagedListView pagedListView = fragmentView.findViewById(R.id.list_view);
        pagedListView.setAdapter(mContactListAdapter);
        pagedListView.getRecyclerView().addItemDecoration(
                new VerticalListDividerDecoration(getContext(), /* hideLastDivider= */true));
        pagedListView.setMaxPages(UNLIMITED_PAGES);

        ContactListViewModel contactListViewModel = ViewModelProviders.of(this).get(
                ContactListViewModel.class);
        contactListViewModel.getAllContacts().observe(this, mContactListAdapter::setContactList);
        return fragmentView;
    }

    @Override
    public void onShowContactDetail(Contact contact) {
        Fragment contactDetailsFragment = ContactDetailsFragment.newInstance(contact, null);
        pushContentFragment(contactDetailsFragment, CONTACT_DETAIL_FRAGMENT_TAG);
    }


    @StringRes
    @Override
    protected int getActionBarTitleRes() {
        return R.string.contacts_title;
    }
}
