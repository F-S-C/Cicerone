package com.fsc.cicerone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Adapter of the Recycler View for the styles present in the app.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;
    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Drawable reportCanceledImage, reportClosedImage, reportInProgressImage, reportOpenImage;
    private ViewType type;

    private enum ViewType {
        REPORT_LIST,
        ITINERARY_LIST,
        REVIEWS_LIST;

        private static ViewType getValue(int i) {
            i = i % 3;
            switch (i) {
                case 0:
                    return ViewType.REPORT_LIST;
                case 1:
                    return ViewType.ITINERARY_LIST;
                case 2:
                    return ViewType.REVIEWS_LIST;
                default:
                    return null;
            }
        }
    }


    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param jsonArray  The array of JSON Objects got from server.
     * @param typeNumber The code that indicates the type of style to use.
     */
    Adapter(Context context, JSONArray jsonArray, int typeNumber) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = jsonArray;
        this.type = ViewType.getValue(typeNumber);
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (type) {
            case REPORT_LIST:
                View reportView = mInflater.inflate(R.layout.report_list, parent, false);
                reportCanceledImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_canceled, null);
                reportClosedImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_closed, null);
                reportInProgressImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_in_progress, null);
                reportOpenImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_open, null);
                return new ViewHolder(reportView);

            case ITINERARY_LIST:
                View itineraryView = mInflater.inflate(R.layout.itinerary_list, parent, false);
                return new ViewHolder(itineraryView);

            case REVIEWS_LIST:
                View reviewsView = mInflater.inflate(R.layout.review_list, parent, false);
                return new ViewHolder(reviewsView);

            default:
                return new ViewHolder(null);
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (type) {
            case REPORT_LIST:
                try {
                    String object = mData.getJSONObject(position).getString("object");
                    String code = "Nr." + mData.getJSONObject(position).getString("report_code");
                    switch (ReportStatus.getValue(mData.getJSONObject(position).getInt("state"))) {
                        case OPEN:
                            holder.status.setImageDrawable(reportOpenImage);
                            break;
                        case CLOSED:
                            holder.status.setImageDrawable(reportClosedImage);
                            break;
                        case PENDING:
                            holder.status.setImageDrawable(reportInProgressImage);
                            break;
                        case CANCELED:
                            holder.status.setImageDrawable(reportCanceledImage);
                            break;
                        default:
                            break;
                    }
                    holder.object.setText(object);
                    holder.report_code.setText(code);
                } catch (JSONException e) {
                    Log.e("EXCEPTION", e.getMessage());
                }
                break; //END REPORT_LIST type

            case ITINERARY_LIST:
                try {
                    String title = mData.getJSONObject(position).getString("title");
                    Integer itinerary_number = mData.getJSONObject(position).getInt("itinerary_code");
                    String location = mData.getJSONObject(position).getString("location");
                    try {
                        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = inputFormat.parse(mData.getJSONObject(position).getString("beginning_date"));
                        holder.beginning.setText(outputFormat.format(date));
                        date = inputFormat.parse(mData.getJSONObject(position).getString("ending_date"));
                        holder.ending.setText(outputFormat.format(date));
                    } catch (ParseException e) {
                        Log.e("EXCEPTION", e.toString());
                    }
                    holder.itinerary_title.setText(title);
                    holder.itinerary_number.setText(String.format(context.getString(R.string.print_integer_number), itinerary_number));
                    holder.location.setText(location);

                } catch (JSONException e) {
                    Log.e("EXCEPTION", e.getMessage());
                }
                break; //END ITINERARY_LIST type

            case REVIEWS_LIST:
                try {
                    String description = mData.getJSONObject(position).getString("description");
                    String reviewer_username = mData.getJSONObject(position).getString("username");
                    int feedback = mData.getJSONObject(position).getInt("feedback");
                    holder.ratingBar.setRating(feedback);
                    holder.reviewer_username.setText(reviewer_username);
                    holder.review_description.setText(description);
                    //TODO Reviewer profile image
                } catch (JSONException e) {
                    Log.e("EXCEPTION", e.toString());
                }
                break; //END REVIEWS_LIST type
        } //END SWITCH
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
        //Defining variables of REPORT_LIST view
        TextView object, report_code;
        ImageView status;
        //Defining variables of ITINERARY_LIST view
        TextView itinerary_title, itinerary_number, location, beginning, ending;
        //Defining variables of REVIEWS_LIST view
        TextView reviewer_username, review_description;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);

            switch (type) {
                case REPORT_LIST:
                    object = itemView.findViewById(R.id.object);
                    report_code = itemView.findViewById(R.id.report_code);
                    status = itemView.findViewById(R.id.report_status);
                    break;

                case ITINERARY_LIST:
                    itinerary_title = itemView.findViewById(R.id.itinerary_title);
                    itinerary_number = itemView.findViewById(R.id.itinerary_number);
                    location = itemView.findViewById(R.id.location);
                    beginning = itemView.findViewById(R.id.beginning);
                    ending = itemView.findViewById(R.id.ending);
                    break;

                case REVIEWS_LIST:
                    reviewer_username = itemView.findViewById(R.id.reviewer_username);
                    ratingBar = itemView.findViewById(R.id.ratingBar);
                    review_description = itemView.findViewById(R.id.review_description);
                    break;
                default:
                    break;
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    //TODO Getting data on click IF-35
    /*JSONObject getItem(int id) {
        try {
            return mData.getJSONObject(id);
        }catch(JSONException e) {
            Log.e("EXCEPTION", e.getMessage());
        }
    }*/

    // TODO IF-35
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Item Click Listener for parent activity will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}