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

package com.fsc.cicerone.view.user.registered_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

/**
 * Class that represents the account details page.
 */
public class AccountDetailsFragment extends Fragment implements Refreshable {

    private TabLayout tabLayout;
    private ProfileFragment profileFragment;
    private ReportFragment reportFragment;
    private ReviewFragment reviewFragment;
    private ItineraryFragment itineraryFragment;
    private ReservationFragment reservationFragment;
    private Fragment fragment = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    private View holderView;


    /**
     * Empty Constructor.
     */
    public AccountDetailsFragment() {
        // required empty constructor
    }

    /**
     * A constructor that takes a SwipeRefreshLayout as a parameter.
     *
     * @param swipeRefreshLayout The SwipeRefreshLayout to set
     */
    public AccountDetailsFragment(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * @see androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        holderView = inflater.inflate(R.layout.fragment_account_details, container, false);

        profileFragment = new ProfileFragment();
        reportFragment = new ReportFragment(swipeRefreshLayout);
        reviewFragment = new ReviewFragment(swipeRefreshLayout);
        itineraryFragment = new ItineraryFragment();
        reservationFragment = AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE ? new ReservationFragment(swipeRefreshLayout) : null;

        fragment = profileFragment;
        FragmentManager fragmentManager = Objects.requireNonNull(getFragmentManager());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = holderView.findViewById(R.id.tabs);

        /* Set TextView username */
        TextView usernameText = holderView.findViewById(R.id.username);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        String username = "@" + currentLoggedUser.getUsername();
        usernameText.setText(username);
        setNameSurname(currentLoggedUser); //Require data from server and set Name Surname

        ImageView imageView = holderView.findViewById(R.id.avatar);
        imageView.setImageResource(currentLoggedUser.getSex().getAvatarResource());

        /* TabLayout */
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
                        fragment = profileFragment;
                        break;
                    case 1:
                        fragment = reportFragment;
                        break;
                    case 2:
                        fragment = reviewFragment;
                        break;
                    case 3:
                        fragment = itineraryFragment;
                        break;
                    case 4:
                        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE)
                            fragment = reservationFragment;
                        break;
                    default:
                        break;
                }
                FragmentManager fm = Objects.requireNonNull(getFragmentManager());
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, fragment);
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

        return holderView;
    }

    /**
     * A function that manage to set Name and Surname of the logged User in the Fragment.
     *
     * @param currentLoggedUser The logged User.
     */
    private void setNameSurname(User currentLoggedUser) {
        String nameSurname = currentLoggedUser.getName() + " " + currentLoggedUser.getSurname();
        TextView nameSurnameTextView = holderView.findViewById(R.id.name_surname);
        nameSurnameTextView.setText(nameSurname);
        if (currentLoggedUser.getUserType() == UserType.GLOBETROTTER) {
            tabLayout.removeTabAt(4);
        }
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh()
     */
    @Override
    public void refresh() {
        refresh(swipeRefreshLayout);
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh(SwipeRefreshLayout)
     */
    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        ImageView imageView = holderView.findViewById(R.id.avatar);
        imageView.setImageResource(AccountManager.getCurrentLoggedUser().getSex().getAvatarResource());

        if (fragment instanceof Refreshable) {
            ((Refreshable) fragment).refresh(swipeRefreshLayout);
        } else {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    }


}
