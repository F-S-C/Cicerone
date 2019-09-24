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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
     * The home fragment (first tab).
     */
    private final Fragment homeFragment = new HomeFragment();

    /**
     * The discover fragment (second tab).
     */
    private final Fragment discoverFragment = new DiscoverFragment();

    /**
     * The fragment manager used to load, unload, show and hide the fragments.
     */
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    /**
     * The currently displayed fragment.
     */
    private Fragment activeFragment = homeFragment;

    /**
     * The bottom navigation.
     */
    private BottomNavigationView navView;

    private boolean isFabMenuExtended = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFabReport;
    private LinearLayout layoutFabItinerary;


    /**
     * Create the activity and load the layout.
     *
     * @param savedInstanceState if the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Note: Otherwise it is
     *                           null. This value may be null. (From the official
     *                           <a href="https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle,%2520android.os.PersistableBundle)">Android Documentation</a>).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.bottom_navigation);
        if (!AccountManager.isLogged()) {
            navView.getMenu().findItem(R.id.navigation_profile).setTitle(getString(R.string.login));
            navView.getMenu().findItem(R.id.navigation_profile).setIcon(getDrawable(R.drawable.ic_login));
            navView.getMenu().removeItem(R.id.empty_menu_item);
        }
        fabSettings = findViewById(R.id.fab);
        layoutFabReport = findViewById(R.id.layout_fab_new_report);
        layoutFabItinerary = findViewById(R.id.layout_fab_new_itinerary);

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
            if (AccountManager.getCurrentLoggedUser().getUserType() != UserType.CICERONE)
                layoutFabItinerary.setVisibility(View.GONE);
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

        Fragment profileFragment = AccountManager.isLogged() ? new AccountDetails(swipeRefreshLayout) : null;
        WishlistFragment wishlistFragment = AccountManager.isLogged() ? new WishlistFragment() : null;

        navView.setOnNavigationItemSelectedListener(item -> {
            ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
            boolean toReturn = false;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeCurrentFragment(homeFragment);
                    supportActionBar.setTitle(getString(R.string.app_name));
                    toReturn = true;
                    break;
                case R.id.navigation_favorites:
                    if (AccountManager.isLogged()) {
                        changeCurrentFragment(wishlistFragment);
                        supportActionBar.setTitle(getString(R.string.wishlist));
                        if (wishlistFragment != null) wishlistFragment.refresh();
                    } else {
                        Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
                    }
                    toReturn = AccountManager.isLogged();
                    break;
                case R.id.navigation_discover:
                    changeCurrentFragment(discoverFragment);
                    supportActionBar.setTitle(getString(R.string.discover));
                    toReturn = true;
                    break;
                case R.id.navigation_profile:
                    if (!AccountManager.isLogged()) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        changeCurrentFragment(profileFragment);
                        supportActionBar.setTitle(getString(R.string.account));
                    }
                    toReturn = true;
                    break;
                default:
                    break;
            }
            return toReturn;
        });

        // Instantiate all of the fragments for usability purposes.
        if (profileFragment != null)
            fragmentManager.beginTransaction().add(R.id.main_container, profileFragment, "4").hide(profileFragment).commit();
        if (wishlistFragment != null)
            fragmentManager.beginTransaction().add(R.id.main_container, wishlistFragment, "3").hide(wishlistFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, discoverFragment, "2").hide(discoverFragment).commit();
        // The last one is shown to the user
        fragmentManager.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();
    }

    /**
     * Change the current displayed fragment.
     *
     * @param nextFragment The fragment to be loaded.
     */
    private void changeCurrentFragment(Fragment nextFragment) {
        fragmentManager.beginTransaction().hide(activeFragment).show(nextFragment).commit();
        activeFragment = nextFragment;
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(!nextFragment.equals(homeFragment));
        supportActionBar.setDisplayShowHomeEnabled(!nextFragment.equals(homeFragment));
    }

    @Override
    public void onBackPressed() {
        if (activeFragment.equals(homeFragment)) {
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
        layoutFabReport.setVisibility(View.INVISIBLE);
        layoutFabItinerary.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.ic_add_black_24dp);
        isFabMenuExtended = false;
    }

    private void openSubMenusFab() {
        layoutFabReport.setVisibility(View.VISIBLE);
        layoutFabItinerary.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
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
            /*button.setOnClickListener(view -> new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_insert_report))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        if (allFilled()) {
                            ReportManager.addNewReport(context, currentLoggedUser, users.getSelectedItem().toString(), object.getText().toString(), body.getText().toString());
                            refresh();
                            dialogSubmit.dismiss();
                        }else
                            Toast.makeText(context, context.getString(R.string.error_fields_empty),
                                    Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show());*/
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
                } else
                    Toast.makeText(MainActivity.this, getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
            });

        });

        dialogSubmit.show();
    }
}
