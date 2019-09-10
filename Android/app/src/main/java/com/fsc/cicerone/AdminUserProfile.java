package com.fsc.cicerone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Class that specifying the details of registered user that the administrator is viewing.
 */
public class AdminUserProfile extends AppCompatActivity {
    private static final String ERROR_TAG = "ERROR IN " + AdminUserProfile.class.getName();

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        frameLayout = findViewById(R.id.frame_admin);
        fragment = new AdminDetailsUserFragment();
        supportActionBar.setTitle(getString(R.string.profile));
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_admin,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.admin_tabs);

        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);

        User user = null;
        try {
            user = new User(new JSONObject((String) Objects.requireNonNull(Objects.requireNonNull(bundle).get("user"))));
        } catch (JSONException e) {
            Log.e(ERROR_TAG,e.getMessage());
        }

        TextView username = findViewById(R.id.admin_username_profile);
        assert user != null;
        String nick = "@" + user.getUsername();
        username.setText(nick);
        String nameSurname = user.getName() + " " + user.getSurname();
        TextView nameSurnameTextView = findViewById(R.id.admin_name_surname_profile);
        nameSurnameTextView.setText(nameSurname);
        if (user.getUserType().equals(UserType.GLOBETROTTER) ) {
            tabLayout.removeTabAt(2);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        supportActionBar.setTitle(getString(R.string.profile));
                        fragment = new AdminDetailsUserFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        supportActionBar.setTitle(getString(R.string.participation_itinerary));
                        fragment = new InsertReviewFragment(); //TODO storico globetrotter
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        supportActionBar.setTitle(getString(R.string.created_itineraries));
                        fragment = new CiceroneItineraryListFragment();
                        fragment.setArguments(bundle);
                    default:
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_admin, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }

        });

    }


}