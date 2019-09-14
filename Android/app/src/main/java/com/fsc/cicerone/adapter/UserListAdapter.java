package com.fsc.cicerone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.AdminUserProfile;
import com.fsc.cicerone.R;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;
import com.fsc.cicerone.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final Context context;
    private List<User> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener = null;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public UserListAdapter(Context context, List<User> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itineraryView = mInflater.inflate(R.layout.user_list, parent, false);
        return new ViewHolder(itineraryView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String usernameStr = mData.get(position).getUsername();
        UserType type = mData.get(position).getUserType();
        String typeName;
        holder.usr.setText(usernameStr);
        switch (type) {
            case GLOBETROTTER:
                typeName = context.getString(R.string.user_type_globetrotter);
                break;
            case CICERONE:
                typeName = context.getString(R.string.user_type_cicerone);
                break;
            case ADMIN:
                typeName = context.getString(R.string.user_type_admin);
                break;
            default:
                typeName = "";
                break;
        }
        holder.usrType.setText(typeName);
        setAvgRating(usernameStr, holder);

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            i = new Intent().setClass(v.getContext(), AdminUserProfile.class);
            i.putExtra("user", mData.get(position).toJSONObject().toString());
            v.getContext().startActivity(i);
        });

    }//END onBindViewHolder

    /**
     * Return the length of the JSON array passed into the ReviewAdapter.
     *
     * @return Length of JSON array.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //TODO: Add to class diagram

        TextView usr;
        TextView usrType;
        RatingBar avgRating;

        ViewHolder(View itemView) {
            super(itemView);
            usr = itemView.findViewById(R.id.username);
            usrType = itemView.findViewById(R.id.usrType);
            avgRating = itemView.findViewById(R.id.ratingBar2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    /**
     * Item Click Listener for parent activity will implement this method to respond to click events.
     */
    interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void setAvgRating(String usr, ViewHolder holder) {
        SendInPostConnector<UserReview> connector = new SendInPostConnector<>(
                ConnectorConstants.REQUEST_USER_REVIEW,
                BusinessEntityBuilder.getFactory(UserReview.class),
                new DatabaseConnector.CallbackInterface<UserReview>() {
                    @Override
                    public void onStartConnection() {
                        //
                    }

                    @Override
                    public void onEndConnection(List<UserReview> list) {
                        int sum = 0;
                        for (UserReview review : list) {
                            sum += review.getFeedback();
                        }
                        holder.avgRating.setRating((!list.isEmpty()) ? ((float) sum / list.size()) : 0);
                    }
                });
        try {
            JSONObject params = new JSONObject();
            params.put("reviewed_user", usr);
            connector.setObjectToSend(params);
            connector.execute();
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }
}


