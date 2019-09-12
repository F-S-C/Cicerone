package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class AdminMainActivity extends AppCompatActivity {

    /**
     * The itinerary active fragment (first tab).
     */
    private final Fragment itineraryActiveFragment = new HomeFragment();

    /**
     * The reports list fragment (second tab).
     */
    private final Fragment reportsFragment = new ReportFragment();

    /**
     * The registered users list fragment (third tab).
     */
    private final Fragment usersListFragment = new UsersListFragment();

    /**
     * The fragment manager used to load, unload, show and hide the fragments.
     */
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    /**
     * The currently displayed fragment.
     */
    private Fragment activeFragment = itineraryActiveFragment;

    /**
     * The bottom navigation.
     */
    private BottomNavigationView navView;


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
        setContentView(R.layout.activity_admin_main);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.active_itineraries));

        navView = findViewById(R.id.bottom_navigation_admin);
        navView.setOnNavigationItemSelectedListener(item -> {
            boolean toReturn = false;
            switch (item.getItemId()) {
                case R.id.navigation_itineraries_active_admin:
                    changeCurrentFragment(itineraryActiveFragment);
                    supportActionBar.setTitle(getString(R.string.active_itineraries));
                    toReturn = true;
                    break;
                case R.id.navigation_reports_admin:
                    changeCurrentFragment(reportsFragment);
                    supportActionBar.setTitle(getString(R.string.reports));
                    toReturn = true;
                    break;
                case R.id.navigation_users_admin:
                    changeCurrentFragment(usersListFragment);
                    supportActionBar.setTitle(getString(R.string.list_users));
                    toReturn = true;
                    break;
                case R.id.navigation_logout_admin:
                    new MaterialAlertDialogBuilder(AdminMainActivity.this)
                            .setTitle(getString(R.string.are_you_sure))
                            .setMessage(getString(R.string.exit_confirm_answer))
                            .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                                AccountManager.logout();
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

        // Instantiate all of the fragments for usability purposes.
        fragmentManager.beginTransaction().add(R.id.main_container_admin, usersListFragment, "3").hide(usersListFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container_admin, reportsFragment, "2").hide(reportsFragment).commit();
        // The last one is shown to the user
        fragmentManager.beginTransaction().add(R.id.main_container_admin, itineraryActiveFragment, "1").commit();
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
        supportActionBar.setDisplayHomeAsUpEnabled(!nextFragment.equals(itineraryActiveFragment));
        supportActionBar.setDisplayShowHomeEnabled(!nextFragment.equals(itineraryActiveFragment));
    }

    @Override
    public void onBackPressed() {
        if (activeFragment.equals(itineraryActiveFragment)) {
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
    
    @Override
    public boolean onSupportNavigateUp() { //TODO: Add to class diagram
        onBackPressed();
        return true;
    }
}