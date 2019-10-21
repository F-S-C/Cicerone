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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Special connector to the database. This connector fetches all entries available from a
 * server-side script.
 */
public class GetDataConnector<T extends BusinessEntity> extends AsyncDatabaseConnector<T> {

    /**
     * The function fetches the results.
     *
     * @param voids Various parameters
     * @return The string that contains the result of the connection.
     */
    @Override
    protected String doInBackground(Void... voids) {
        super.doInBackground(voids);
        try {
            URL url = new URL(fileUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                stringBuilder.append(json).append("\n");
            }
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e("GET_DATA_ERROR", sw.toString());
            setError(e);
            return null;
        }
    }

    private GetDataConnector(Builder<T> builder) {
        super(builder);
    }

    /**
     * Builder class for BusinessEntity.
     */
    public static class Builder<B extends BusinessEntity> extends AsyncDatabaseConnector.Builder<B> {

        /**
         * Constructor. Uses the AsyncDatabaseConnector's Builder constructor.
         *
         * @param url     The url of the server-side connector.
         * @param builder The BusinessEntity's Builder.
         */
        public Builder(String url, BusinessEntityBuilder<B> builder) {
            super(url, builder);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setOnStartConnectionListener(OnStartConnectionListener)
         */
        @Override
        public Builder<B> setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder<B>) super.setOnStartConnectionListener(listener);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setOnEndConnectionListener(OnEndConnectionListener)
         */
        @Override
        public Builder<B> setOnEndConnectionListener(OnEndConnectionListener<B> onEndConnectionListener) {
            return (Builder<B>) super.setOnEndConnectionListener(onEndConnectionListener);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setContext(Activity)
         */
        @Override
        public Builder<B> setContext(Activity context) {
            return (Builder<B>) super.setContext(context);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#build()
         */
        @Override
        public GetDataConnector<B> build() {
            return new GetDataConnector<>(this);
        }
    }
}
