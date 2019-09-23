package com.fsc.cicerone;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * ProfileActivity is the class that allows you to build a user's profile activity.
 **/
public class ProfileActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(R.string.reviews);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        frameLayout = findViewById(R.id.frame);
        fragment = new SelectedUserReviewFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.tabs);

        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);


        final Map<String, Object> params = new HashMap<>();
        //Get the bundle
        //Extract the data…
        params.put("username", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("reviewed_user")));
        TextView username = findViewById(R.id.username_profile);
        String nick = "@" + Objects.requireNonNull(params.get("username")).toString();
        username.setText(nick);
        getData(params);

        if(!AccountManager.isLogged())
            tabLayout.removeTabAt(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Objects.requireNonNull(actionBar).setTitle(R.string.reviews);
                        fragment = new SelectedUserReviewFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        Objects.requireNonNull(actionBar).setTitle(R.string.add_review);
                        fragment = new InsertReviewFragment();
                        fragment.setArguments(bundle);
                        break;
                    default:
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
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

    }

    private void getData(Map<String, Object> parameters) {
        SendInPostConnector<User> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    String string;
                    User result = list.get(0);
                    TextView name = findViewById(R.id.name_profile);
                    string= String.format("%s%s %s", getString(R.string.name), getString(R.string.separator), result.getName());
                    name.setText(string);
                    TextView surname = findViewById(R.id.surname_profile);
                    string = String.format("%s%s %s", getString(R.string.surname), getString(R.string.separator), result.getSurname());
                    surname.setText(string);
                    TextView email = findViewById(R.id.email_profile);
                    string = String.format("%s%s %s", getString(R.string.email), getString(R.string.separator), result.getEmail());
                    email.setText(string);
                    TextView userType = findViewById(R.id.user_type_profile);
                    if (result.getUserType() == UserType.CICERONE) {
                        userType.setText(R.string.user_type_cicerone);
                    } else {
                        userType.setText(R.string.user_type_globetrotter);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();

        Map<String, Object> newParameter = new HashMap<>(1);
        newParameter.put("reviewed_user", Objects.requireNonNull(parameters.get("username")).toString());
        SendInPostConnector<UserReview> connectorReview = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    int sum = 0;
                    for (UserReview review : list) {
                        sum += review.getFeedback();
                    }
                    RatingBar star = findViewById(R.id.avg_feedback);
                    star.setRating((!list.isEmpty()) ? ((float) sum / list.size()) : 0);
                })
                .setObjectToSend(newParameter)
                .build();
        connectorReview.execute();
    }
}