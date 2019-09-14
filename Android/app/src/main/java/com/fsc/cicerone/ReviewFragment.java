package com.fsc.cicerone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Review on the account details page.
 */
public class ReviewFragment extends Fragment {

    RecyclerView.Adapter adapter;

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

    /**
     * Empty Constructor
     */
    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_review_fragment, container, false);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        try {
            final JSONObject parameters = new JSONObject();
            parameters.put("reviewed_user", currentLoggedUser.getUsername());
            // set up the RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.review_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            requireData(view, parameters, recyclerView);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }

        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        TextView message = view.findViewById(R.id.noReview);
        SendInPostConnector<UserReview> connector = new SendInPostConnector<>(
                ConnectorConstants.REQUEST_USER_REVIEW,
                BusinessEntityBuilder.getFactory(UserReview.class),
                new DatabaseConnector.CallbackInterface<UserReview>() {
                    @Override
                    public void onStartConnection() {
                        progressBar.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEndConnection(List<UserReview> list) {
                        progressBar.setVisibility(View.GONE);
                        if (!list.isEmpty()) {
                            adapter = new ReviewAdapter(getActivity(), list);
                            recyclerView.setAdapter(adapter);
                        } else {
                            message.setVisibility(View.VISIBLE);
                        }
                    }
                },
                parameters);
        connector.execute();
    }

}
