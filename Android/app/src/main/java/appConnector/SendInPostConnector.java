package appConnector;

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
            StringBuilder stringBuilder = new StringBuilder();

            for (Iterator<String> i = objectToSend.keys(); i.hasNext(); ) {
                try {
                    if (!stringBuilder.toString().equals(""))
                        stringBuilder.append("&");
                    String key = i.next();
                    stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(objectToSend.getString(key), "UTF-8"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write(stringBuilder.toString());
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
            e.printStackTrace();
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
}
