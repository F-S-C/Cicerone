package com.fsc.cicerone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private final Fragment usersListFragment = new HomeFragment();

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
        final Dialog logoutDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(logoutDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        logoutDialog.setContentView(R.layout.activity_logout);
        logoutDialog.setCancelable(true);

        navView = findViewById(R.id.bottom_navigation_admin);
        navView.setOnNavigationItemSelectedListener(item -> {
                    boolean toReturn = false;
                    ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
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
                            Button noButton = logoutDialog.findViewById(R.id.no_logout_button);
                            noButton.setOnClickListener(view1 -> logoutDialog.hide());

                            Button yesButton = logoutDialog.findViewById(R.id.yes_logout_button);
                            yesButton.setOnClickListener(view1 -> {
                                logoutDialog.hide();
                                logoutDialog.dismiss();
                                AccountManager.logout();
                                Intent i = new Intent(this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            });
                            logoutDialog.show();
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
    }

    @Override
    public void onBackPressed() {
        if (activeFragment.equals(itineraryActiveFragment)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_exit))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> AdminMainActivity.super.onBackPressed())
                    .create()
                    .show();
        }
        else {
            navView.setSelectedItemId(R.id.navigation_itineraries_active_admin);
        }
    }
}