package app_connector;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Generic connector to a database.
 * This class gives an interface to communicate with a database. It needs the URL of the server-side
 * script that generates and handles the data.
 */
public abstract class DatabaseConnector extends AsyncTask<Void, Void, String> {

    /**
     * An interface to use before and after the connection.
     */
    public interface CallbackInterface {
        /**
         * Function that will be called before the start of the connection.
         */
        void onStartConnection();

        /**
         * Function that will be called when the connection has ended.
         *
         * @param jsonArray This array contains the results of the connection.
         * @throws JSONException If there are errors in the conversion of the results to JSON,
         * an exception is thrown.
         */
        void onEndConnection(JSONArray jsonArray) throws JSONException;
    }

    final String fileUrl; // The URL of the server-side script
    private CallbackInterface callback; // A reference to a CallbackInterface

    /**
     * Constructor.
     *
     * @param url The url of the server-side script.
     */
    DatabaseConnector(String url) {
        super();
        fileUrl = url;
        callback = new CallbackInterface() {
            @Override
            public void onEndConnection(JSONArray jsonArray) {
                // Do nothing by default
            }

            @Override
            public void onStartConnection() {
                // Do nothing by default
            }
        };
    }

    /**
     * Constructor.
     *
     * @param url      The url of the server-side script.
     * @param callback A reference to CallbackInterface that will be used before and after the
     *                 connection.
     */
    DatabaseConnector(String url, CallbackInterface callback) {
        super();
        fileUrl = url;
        this.callback = callback;
    }

    /**
     * A function that will be called before the execution.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onStartConnection();
    }

    /**
     * A function that will be called after the execution.
     *
     * @param s This string will contain the result of the connection.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            try {
                s = s.trim(); // removing excess blank characters

                // adapting the string to be a JSON Array by adding the brackets where needed
                callback.onEndConnection(new JSONArray((s.startsWith("[") ? "" : "[") + s + (s.endsWith("]") ? "" : "]")));
            } catch (JSONException e) {
                Log.e("EXCEPTION", e.toString());
            }
        }
    }

    /**
     * The function that sends and fetches the results.
     *
     * @param voids Various parameters
     * @return The string that contains the result of the connection.
     */
    @Override
    protected abstract String doInBackground(Void... voids);
}
