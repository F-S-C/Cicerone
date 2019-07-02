package com.fsc.cicerone;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        fragment = new ReviewFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.tabs);

        final JSONObject params;
        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            //Extract the data…
            params = new JSONObject();
            params.put("username", Objects.requireNonNull(bundle).getString("username"));
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
                        fragment = new ReviewFragment();
                        break;
                    case 1:
                        //TODO inserire il fragment relativo all'inserimento della recisione da parte dell'utente loggato al momento. (IF-24,IF-23)
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
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                TextView name = findViewById(R.id.name_profile);
                name.setText(result.getString("name"));
                TextView surname = findViewById(R.id.surname_profile);
                surname.setText(result.getString("surname"));
                TextView email = findViewById(R.id.email_profile);
                email.setText(result.getString("email"));
                TextView userType = findViewById(R.id.user_type_profile);
                if (Objects.requireNonNull(UserType.getValue(result.getInt("user_type"))) == UserType.CICERONE) {
                    userType.setText(R.string.user_type_cicerone);
                } else {
                    userType.setText(R.string.user_type_globetrotter);
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();

        SendInPostConnector connectorReview = new SendInPostConnector(ConnectorConstants.USER_REVIEW, new DatabaseConnector.CallbackInterface() {
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