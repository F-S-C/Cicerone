package app_connector;

import android.os.AsyncTask;
import android.util.Log;

import com.fsc.cicerone.model.BusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Generic connector to a database.
 * This class gives an interface to communicate with a database. It needs the URL of the server-side
 * script that generates and handles the data.
 */
public abstract class DatabaseConnector<T extends BusinessEntity> extends AsyncTask<Void, Void, String> {

    /**
     * An interface to use before and after the connection.
     */
    public interface CallbackInterface<T> {
        /**
         * Function that will be called before the start of the connection.
         */
        void onStartConnection();

        /**
         * Function that will be called when the connection has ended.
         *
         * @param list This array contains the results of the connection.
         */
        void onEndConnection(List<T> list) throws JSONException;
    }

    final String fileUrl; // The URL of the server-side script
    private CallbackInterface<T> callback; // A reference to a CallbackInterface

    private BusinessEntityBuilder<T> builder;

    /**
     * Constructor.
     *
     * @param url The url of the server-side script.
     */
    DatabaseConnector(String url, BusinessEntityBuilder<T> builder) {
        super();
        fileUrl = url;
        this.builder = builder;
        callback = new CallbackInterface<T>() {
            @Override
            public void onEndConnection(List<T> jsonArray) {
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
    DatabaseConnector(String url, BusinessEntityBuilder<T> builder, CallbackInterface<T> callback) {
        super();
        fileUrl = url;
        this.builder = builder;
        this.callback = callback != null? callback : new CallbackInterface<T>() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(List<T> list) {
                // Do nothing
            }
        };
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

            s = s.trim(); // removing excess blank characters

            executeAfterConnection(s);
        }
    }

    protected void executeAfterConnection(String s) {
        try {
            // adapting the string to be a JSON Array by adding the brackets where needed
            JSONArray jsonArray = new JSONArray((s.startsWith("[") ? "" : "[") + s + (s.endsWith("]") ? "" : "]"));
            ArrayList<T> array = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    T object = builder.fromJSONObject(jsonArray.getJSONObject(i));

                    array.add(object);
                } catch (JSONException e) {
                    Log.e("ADD_ELEMENT_EXCEPTION", e.getMessage());
                }
            }


            callback.onEndConnection(array);
        } catch (JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            Log.e("EXCEPTION", e.toString());
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
