package app_connector;

import android.util.Log;

import com.fsc.cicerone.model.BusinessEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BooleanConnector extends SendInPostConnector<BooleanConnector.BooleanResult> {

    private static final String ERROR_TAG = "BOOL_CONNECTOR_ERROR";

    public interface CallbackInterface {
        /**
         * Function that will be called before the start of the connection.
         */
        void onStartConnection();

        /**
         * Function that will be called when the connection has ended.
         *
         * @param result The response of the connection.
         */
        void onEndConnection(BooleanResult result) throws JSONException;
    }

    private CallbackInterface callback;

    public BooleanConnector(String url) {
        super(url, null);
        this.callback = new CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(BooleanResult result) {
                // Do nothing
            }
        };
    }

    public BooleanConnector(String url, CallbackInterface callback) {
        super(url, null, new DatabaseConnector.CallbackInterface<BooleanResult>() {
            @Override
            public void onStartConnection() {
                callback.onStartConnection();
            }

            @Override
            public void onEndConnection(List<BooleanResult> list) {
                // Do nothing
            }
        });
        this.callback = callback;
    }

    public BooleanConnector(String url, CallbackInterface callback, JSONObject objectToSend) {
        super(url, null, new DatabaseConnector.CallbackInterface<BooleanResult>() {
            @Override
            public void onStartConnection() {
                callback.onStartConnection();
            }

            @Override
            public void onEndConnection(List<BooleanResult> list) {
                // Do nothing
            }
        }, objectToSend);
        this.callback = callback;
    }

    @Override
    protected void executeAfterConnection(String s) {
        try {
            this.callback.onEndConnection(new BooleanResult(s));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
    }

    public static class BooleanResult extends BusinessEntity {
        private boolean result;
        private String message;

        public BooleanResult(boolean result, String message) {
            this.result = result;
            this.message = message;
        }

        BooleanResult(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                result = jsonObject.getBoolean("result");
                message = jsonObject.getString(result ? "message" : "error");
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
            }
        }

        public boolean getResult() {
            return result;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public JSONObject toJSONObject() {
            JSONObject toReturn = new JSONObject();
            try {
                toReturn.put("result", result);
                toReturn.put(result ? "message" : "error", message);
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
            }
            return toReturn;
        }
    }
}
