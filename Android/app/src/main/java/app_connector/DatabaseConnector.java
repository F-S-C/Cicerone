package app_connector;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.BusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
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
    private final WeakReference<Context> context;

    private Exception error = null;

    /**
     * Constructor.
     *
     * @param url The url of the server-side script.
     */
    DatabaseConnector(Context context, String url, BusinessEntityBuilder<T> builder) {
        super();
        this.context = context != null ? new WeakReference<>(context) : null;
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
    DatabaseConnector(Context context, String url, BusinessEntityBuilder<T> builder, CallbackInterface<T> callback) {
        super();
        this.context = context != null ? new WeakReference<>(context) : null;
        fileUrl = url;
        this.builder = builder;
        this.callback = callback != null ? callback : new CallbackInterface<T>() {
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
        } else {
            manageErrors();
        }
    }

    protected void executeAfterConnection(String s) {
        try {
            // adapting the string to be a JSON Array by adding the brackets where needed
            JSONArray jsonArray = new JSONArray((s.startsWith("[") ? "" : "[") + s + (s.endsWith("]") ? "" : "]"));
            ArrayList<T> array = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                T object = builder.fromJSONObject(jsonArray.getJSONObject(i));

                array.add(object);
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

    protected void setError(Exception error) {
        this.error = error;
    }

    private void manageErrors() {
        if (context == null || error == null)
            return;

        new MaterialAlertDialogBuilder(context.get())
                .setTitle("Connection Error")
                .setMessage(error.getMessage())
                .setPositiveButton(context.get().getString(R.string.ok), null)
                .setNegativeButton("Close app", (dialog, which) -> {
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                })
                .show();
    }
}
