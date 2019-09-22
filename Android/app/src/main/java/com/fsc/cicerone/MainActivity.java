package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.manager.AccountManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
     * The wishlist fragment (third tab).
     */
    private WishlistFragment wishlistFragment;

    /**
     * The profile fragment (fourth tab).
     */
    private Fragment profileFragment;

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
        navView.getMenu().removeItem(AccountManager.isLogged() ? R.id.navigation_login : R.id.navigation_profile);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_activity_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (activeFragment instanceof Refreshable) {
                ((Refreshable) activeFragment).refresh(swipeRefreshLayout);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        profileFragment = AccountManager.isLogged() ? new AccountDetails() : null;
        wishlistFragment = AccountManager.isLogged() ? new WishlistFragment() : null;

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
                        wishlistFragment.refresh();
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
                    changeCurrentFragment(profileFragment);
                    supportActionBar.setTitle(getString(R.string.account));
                    toReturn = true;
                    break;
                case R.id.navigation_login:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
}
