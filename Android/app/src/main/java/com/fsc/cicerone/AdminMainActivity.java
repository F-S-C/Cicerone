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

package com.fsc.cicerone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fsc.cicerone.manager.AccountManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class AdminMainActivity extends AppCompatActivity {

    /**
     * The currently displayed fragment.
     */
    private Fragment activeFragment;

    /**
     * The bottom navigation.
     */
    private BottomNavigationView navView;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;

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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.active_itineraries));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.admin_main_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (activeFragment instanceof Refreshable) {
                ((Refreshable) activeFragment).refresh(swipeRefreshLayout);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setUpViewPager();

        // fix to make the ViewPager work with SwipeRefreshLayout
        viewPager.setOnTouchListener((v, event) -> {
            swipeRefreshLayout.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                swipeRefreshLayout.setEnabled(true);
            }
            return false;
        });

        navView = findViewById(R.id.bottom_navigation_admin);
        navView.setOnNavigationItemSelectedListener(item -> {
            boolean toReturn = false;
            switch (item.getItemId()) {
                case R.id.navigation_itineraries_active_admin:
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
        });
    }

    @Override
    public void onBackPressed() {
        if (activeFragment == fragmentPagerAdapter.getItem(0)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_exit))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> AdminMainActivity.super.onBackPressed())
                    .create()
                    .show();
        } else {
            navView.setSelectedItemId(R.id.navigation_itineraries_active_admin);
        }
    }

    private void setUpViewPager() {
        viewPager = findViewById(R.id.main_container);
        fragmentPagerAdapter = new AdminMainActivityPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        activeFragment = fragmentPagerAdapter.getItem(0);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {

                ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
                supportActionBar.setDisplayHomeAsUpEnabled(position != 0);
                supportActionBar.setDisplayShowHomeEnabled(position != 0);
                supportActionBar.setTitle(fragmentPagerAdapter.getPageTitle(position));

                activeFragment = fragmentPagerAdapter.getItem(position);

                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    navView.getMenu().getItem(0).setChecked(false);

                navView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class AdminMainActivityPagerAdapter extends FragmentPagerAdapter {
        private final HomeFragment homeFragment = new HomeFragment();
        private final ReportFragment reportFragment = new ReportFragment();
        private final UsersListFragment usersFragment = new UsersListFragment();

        AdminMainActivityPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

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

        @Override
        public int getCount() {
            return 3;
        }
    }
}