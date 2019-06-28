package com.fsc.cicerone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that specifying the account detail page
 */
public class AccountDetails extends AppCompatActivity {

    private static final String ERROR_TAG = "ERROR IN " + AccountDetails.class.getName();

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        frameLayout = findViewById(R.id.frame);
        fragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.tabs);

        /* Set TextView username */
        SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        TextView usernameText = findViewById(R.id.username);
        try {
            final JSONObject parameters = new JSONObject(preferences.getString("session", ""));
            parameters.remove("password");
            String username = "@" + parameters.getString("username");
            usernameText.setText(username);
            setNameSurname(parameters); //Require data from server and set Name Surname
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }

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

    private void setNameSurname(JSONObject parameters) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                String nameSurname = result.getString("name") + " " + result.getString("surname");
                TextView nameSurnameTextView = findViewById(R.id.name_surname);
                nameSurnameTextView.setText(nameSurname);
                if(result.getInt("user_type") == 0)
                    tabLayout.removeTabAt(3);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }
}
