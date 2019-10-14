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

import java.util.HashMap;
import java.util.Map;

public class BooleanConnector extends SendInPostConnector<BooleanConnector.BooleanResult> {

    private static final String ERROR_TAG = "BOOL_CONNECTOR_ERROR";

    public interface OnEndConnectionListener {
        /**
         * Function that will be called when the connection has ended.
         *
         * @param result The response of the connection.
         */
        void onEndConnection(BooleanResult result);
    }

    private OnEndConnectionListener onEndConnectionListener;

    @Override
    protected void executeAfterConnection(String s) {
        if (onEndConnectionListener != null)
            onEndConnectionListener.onEndConnection(new BooleanResult(s));
    }

    public static class BooleanResult extends BusinessEntity {
        private boolean result;
        private String message;

        private static final String RESULT_KEY = "result";
        private static final String ERROR_KEY = "error";
        private static final String MESSAGE_KEY = "message";

        public BooleanResult(boolean result, String message) {
            super();
            this.result = result;
            this.message = message;
        }

        BooleanResult(String json) {
            super(json);
        }

        BooleanResult(Map<String, Object> jsonObject) {
            super(jsonObject);
        }

        @Override
        protected void loadFromMap(Map<String, Object> jsonObject) {
            result = (boolean) jsonObject.get(RESULT_KEY);
            if (jsonObject.containsKey(result ? MESSAGE_KEY : ERROR_KEY))
                message = (String) jsonObject.get(result ? MESSAGE_KEY : ERROR_KEY);
        }

        public boolean getResult() {
            return result;
        }

        public String getMessage() {
            return message != null ? message : "";
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> toReturn = new HashMap<>();
            toReturn.put(RESULT_KEY, result);
            toReturn.put(result ? MESSAGE_KEY : ERROR_KEY, message);
            return toReturn;
        }
    }

    private BooleanConnector(Builder builder) {
        super(builder);
        onEndConnectionListener = builder.onEndConnectionListener;
    }

    public static class Builder extends SendInPostConnector.Builder<BooleanResult> {

        private OnEndConnectionListener onEndConnectionListener;

        public Builder(String url) {
            super(url, null);
        }

        @Override
        public Builder setOnStartConnectionListener(OnStartConnectionListener listener) {
            return (Builder) super.setOnStartConnectionListener(listener);
        }

        public Builder setOnEndConnectionListener(OnEndConnectionListener listener) {
            onEndConnectionListener = listener;
            return this;
        }

        @Override
        public Builder setContext(Activity context) {
            return (Builder) super.setContext(context);
        }

        @Override
        public Builder setObjectToSend(Map<String, Object> objectToSend) {
            return (Builder) super.setObjectToSend(objectToSend);
        }

        @Override
        public BooleanConnector build() {
            return new BooleanConnector(this);
        }
    }
}
