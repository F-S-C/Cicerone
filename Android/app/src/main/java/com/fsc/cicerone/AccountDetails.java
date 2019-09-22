package com.fsc.cicerone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

/**
 * Class that specifying the account detail page
 */
public class AccountDetails extends Fragment {

    private TabLayout tabLayout;
    private Fragment fragment = null;

    private View holderView;


    public AccountDetails() {
        // required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        holderView = inflater.inflate(R.layout.activity_account_details, container, false);

        fragment = new ProfileFragment();
        FragmentManager fragmentManager = Objects.requireNonNull(getFragmentManager());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = holderView.findViewById(R.id.tabs);

        SwipeRefreshLayout swipeRefreshLayout = holderView.findViewById(R.id.account_details_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (fragment instanceof ReportFragment) {
                ((ReportFragment) fragment).requireData(swipeRefreshLayout);
            } else if (fragment instanceof ReviewFragment) {
                ((ReviewFragment) fragment).requireData(swipeRefreshLayout);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /* Set TextView username */
        TextView usernameText = holderView.findViewById(R.id.username);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        String username = "@" + currentLoggedUser.getUsername();
        usernameText.setText(username);
        setNameSurname(currentLoggedUser); //Require data from server and set Name Surname

        /* TabLayout */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ProfileFragment();
                        break;
                    case 1:
                        fragment = new ReportFragment();
                        break;
                    case 2:
                        fragment = new ReviewFragment();
                        break;
                    case 3:
                        fragment = new ItineraryFragment();
                        break;
                    case 4:
                        fragment = new ReservationFragment();
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

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }

        });

        return holderView;
    }

    private void setNameSurname(User currentLoggedUser) {
        String nameSurname = currentLoggedUser.getName() + " " + currentLoggedUser.getSurname();
        TextView nameSurnameTextView = holderView.findViewById(R.id.name_surname);
        nameSurnameTextView.setText(nameSurname);
        if (currentLoggedUser.getUserType() == UserType.GLOBETROTTER) {
            tabLayout.removeTabAt(4);
        }
    }
}
