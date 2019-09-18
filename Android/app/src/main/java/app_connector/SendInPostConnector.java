package app_connector;

import android.content.Context;
import android.util.Log;

import com.fsc.cicerone.model.BusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;

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
        if (list == null)
            return "";
        else if (list.isEmpty())
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

    public static Map<String, Object> paramsFromJSONObject(final JSONObject jsonObject) {
        Map<String, Object> mapData = new HashMap<>(jsonObject.length());
        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            try {
                String key = keysItr.next();
                Object value = jsonObject.get(key);

                if (value instanceof JSONObject) {
                    value = paramsFromJSONObject((JSONObject) value);
                }

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

    public static class Builder<BuilderType extends BusinessEntity> extends DatabaseConnector.Builder<BuilderType> {
        private Map<String, Object> objectToSend;

        public Builder(String url, BusinessEntityBuilder<BuilderType> builder) {
            super(url, builder);
        }

        @Override
        public Builder<BuilderType> setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder<BuilderType>) super.setOnStartConnectionListener(listener);
        }

        @Override
        public Builder<BuilderType> setOnEndConnectionListener(OnEndConnectionListener<BuilderType> listener) {
            return (Builder<BuilderType>) super.setOnEndConnectionListener(listener);
        }

        @Override
        public Builder<BuilderType> setContext(Context context) {
            return (Builder<BuilderType>) super.setContext(context);
        }

        public Builder<BuilderType> setObjectToSend(Map<String, Object> objectToSend) {
            this.objectToSend = objectToSend;
            return this;
        }

        @Override
        public SendInPostConnector<BuilderType> build() {
            return new SendInPostConnector<>(this);
        }
    }
}
