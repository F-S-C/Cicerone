package app_connector;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Generic connector to the database.
 * This connector sends data using POST to a server-side script that will be used in order to
 * connect to a database.
 */
public abstract class SendInPostConnector extends DatabaseConnector {

    private JSONObject objectToSend; // The object that will be sent

    /**
     * Default constructor.
     */
    public SendInPostConnector() {
        super();
        objectToSend = null;
    }

    /**
     * Constructor.
     *
     * @param url The url of the server-side script.
     */
    public SendInPostConnector(String url) {
        super(url);
        objectToSend = null;
    }

    /**
     * Constructor.
     *
     * @param url      The url of the server-side script.
     * @param callback A reference to CallbackInterface that will be used before and after the
     *                 connection.
     */
    public SendInPostConnector(String url, CallbackInterface callback) {
        super(url, callback);
        objectToSend = null;
    }

    /**
     * Constructor
     *
     * @param url          The url of the server-side script.
     * @param callback     A reference to CallbackInterface that will be used before and after the
     *                     connection.
     * @param objectToSend A JSON Object containing the data that will be sent.
     */
    public SendInPostConnector(String url, CallbackInterface callback, JSONObject objectToSend) {
        super(url, callback);
        this.objectToSend = objectToSend;
    }

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
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
            Log.e("EXCEPTION", e.toString());
            return null;
        }
    }

    /**
     * Set the object that will be searched.
     *
     * @param objectToSend The new object that will be sent.
     */
    public void setObjectToSend(JSONObject objectToSend) {
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
     * @param object The JSON Object that has to be converted.
     * @return A string containing the URL query.
     */
    private String urlQueryFromJSONObject(JSONObject object) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            for (Iterator<String> i = object.keys(); i.hasNext(); ) {
                if (!stringBuilder.toString().equals(""))
                    stringBuilder.append("&");
                String key = i.next();
                stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(object.getString(key), "UTF-8"));
            }
        } catch (JSONException | UnsupportedEncodingException e) {
            Log.e("EXCEPTION", e.toString());
        }

        return stringBuilder.toString();
    }
}
