package appConnector;

import android.os.AsyncTask;

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
         * @throws JSONException
         */
        void onEndConnection(JSONArray jsonArray) throws JSONException;
    }

    String fileUrl; // The URL of the server-side script
    private CallbackInterface callback; // A reference to a CallbackInterface

    /**
     * Default constructor.
     */
    DatabaseConnector() {
        super();
        callback = new CallbackInterface() {
            @Override
            public void onEndConnection(JSONArray jsonArray) {
            }

            @Override
            public void onStartConnection() {
            }
        };
    }

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
            }

            @Override
            public void onStartConnection() {
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
     * Sets the URL of the server-side script.
     *
     * @param fileUrl The new URL of the server-side script.
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
        try {
            callback.onEndConnection(new JSONArray(s));
        } catch (JSONException e) {
            e.printStackTrace();
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
