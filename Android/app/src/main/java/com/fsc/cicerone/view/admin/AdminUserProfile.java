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

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.user.registered_user.globetrotter.GlobetrotterItineraryListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

/**
 * Class that specifying the details of registered user that the administrator is viewing.
 */
public class AdminUserProfile extends AppCompatActivity {

    private Fragment fragment = null;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        fragment = new AdminDetailsUserFragment();
        supportActionBar.setTitle(getString(R.string.profile));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_admin, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        TabLayout tabLayout = findViewById(R.id.admin_tabs);

        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);

        User user = new User(Objects.requireNonNull(bundle).getString("user"));

        TextView username = findViewById(R.id.admin_username_profile);

        ImageView imageView = findViewById(R.id.avatar);
        imageView.setImageResource(user.getSex().getAvatarResource());
        String nick = "@" + user.getUsername();
        username.setText(nick);
        String nameSurname = user.getName() + " " + user.getSurname();
        TextView nameSurnameTextView = findViewById(R.id.admin_name_surname_profile);
        nameSurnameTextView.setText(nameSurname);
        if (user.getUserType().equals(UserType.GLOBETROTTER)) {
            tabLayout.removeTabAt(2);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * Called when a tab enters the selected state.
             *
             * @param tab The tab that was selected
             */
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
                        fragment = new GlobetrotterItineraryListFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        supportActionBar.setTitle(getString(R.string.created_itineraries));
                        fragment = new CiceroneItineraryListFragment();
                        fragment.setArguments(bundle);
                        break;
                    default:
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_admin, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            /**
             * Called when a tab exits the selected state.
             *
             * @param tab The tab that was unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications may
             * use this action to return to the top level of a category.
             *
             * @param tab The tab that was reselected.
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }

        });

    }

    /**
     * @see AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}