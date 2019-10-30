/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.app_connector;

import android.app.Activity;
import android.util.Log;

import com.fsc.cicerone.model.BusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.Review;
import com.fsc.cicerone.model.UserReview;
import com.fsc.cicerone.model.Wishlist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Generic connector to the database. This connector sends data using POST to a server-side script
 * that will be used in order to connect to a database and reads its results.
 */
public class SendInPostConnector<T extends BusinessEntity> extends AsyncDatabaseConnector<T> {

    //    private JSONObject objectToSend; // The object that will be sent
    private Map<String, Object> objectToSend;

    /**
     * This function send the data to the server-side script and fetches its response.
     *
     * @param voids Various parameters
     * @return The string that contains the result of the connection.
     */
    @Override
    protected String doInBackground(Void... voids) {
        super.doInBackground(voids);
        try {
            URL url = new URL(fileUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            // Send data
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            String query = urlQueryFromJSONObject(objectToSend);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write(query);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            // Get results
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder resultBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append("\n");
            }

            return resultBuilder.toString();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e("SEND_IN_POST_ERROR", sw.toString());
            setError(e);
            return null;
        }
    }

    /**
     * Set the object that will be searched.
     *
     * @param objectToSend The new object that will be sent.
     */
    public void setObjectToSend(Map<String, Object> objectToSend) {
        this.objectToSend = objectToSend;
    }

    /**
     * Convert a JSON Object to a URL query. As an example, the following JSON Object:
     * <pre>
     * {
     *     "attribute1": "value1",
     *     "attribute2": "value2"
     * }
     * </pre>
     * Will be translated in <code>attribute1=value1&attribute2=value2</code>.
     *
     * @param list The JSON Object that has to be converted.
     * @return A string containing the URL query.
     */
    private String urlQueryFromJSONObject(Map<String, Object> list) {
        if (list == null || list.isEmpty())
            return "";

        StringBuilder stringBuilder = new StringBuilder();

        try {
            for (Map.Entry<String, Object> entry : list.entrySet()) {
                if (!stringBuilder.toString().equals(""))
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            Log.e("EXCEPTION", e.toString());
        }

        return stringBuilder.toString();
    }

    /**
     * Return a Map from the result returned by the remote connector.
     *
     * @param entity The BusinessEntity object to be converted.
     * @return The Map.
     */
    public static Map<String, Object> paramsFromObject(final BusinessEntity entity) {
        JSONObject jsonObject = entity.toJSONObject();
        Map<String, Object> mapData = new HashMap<>(jsonObject.length());
        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            try {
                String key = keysItr.next();
                Object value = jsonObject.get(key);

                mapData.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (entity instanceof Itinerary) {
            mapData.put(Itinerary.Columns.CICERONE_KEY, ((Itinerary) entity).getCicerone().getUsername());
        } else if (entity instanceof Report) {
            mapData.put(Report.Columns.AUTHOR_KEY, ((Report) entity).getAuthor().getUsername());
            mapData.put(Report.Columns.REPORTED_USER_KEY, ((Report) entity).getReportedUser().getUsername());
        } else if (entity instanceof Review) {
            mapData.put(Review.Columns.AUTHOR_KEY, ((Review) entity).getAuthor().getUsername());
            if (entity instanceof ItineraryReview) {
                mapData.put(ItineraryReview.Columns.REVIEWED_ITINERARY_KEY, ((ItineraryReview) entity).getReviewedItinerary().getCode());
            } else if (entity instanceof UserReview) {
                mapData.put(UserReview.Columns.REVIEWED_USER_KEY, ((UserReview) entity).getReviewedUser().getUsername());
            }
        } else if (entity instanceof Reservation) {
            mapData.put(Reservation.Columns.BOOKED_ITINERARY_KEY, ((Reservation) entity).getItinerary().getCode());
            mapData.put(Reservation.Columns.CLIENT_KEY, ((Reservation) entity).getClient().getUsername());
        } else if (entity instanceof Wishlist) {
            mapData.put(Wishlist.Columns.OWNER_KEY, ((Wishlist) entity).getUser().getUsername());
            mapData.put(Wishlist.Columns.ITINERARY_IN_WISHLIST_KEY, ((Wishlist) entity).getItinerary().getCode());
        }

        return mapData;
    }

    /**
     * SendInPostConnector's constructor. Uses the AsyncDatabaseConnector's constructor.
     *
     * @param builder The Builder.
     */
    protected SendInPostConnector(Builder<T> builder) {
        super(builder);
        this.objectToSend = builder.objectToSend;
    }

    /**
     * A utility class useful to create a SendInPostConnector.
     *
     * @param <B> The object to be sent to the remote connector.
     */
    public static class Builder<B extends BusinessEntity> extends AsyncDatabaseConnector.Builder<B> {
        private Map<String, Object> objectToSend;

        /**
         * Builder's constructor.
         *
         * @param url     The url of the remote connector on the server.
         * @param builder A factory for a BusinessEntity's object.
         */
        public Builder(String url, BusinessEntityBuilder<B> builder) {
            super(url, builder);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setOnStartConnectionListener(AsyncDatabaseConnector.OnStartConnectionListener)
         */
        @Override
        public Builder<B> setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder<B>) super.setOnStartConnectionListener(listener);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setOnEndConnectionListener(AsyncDatabaseConnector.OnEndConnectionListener)
         */
        @Override
        public Builder<B> setOnEndConnectionListener(OnEndConnectionListener<B> listener) {
            return (Builder<B>) super.setOnEndConnectionListener(listener);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setContext(Activity)
         */
        @Override
        public Builder<B> setContext(Activity context) {
            return (Builder<B>) super.setContext(context);
        }

        /**
         * Set the object to be sent to the remote connector.
         *
         * @param objectToSend Set the object to be sent to the remote connector. The map's entries
         *                     will be used in the form key = value.
         * @return The Builder itself.
         */
        public Builder<B> setObjectToSend(Map<String, Object> objectToSend) {
            this.objectToSend = objectToSend;
            return this;
        }

        /**
         * @see AsyncDatabaseConnector.Builder#build()
         */
        @Override
        public SendInPostConnector<B> build() {
            return new SendInPostConnector<>(this);
        }
    }
}
