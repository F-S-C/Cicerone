package com.fsc.cicerone.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * The adapter useful to show data in the admin part of the application.
 */
public class AdminItineraryAdapter extends RecyclerView.Adapter<AdminItineraryAdapter.ViewHolder> {

    private final Activity context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public AdminItineraryAdapter(Activity context, List<Itinerary> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
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
        String title = mData.get(position).getTitle();
        Integer itineraryNumber = mData.get(position).getCode();
        String location = mData.get(position).getLocation();

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        holder.beginning.setText(outputFormat.format(mData.get(position).getBeginningDate()));
        holder.ending.setText(outputFormat.format(mData.get(position).getEndingDate()));
        holder.itineraryTitle.setText(title);
        holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
        holder.location.setText(location);
        setItineraryAvgPrice(itineraryNumber, holder.avgItineraryPrice);
    }

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //TODO: Add in class diagram

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
            itineraryNumber = itemView.findViewById(R.id.itinerary_cicerone);
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
     *
     * @param itineraryCode The itinerary code.
     * @param t             The TextView which shows the average earnings.
     */
    private void setItineraryAvgPrice(Integer itineraryCode, TextView t) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("booked_itinerary", itineraryCode);
        SendInPostConnector<Reservation> conn = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    int count = 0;
                    float price = 0;
                    for (Reservation reservation : list) {
                        if (reservation.isConfirmed()) {
                            price += reservation.getTotal();
                            count++;
                        }
                    }
                    t.setText(context.getString(R.string.itinerary_earn, (count > 0) ? price / count : 0));
                })
                .setObjectToSend(params)
                .build();
        conn.execute();
    }
}

