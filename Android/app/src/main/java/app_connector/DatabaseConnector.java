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
import android.os.AsyncTask;
import android.util.Log;

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

    public interface OnStartConnectionListener {
        /**
         * Function that will be called before the start of the connection.
         */
        void onStartConnection();
    }

    public interface OnEndConnectionListener<T> {
        /**
         * Function that will be called when the connection has ended.
         *
         * @param list This array contains the results of the connection.
         */
        void onEndConnection(List<T> list);
    }

    String fileUrl; // The URL of the server-side script
    private OnStartConnectionListener onStartConnectionListener;
    private OnEndConnectionListener<T> onEndConnectionListener;

    private BusinessEntityBuilder<T> builder;
    private WeakReference<Activity> context;

    private Exception error = null;

    /**
     * A function that will be called before the execution.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (onStartConnectionListener != null) onStartConnectionListener.onStartConnection();
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

    /**
     * A function that will be executed after the connection ends. It should call onEndConnectionListener.onEndConnection in its body.
     *
     * @param s A string containing the connection result.
     * @see OnEndConnectionListener#onEndConnection(List)
     */
    protected void executeAfterConnection(String s) {
        try {
            // adapting the string to be a JSON Array by adding the brackets where needed
            JSONArray jsonArray = new JSONArray((s.startsWith("[") ? "" : "[") + s + (s.endsWith("]") ? "" : "]"));
            ArrayList<T> array = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                T object = builder.fromJSONObject(jsonArray.getJSONObject(i));
                array.add(object);
            }


            if (onEndConnectionListener != null) onEndConnectionListener.onEndConnection(array);
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

    /**
     * Manages the errors.
     */
    private void manageErrors() {
        if (context == null || error == null)
            return;

        new MaterialAlertDialogBuilder(context.get())
                .setTitle("Connection Error")
                .setMessage(error.getMessage())
                .setPositiveButton(context.get().getString(R.string.ok), null)
                .setNegativeButton("Close app", (dialog, which) -> {
                    context.get().finishAndRemoveTask();
                    System.exit(1);
                })
                .show();
    }

    protected DatabaseConnector(Builder<T> builder) {
        this.context = builder.context;
        this.onStartConnectionListener = builder.onStartConnectionListener;
        this.onEndConnectionListener = builder.onEndConnectionListener;
        this.fileUrl = builder.url;
        this.builder = builder.businessEntityBuilder;
    }

    public abstract static class Builder<B extends BusinessEntity> {
        private final String url; // The URL of the server-side script

        private OnStartConnectionListener onStartConnectionListener = null;
        private OnEndConnectionListener<B> onEndConnectionListener = null;

        private final BusinessEntityBuilder<B> businessEntityBuilder;
        private WeakReference<Activity> context = null;

        Builder(String url, BusinessEntityBuilder<B> businessEntityBuilder) {
            this.url = url;
            this.businessEntityBuilder = businessEntityBuilder;
        }

        public Builder<B> setOnStartConnectionListener(OnStartConnectionListener onStartConnectionListener) {
            this.onStartConnectionListener = onStartConnectionListener;
            return this;
        }

        public Builder<B> setOnEndConnectionListener(OnEndConnectionListener<B> onEndConnectionListener) {
            this.onEndConnectionListener = onEndConnectionListener;
            return this;
        }

        public Builder<B> setContext(Activity context) {
            this.context = context != null ? new WeakReference<>(context) : null;
            return this;
        }

        public abstract DatabaseConnector<B> build();

    }
}
