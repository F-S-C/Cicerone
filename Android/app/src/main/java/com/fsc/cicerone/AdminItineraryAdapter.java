package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * The adapter useful to show data in the admin part of the application.
 */
public class AdminItineraryAdapter extends RecyclerView.Adapter<AdminItineraryAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + AdminItineraryAdapter.class.getName();

    private final Context context;
    private JSONArray mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param jsonArray  The array of JSON Objects got from server.
     */
    AdminItineraryAdapter(Context context, JSONArray jsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = jsonArray;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itineraryView = mInflater.inflate(R.layout.itinerary_admin_list, parent, false);
        return new ViewHolder(itineraryView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Itinerary[] itineraryList = new Itinerary[mData.length()];


        try {
            itineraryList[position] = new Itinerary(mData.getJSONObject(position));
            String title = itineraryList[position].getTitle();
            Integer itineraryNumber = itineraryList[position].getCode();
            String location = itineraryList[position].getLocation();

            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            holder.beginning.setText(outputFormat.format(itineraryList[position].getBeginningDate()));
            holder.ending.setText(outputFormat.format(itineraryList[position].getEndingDate()));
            holder.itineraryTitle.setText(title);
            holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
            holder.location.setText(location);
            setItineraryAvgPrice(itineraryNumber, holder.avgItineraryPrice);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
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

        //Defining variables of ITINERARY_LIST view
        TextView itineraryTitle;
        TextView itineraryNumber;
        TextView location;
        TextView beginning;
        TextView ending;
        TextView avgItineraryPrice;


        ViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = itemView.findViewById(R.id.itinerary_title);
            itineraryNumber = itemView.findViewById(R.id.itinerary_number);
            location = itemView.findViewById(R.id.location);
            beginning = itemView.findViewById(R.id.beginning);
            ending = itemView.findViewById(R.id.ending);
            avgItineraryPrice = itemView.findViewById(R.id.avg_itinerary_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Do nothing
        }
    }

    /**
     * Set the average earnings of an itinerary.
     * @param itineraryCode The itinerary code.
     * @param t The TextView which shows the average earnings.
     */
    private void setItineraryAvgPrice(Integer itineraryCode, TextView t){
        JSONObject params = new JSONObject();
        try{
            params.put("booked_itinerary",itineraryCode);
            SendInPostConnector conn = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    int count = 0;
                    float price = 0;
                    for(int i = 0; i < jsonArray.length(); i++) {
                        if(!jsonArray.getJSONObject(i).isNull("confirm_date")) {
                            if (!jsonArray.getJSONObject(i).getString("confirm_date").equals("0000-00-00")) {
                                price += Float.valueOf(jsonArray.getJSONObject(i).getString("total"));
                                count++;
                            }
                        }
                    }
                    t.setText(context.getString(R.string.itinerary_earn, (count < 1) ? 0 : price/count));
                }
            },params);
            conn.execute();
        }catch (JSONException e){
            Log.e(ERROR_TAG,e.toString());
        }
    }
}
