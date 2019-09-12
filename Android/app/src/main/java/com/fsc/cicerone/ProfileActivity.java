package com.fsc.cicerone;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * ProfileActivity is the class that allows you to build a user's profile activity.
 **/
public class ProfileActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


        final JSONObject params;
        try {
            //Get the bundle
            //Extract the data…
            params = new JSONObject();
            params.put("username", Objects.requireNonNull(bundle).getString("reviewed_user"));
            Log.e("bundle", Objects.requireNonNull(bundle).getString("reviewed_user"));
            TextView username = findViewById(R.id.username_profile);
            String nick = "@" + params.getString("username");
            username.setText(nick);
            getData(params);
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new SelectedUserReviewFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 1:
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

    private void getData(JSONObject parameters) {
        SendInPostConnector<User> connector = new SendInPostConnector<>(
                ConnectorConstants.REGISTERED_USER,
                BusinessEntityBuilder.getFactory(User.class),
                new DatabaseConnector.CallbackInterface<User>() {
                    @Override
                    public void onStartConnection() {
                        //
                    }

                    @Override
                    public void onEndConnection(List<User> list) throws JSONException {
                        User result = list.get(0);
                        TextView name = findViewById(R.id.name_profile);
                        name.setText(result.getName());
                        TextView surname = findViewById(R.id.surname_profile);
                        surname.setText(result.getSurname());
                        TextView email = findViewById(R.id.email_profile);
                        email.setText(result.getEmail());
                        TextView userType = findViewById(R.id.user_type_profile);
                        if (result.getUserType() == UserType.CICERONE) {
                            userType.setText(R.string.user_type_cicerone);
                        } else {
                            userType.setText(R.string.user_type_globetrotter);
                        }
                    }
                },
                parameters);
        connector.execute();

        //TODO: Add review class
        SendInPostConnector connectorReview = new SendInPostConnector(ConnectorConstants.REQUEST_USER_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                int i;
                int sum = 0;
                JSONObject result;
                for (i = 0; i < jsonArray.length(); i++) {
                    result = jsonArray.getJSONObject(i);
                    sum += result.getInt("feedback");
                }
                RatingBar star = findViewById(R.id.avg_feedback);
                star.setRating((i > 0) ? ((float) sum / i) : 0);
            }
        });
        try {
            JSONObject newParameter = new JSONObject();
            newParameter.put("reviewed_user", parameters.getString("username"));
            connectorReview.setObjectToSend(newParameter);
            connectorReview.execute();
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }
}