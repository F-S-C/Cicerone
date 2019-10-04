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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReportManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    /**
     * The currently displayed fragment.
     */
    private Fragment activeFragment;

    /**
     * The bottom navigation.
     */
    private BottomNavigationView navView;

    private boolean isFabMenuExtended = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFabReport;
    private LinearLayout layoutFabItinerary;
    private FrameLayout subFabContainer;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;

    /**
     * Create the activity and load the layout.
     *
     * @param savedInstanceState if the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Note: Otherwise it is
     *                           null. This value may be null. (From the official
     *                           <a href="https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle,%2520android.os.PersistableBundle)">Android Documentation</a>).
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpViewPager();

        navView = findViewById(R.id.bottom_navigation);
        if (!AccountManager.isLogged()) {
            navView.getMenu().findItem(R.id.navigation_profile).setTitle(getString(R.string.login));
            navView.getMenu().findItem(R.id.navigation_profile).setIcon(getDrawable(R.drawable.ic_login));
            navView.getMenu().removeItem(R.id.empty_menu_item);
        }
        fabSettings = findViewById(R.id.fab);
        layoutFabReport = findViewById(R.id.layout_fab_new_report);
        layoutFabItinerary = findViewById(R.id.layout_fab_new_itinerary);
        subFabContainer = findViewById(R.id.fabFrame);

        subFabContainer.setOnClickListener(v -> closeSubMenusFab());

        FloatingActionButton fabReport = layoutFabReport.findViewById(R.id.fab_new_report);
        FloatingActionButton fabItinerary = layoutFabItinerary.findViewById(R.id.fab_new_itinerary);

        if (!AccountManager.isLogged()) {
            fabSettings.setVisibility(View.GONE);
        } else {
            fabSettings.setOnClickListener(v -> {
                if (isFabMenuExtended) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            });

            fabReport.setOnClickListener(v -> {
                insertReport();
                closeSubMenusFab();
            });
            fabItinerary.setOnClickListener(v -> {
                closeSubMenusFab();
                Intent i = new Intent().setClass(MainActivity.this, ItineraryCreation.class);
                startActivity(i);
            });
        }
        closeSubMenusFab();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_activity_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (activeFragment instanceof Refreshable) {
                ((Refreshable) activeFragment).refresh(swipeRefreshLayout);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // fix to make the ViewPager work with SwipeRefreshLayout
        viewPager.setOnTouchListener((v, event) -> {
            swipeRefreshLayout.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                swipeRefreshLayout.setEnabled(true);
            }
            return false;
        });

        navView.setOnNavigationItemSelectedListener(item -> {
            closeSubMenusFab();
            boolean toReturn = false;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    toReturn = true;
                    break;
                case R.id.navigation_discover:
                    viewPager.setCurrentItem(1);
                    toReturn = true;
                    break;
                case R.id.navigation_favorites:
                    if (AccountManager.isLogged()) {
                        viewPager.setCurrentItem(2);
                    } else {
                        Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
                    }
                    toReturn = AccountManager.isLogged();
                    break;
                case R.id.navigation_profile:
                    if (!AccountManager.isLogged()) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        viewPager.setCurrentItem(3);
                    }
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
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> MainActivity.super.onBackPressed())
                    .create()
                    .show();
        } else {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void closeSubMenusFab() {
        subFabContainer.setVisibility(View.GONE);
        layoutFabReport.setVisibility(View.GONE);
        layoutFabItinerary.setVisibility(View.GONE);
        fabSettings.setImageResource(R.drawable.ic_add_black_24dp);
        isFabMenuExtended = false;
    }

    private void openSubMenusFab() {
        subFabContainer.setVisibility(View.VISIBLE);
        layoutFabReport.setVisibility(View.VISIBLE);
        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE)
            layoutFabItinerary.setVisibility(View.VISIBLE);
        fabSettings.setImageResource(R.drawable.ic_dialog_close_dark);
        isFabMenuExtended = true;
    }

    private void insertReport() {
        View reportView = getLayoutInflater().inflate(R.layout.dialog_new_report, null);

        EditText object = reportView.findViewById(R.id.object);
        EditText body = reportView.findViewById(R.id.body);
        Spinner users = reportView.findViewById(R.id.users);

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        AccountManager.setUsersInSpinner(this, users);

        AlertDialog dialogSubmit = new MaterialAlertDialogBuilder(this)
                .setView(reportView)
                .setTitle(getString(R.string.insert_report))
                .setMessage(getString(R.string.report_dialog_message))
                .setPositiveButton(R.string.insert_report, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialogSubmit.setOnShowListener(dialogInterface -> {

            Button button = dialogSubmit.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                if (!object.getText().toString().equals("") && !body.getText().toString().equals("") && !users.getSelectedItem().equals("")) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle(R.string.insert_report)
                            .setMessage(R.string.sure_to_insert_report)
                            .setPositiveButton(R.string.yes, (dialog, witch) -> {
                                ReportManager.addNewReport(MainActivity.this, currentLoggedUser, users.getSelectedItem().toString(), object.getText().toString(), body.getText().toString(), success ->
                                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.report_sent), Toast.LENGTH_SHORT).show());
//                                refresh();
                                dialogSubmit.dismiss();
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                } else{
                 if(object.getText().toString().equals("")) object.setError(getString(R.string.empty_object_error));
                 if(body.getText().toString().equals("")) body.setError(getString(R.string.empty_body_error));
                }
            });

        });

        dialogSubmit.show();
    }

    private void setUpViewPager() {
        viewPager = findViewById(R.id.main_container);
        fragmentPagerAdapter = AccountManager.isLogged() ?
                new MainActivityAfterLoginPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) :
                new MainActivityPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

                navView.getMenu().getItem(position < 2 ? position : position + 1).setChecked(true);
                prevMenuItem = navView.getMenu().getItem(position < 2 ? position : position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
    }

    private class MainActivityPagerAdapter extends FragmentPagerAdapter {
        private final HomeFragment homeFragment = new HomeFragment();
        private final DiscoverFragment discoverFragment = new DiscoverFragment();

        MainActivityPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = discoverFragment;
            if (position == 0) {
                fragment = homeFragment;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = getString(R.string.app_name);
            if (position == 1) {
                title = getString(R.string.discover);
            }
            return title;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class MainActivityAfterLoginPagerAdapter extends MainActivityPagerAdapter {
        private final WishlistFragment wishlistFragment = new WishlistFragment();
        private final AccountDetails profileFragment = new AccountDetails();

        MainActivityAfterLoginPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = super.getItem(position);
            if (position == 2) {
                fragment = wishlistFragment;
            } else if (position == 3) {
                fragment = profileFragment;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = super.getPageTitle(position);
            if (position == 2) {
                title = getString(R.string.wishlist);
            } else if (position == 3) {
                title = getString(R.string.profile);
            }
            return title;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
