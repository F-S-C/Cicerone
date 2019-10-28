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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A connector that sends data in POST and handles boolean results.
 */
public class BooleanConnector extends SendInPostConnector<BooleanConnector.BooleanResult> {

    private static final String ERROR_TAG = "BOOL_CONNECTOR_ERROR";

    /**
     * An interfaced to be used to create callback functions to be used to define the connector
     * behaviour on the connection's end and handles a BooleanResult.
     */
    public interface OnEndConnectionListener {
        /**
         * Function that will be called when the connection has ended.
         *
         * @param result The response of the connection.
         */
        void onEndConnection(BooleanResult result);
    }

    private OnEndConnectionListener onEndConnectionListener;

    /**
     * @see AsyncDatabaseConnector#executeAfterConnection(String)
     */
    @Override
    protected void executeAfterConnection(String s) {
        if (onEndConnectionListener != null)
            onEndConnectionListener.onEndConnection(new BooleanResult(s));
    }

    /**
     * The boolean result. It contains the result (boolean) and the message (String).
     */
    public static class BooleanResult extends BusinessEntity {
        private boolean result;
        private String message;

        private static final String RESULT_KEY = "result";
        private static final String ERROR_KEY = "error";
        private static final String MESSAGE_KEY = "message";

        /**
         * Constructor.
         *
         * @param result  The result (boolean).
         * @param message The message (String).
         */
        public BooleanResult(boolean result, String message) {
            super();
            this.result = result;
            this.message = message;
        }

        /**
         * Constructor. Uses the BusinessEntity constructor.
         */
        BooleanResult(String json) {
            super(json);
        }

        /**
         * Constructor. Uses the BusinessEntity constructor.
         */
        BooleanResult(JSONObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @see com.fsc.cicerone.model.BusinessEntity#loadFromJSONObject(JSONObject)
         */
        @Override
        protected void loadFromJSONObject(JSONObject jsonObject) {
            try {
                result = jsonObject.getBoolean(RESULT_KEY);
                if (jsonObject.has(result ? MESSAGE_KEY : ERROR_KEY))
                    message = jsonObject.getString(result ? MESSAGE_KEY : ERROR_KEY);
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage() + " (trying to parse " + jsonObject.toString() + ")");
            }
        }

        /**
         * Return the value of result.
         *
         * @return The boolean value of result.
         */
        public boolean getResult() {
            return result;
        }

        /**
         * Return the message.
         *
         * @return The message. If null, returns an empty string.
         */
        public String getMessage() {
            return message != null ? message : "";
        }

        /**
         * @see com.fsc.cicerone.model.BusinessEntity#toJSONObject()
         */
        @Override
        public JSONObject toJSONObject() {
            JSONObject toReturn = new JSONObject();
            try {
                toReturn.put(RESULT_KEY, result);
                toReturn.put(result ? MESSAGE_KEY : ERROR_KEY, message);
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
            }
            return toReturn;
        }
    }

    /**
     * BooleanConnector constructor. Uses a builder to create a SendInPostConnector.
     *
     * @param builder The SendInPostConnector.Builder object.
     */
    private BooleanConnector(Builder builder) {
        super(builder);
        onEndConnectionListener = builder.onEndConnectionListener;
    }

    /**
     * Builder class for BooleanConnector.
     */
    public static class Builder extends SendInPostConnector.Builder<BooleanResult> {

        private OnEndConnectionListener onEndConnectionListener;

        /**
         * Constructor. Uses the SendInPostConnector's Builder constructor.
         *
         * @param url The url of the server-side connector.
         */
        public Builder(String url) {
            super(url, null);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setOnStartConnectionListener(AsyncDatabaseConnector.OnStartConnectionListener)
         */
        @Override
        public Builder setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder) super.setOnStartConnectionListener(listener);
        }

        /**
         * Set the listener at the end of the connection. Specify what to do at the end of the
         * connection.
         *
         * @param listener The listener.
         * @return The Builder itself.
         */
        public Builder setOnEndConnectionListener(OnEndConnectionListener listener) {
            onEndConnectionListener = listener;
            return this;
        }

        /**
         * @see AsyncDatabaseConnector.Builder#setContext(Activity)
         */
        @Override
        public Builder setContext(Activity context) {
            return (Builder) super.setContext(context);
        }

        /**
         * @see SendInPostConnector.Builder#setObjectToSend(Map)
         */
        @Override
        public Builder setObjectToSend(Map<String, Object> objectToSend) {
            return (Builder) super.setObjectToSend(objectToSend);
        }

        /**
         * @see AsyncDatabaseConnector.Builder#build()
         */
        @Override
        public BooleanConnector build() {
            return new BooleanConnector(this);
        }
    }
}
