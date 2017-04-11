package no.byteme.magnuspoppe.dashboard;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

/**
 * Created by MagnusPoppe on 08/04/2017.
 */

public class VisitorREST implements TrafficAPI
{
    Context context;

    public VisitorREST(Context context)
    {
        this.context = context;
    }

    public void get(int visitorID)
    {
        // Not implemented.
    }

    public void get(String ip)
    {
        // Not implemented.
    }

    public void post(Visitor visitor)
    {
        // Not implemented.
    }

    public void put(Visitor visitor)
    {
        VisitorRESTCall call = new VisitorRESTCall();
        String url = "";
        if (LIVE_CONNECTION)    url = LIVE_DOMAIN;
        else                    url = LOCAL_DOMAIN;

        call.execute(url + PATH + VISITOR + visitor.ip, "PUT", visitor.toJSON());
    }
    public void delete(Visitor visitor)
    {
        VisitorRESTCall call = new VisitorRESTCall();
        String url = "";
        if (LIVE_CONNECTION)    url = LIVE_DOMAIN;
        else                    url = LOCAL_DOMAIN;

        call.execute(url + PATH + VISITOR + visitor.ip, "DELETE", visitor.toJSON());
    }

    private class VisitorRESTCall extends AsyncTask<String, Void, Long>
    {
        // HTTP statuscodes:
        final static int URL = 0;
        final static int HTTP_METHOD = 1;
        final static int PAYLOAD = 2;

        // Statuscodes:
        final static long ERROR = 0l;
        final static long OK = 1l;
        final static long JSON_PARSE_ERROR = 3l;
        final static long IO_ERROR = 4l;
        final static long MALFORMED_URL_EXCEPTION = 5l;


        @Override
        protected Long doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            long result = 0L;

            try
            {
                java.net.URL url = new URL(params[URL]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(params[HTTP_METHOD]);
                connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

                if( params[HTTP_METHOD].equals("PUT") || params[HTTP_METHOD].equals("POST") || params[HTTP_METHOD].equals("DELETE") )
                {
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setChunkedStreamingMode(0);
                    connection.setRequestProperty("Transfer-Encoding","chunked");
                }
                else
                {
                    connection.setDoInput(true);
                }
                connection.connect();

                if( params[HTTP_METHOD].equals("PUT") || params[HTTP_METHOD].equals("POST") || params[HTTP_METHOD].equals("DELETE") )
                {
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(params[PAYLOAD]);
                    out.close();
                }

                int response = connection.getResponseCode();
                String checkResponse = "responsecode: " + response;
                if(response != HttpURLConnection.HTTP_OK)
                    return ERROR;

                if (params[HTTP_METHOD].equals("GET"))
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder payload = new StringBuilder();
                    String responseString;

                    while ((responseString = reader.readLine()) != null)
                        payload.append(responseString);
                    parseData(payload.toString());
                }

                result = OK;
            }
            catch (MalformedURLException e)
            {
                Log.e("ASYNC ERROR", "SOMETHING WENT WRONG: "+e);
                result = MALFORMED_URL_EXCEPTION;
            }
            catch (IOException e)
            {
                Log.e("ASYNC ERROR", "SOMETHING WENT WRONG: "+e);
                result = IO_ERROR;
            }
            catch (JSONException e)
            {
                Log.e("ASYNC ERROR", "JSON ERROR... : "+e);
                result =  JSON_PARSE_ERROR;
            }
            finally
            {
                if (connection != null)
                    connection.disconnect();
            }
            return result;
        }

        /**
         * Parses the recived jsondata from the servercall into
         * Visit and Visitor objects.
         * @param json data gotten from the traffic branch.
         * @throws JSONException
         */
        private void parseData(String json) throws JSONException
        {

        }

        @Override
        protected void onPostExecute(Long result)
        {
            super.onPostExecute(result);

            String statusmessage = "";

            if (result == OK)
            {
                statusmessage = "Executed rest call successfully.";
            }
            else if (result == MALFORMED_URL_EXCEPTION)
            {
                statusmessage = "Bad url...";
            }
            else if (result == IO_ERROR)
            {
                statusmessage = "Problems reading the downloaded data. ( I/O ERROR )";
            }
            else if (result == JSON_PARSE_ERROR)
            {
                statusmessage = "Bad JSON format on data.";
            }
            else if (result == ERROR)
            {
                statusmessage = "Something went wrong.";
            }
            else
            {
                statusmessage = "Unknown errorcode... WHAT?!";
            }

            Log.d("ASYNC_POST_EXECUTE", "Result-code=" + result + ", Message=" + statusmessage);
        }
    }
}
