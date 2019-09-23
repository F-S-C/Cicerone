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

package app_connector;

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
 * Generic connector to the database.
 * This connector sends data using POST to a server-side script that will be used in order to
 * connect to a database.
 */
public class SendInPostConnector<T extends BusinessEntity> extends DatabaseConnector<T> {

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

        final String USERNAME_KEY = "username";
        final String REPORTED_USER_KEY = "reported_user";
        final String REVIEWED_USER_KEY = "reviewed_user";
        final String REVIEWED_ITINERARY_KEY = "reviewed_itinerary";
        final String BOOKED_ITINERARY_KEY = "booked_itinerary";
        final String ITINERARY_IN_WISHLIST_KEY = "itinerary_in_wishlist";

        if (entity instanceof Itinerary) {
            mapData.put(USERNAME_KEY, ((Itinerary) entity).getCicerone().getUsername());
        } else if (entity instanceof Report) {
            mapData.put(USERNAME_KEY, ((Report) entity).getAuthor().getUsername());
            mapData.put(REPORTED_USER_KEY, ((Report) entity).getReportedUser().getUsername());
        } else if (entity instanceof Review) {
            mapData.put(USERNAME_KEY, ((Review) entity).getAuthor().getUsername());
            if (entity instanceof ItineraryReview) {
                mapData.put(REVIEWED_ITINERARY_KEY, ((ItineraryReview) entity).getReviewedItinerary().getCode());
            } else if (entity instanceof UserReview) {
                mapData.put(REVIEWED_USER_KEY, ((UserReview) entity).getReviewedUser().getUsername());
            }
        } else if (entity instanceof Reservation) {
            mapData.put(BOOKED_ITINERARY_KEY, ((Reservation) entity).getItinerary().getCode());
            mapData.put(USERNAME_KEY, ((Reservation) entity).getClient().getUsername());
        } else if (entity instanceof Wishlist) {
            mapData.put(USERNAME_KEY, ((Wishlist) entity).getUser().getUsername());
            mapData.put(ITINERARY_IN_WISHLIST_KEY, ((Wishlist) entity).getItinerary().getCode());
        }

        return mapData;
    }
    public static Map<String, Object> paramsFromObject(final JSONObject jsonObject) {
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

        return mapData;
    }

    protected SendInPostConnector(Builder<T> builder) {
        super(builder);
        this.objectToSend = builder.objectToSend;
    }

    public static class Builder<B extends BusinessEntity> extends DatabaseConnector.Builder<B> {
        private Map<String, Object> objectToSend;

        public Builder(String url, BusinessEntityBuilder<B> builder) {
            super(url, builder);
        }

        @Override
        public Builder<B> setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder<B>) super.setOnStartConnectionListener(listener);
        }

        @Override
        public Builder<B> setOnEndConnectionListener(OnEndConnectionListener<B> listener) {
            return (Builder<B>) super.setOnEndConnectionListener(listener);
        }

        @Override
        public Builder<B> setContext(Activity context) {
            return (Builder<B>) super.setContext(context);
        }

        public Builder<B> setObjectToSend(Map<String, Object> objectToSend) {
            this.objectToSend = objectToSend;
            return this;
        }

        @Override
        public SendInPostConnector<B> build() {
            return new SendInPostConnector<>(this);
        }
    }
}
