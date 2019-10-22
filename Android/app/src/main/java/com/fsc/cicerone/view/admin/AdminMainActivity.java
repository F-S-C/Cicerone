/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.view.user.HomeFragment;
import com.fsc.cicerone.view.user.registered_user.LoginActivity;
import com.fsc.cicerone.view.system.MainActivity;
import com.fsc.cicerone.view.user.registered_user.ReportFragment;
import com.fsc.cicerone.view.user.UsersListFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

/**
 * The main activity Admin-side.
 */
public class AdminMainActivity extends MainActivity {

    /**
     * Empty Constructor.
     */
    public AdminMainActivity() {
        super();
        this.layout = R.layout.activity_admin_main;
    }

    /**
     * A Constructor that takes a Layout as a parameter.
     *
     * @param contentLayoutId The Layout to set.
     */
    public AdminMainActivity(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_admin_main;
    }

    /**
     * Create the activity and load the layout.
     *
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle). Note: Otherwise it is null. This
     *                           value may be null. (From the official
     *                           <a href="https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle,%2520android.os.PersistableBundle)">Android
     *                           Documentation</a>).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.active_itineraries));


    }

    /**
     * @see MainActivity#setupFragmentAdapter()
     */
    @Override
    protected void setupFragmentAdapter() {
        fragmentPagerAdapter = new AdminMainActivityPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them appear
     * non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean toReturn = false;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                toReturn = true;
                break;
            case R.id.navigation_reports_admin:
                viewPager.setCurrentItem(1);
                toReturn = true;
                break;
            case R.id.navigation_users_admin:
                viewPager.setCurrentItem(2);
                toReturn = true;
                break;
            case R.id.navigation_logout_admin:
                new MaterialAlertDialogBuilder(AdminMainActivity.this)
                        .setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.exit_confirm_answer))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            AccountManager.logout(AdminMainActivity.this);
                            Intent i = new Intent(this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
                toReturn = true;
                break;
            default:
                break;
        }
        return toReturn;
    }

    /**
     * Adapter for ViewPager.
     */
    private class AdminMainActivityPagerAdapter extends FragmentPagerAdapter {
        private final HomeFragment homeFragment = new HomeFragment();
        private final ReportFragment reportFragment = new ReportFragment();
        private final UsersListFragment usersFragment = new UsersListFragment();

        /**
         * @see FragmentPagerAdapter#FragmentPagerAdapter(FragmentManager, int)
         */
        AdminMainActivityPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        /**
         * @see androidx.fragment.app.FragmentPagerAdapter#getItem(int)
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = homeFragment;
            if (position == 1) {
                fragment = reportFragment;
            } else if (position == 2) {
                fragment = usersFragment;
            }
            return fragment;
        }

        /**
         * @see androidx.fragment.app.FragmentPagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = getString(R.string.active_itineraries);
            if (position == 1) {
                title = getString(R.string.reports);
            } else if (position == 2) {
                title = getString(R.string.list_users);
            }
            return title;
        }

        /**
         * @see FragmentPagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            return 3;
        }
    }
}