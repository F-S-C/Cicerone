package com.fsc.cicerone;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ItineraryReviewFragment extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    TextView title;
    TextView author;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_review_fragment);

        frameLayout = findViewById(R.id.frame);
        fragment = new SelectedItineraryReviewFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.tabs);

        title = findViewById(R.id.it_title);
        rating = findViewById(R.id.it_feedback);
        author = findViewById(R.id.author);

        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);


        if (bundle != null) {
            try {
                Itinerary itinerary = new Itinerary(new JSONObject(bundle.getString("itinerary")));
                bundle.putString("reviewed_itinerary", String.valueOf(itinerary.getCode()));
                title.setText(itinerary.getTitle());
                rating.setRating(bundle.getFloat("rating"));
                author.setText(itinerary.getUsername());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new SelectedItineraryReviewFragment();
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
}
