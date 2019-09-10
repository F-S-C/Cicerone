package app_connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Special connector to the database.
 * This connector fetches all entries available from a server-side script.
 */
public class GetDataConnector extends DatabaseConnector {

    /**
     * Constructor.
     *
     * @param url The url of the server-side script.
     */
    public GetDataConnector(String url) {
        super(url);
    }

    /**
     * Constructor.
     *
     * @param url      The url of the server-side script.
     * @param callback A reference to CallbackInterface that will be used before and after the
     *                 connection.
     */
    public GetDataConnector(String url, CallbackInterface callback) {
        super(url, callback);
    }

    /**
     * The function fetches the results.
     *
     * @param voids Various parameters
     * @return The string that contains the result of the connection.
     */
    @Override
    protected String doInBackground(Void... voids) {
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
            return null;
        }
    }
}
