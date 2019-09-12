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
import com.fsc.cicerone.User;
import com.fsc.cicerone.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * The Adapter of the Recycler View for the styles present in the app.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + UserListAdapter.class.getName();

    private final Context context;
    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener = null;

    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param jsonArray  The array of JSON Objects got from server.
     */
    public UserListAdapter(Context context, JSONArray jsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = jsonArray;
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
        User[] userList = new User[mData.length()];


        try {
            userList[position] = new User(mData.getJSONObject(position));
            String usernameStr = userList[position].getUsername();
            UserType type = userList[position].getUserType();
            String typeName;
            holder.usr.setText(usernameStr);
            switch (type){
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
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            i = new Intent().setClass(v.getContext(), AdminUserProfile.class);
            i.putExtra("user",userList[position].toJSONObject().toString());
            v.getContext().startActivity(i);
        });

    }//END onBindViewHolder

    /**
     * Return the length of the JSON array passed into the Adapter.
     *
     * @return Length of JSON array.
     */
    @Override
    public int getItemCount() {
        return mData.length();
    }

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_USER_REVIEW, new DatabaseConnector.CallbackInterface() {
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
                holder.avgRating.setRating((i > 0) ? ((float) sum / i) : 0);
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


