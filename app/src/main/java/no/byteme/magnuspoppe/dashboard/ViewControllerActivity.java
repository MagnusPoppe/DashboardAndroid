package no.byteme.magnuspoppe.dashboard;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewControllerActivity extends Activity  implements TrafficAPI
{
    public ArrayList<Visitor> visitors;
    private View dashboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller);

        visitors = new ArrayList<>();
        dashboardView  = findViewById(R.id.dashboard);
        updateDatabase();
    }

    public void switchView(View v)
    {
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            VisitListFragment fragment = new VisitListFragment();
            ft.replace(R.id.dashboard, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void updateDatabase()
    {
        if (isOnline())
        {
            AsyncGetTraffic asyncCall = new AsyncGetTraffic();

            if (LIVE_CONNECTION) asyncCall.execute(LIVE_DOMAIN + PATH, "GET");
            else asyncCall.execute(LOCAL_DOMAIN + PATH + "traffic/", "GET");
        }
        else
        {
            String message = getResources().getString(R.string.NoConnection);
            Snackbar.make(dashboardView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Activity.CONNECTIVITY_SERVICE
        );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class AsyncGetTraffic extends AsyncTask<String, Void, Long>
    {
        // HTTP statuscodes:
        final static int URL = 0;
        final static int HTTP_METHOD = 1;

        // Statuscodes:
        final static long ERROR = 0l;
        final static long DEVICE_UPDATED = 1l;
        final static long DEVICE_UP_TO_DATE = 2l;
        final static long JSON_PARSE_ERROR = 3l;
        final static long IO_ERROR = 4l;
        final static long MALFORMED_URL_EXCEPTION = 5l;


        @Override
        protected Long doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            InputStreamReader ioReader = null;
            long result = 0L;

            try
            {
                java.net.URL url = new URL(params[URL]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true); // SIER IFRA AT VI SKAL HA TILBAKE DATA
                connection.setRequestMethod(params[HTTP_METHOD]);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder payload = new StringBuilder();
                String responseString;

                while ((responseString = reader.readLine()) != null)
                {
                    payload.append(responseString);
                }
                String payloadbody = payload.toString();
                if (! localDataUpToDate()) ;
                    parseData(payload.toString());

                result = DEVICE_UPDATED;
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
                Log.e("ASYNC ERROR", "SOMETHING WENT WRONG: "+e);
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
         * Checks if the local SQLite database is up to date with the
         * server.
         * @return true if the data is up to date, false otherwise.
         */
        private boolean localDataUpToDate()
        {
            return false; // NOT YET IMPLEMENTED.
        }

        /**
         * Parses the recived jsondata from the servercall into
         * Visit and Visitor objects.
         * @param json data gotten from the traffic branch.
         * @throws JSONException
         */
        private void parseData(String json) throws JSONException
        {
            JSONArray array = new JSONArray(json);

            for(int i = 0; i < array.length(); i++)
                visitors.add( new Visitor( (JSONObject) array.get(i)) );
        }

        @Override
        protected void onPostExecute(Long result)
        {
            super.onPostExecute(result);

            String statusmessage = "";
            if (result == DEVICE_UPDATED)
                statusmessage = "Device data updated.";
            else if (result == DEVICE_UP_TO_DATE)
                statusmessage = "Device up to date.";
            else if (result == MALFORMED_URL_EXCEPTION)
                statusmessage = "Bad url...";
            else if (result == IO_ERROR)
                statusmessage = "Problems reading the downloaded data. ( I/O ERROR )";
            else if (result == JSON_PARSE_ERROR)
                statusmessage = "Bad JSON format on data.";
            else if (result == ERROR)
                statusmessage = "Something went wrong.";
            else
                statusmessage = "Unknown errorcode... WHAT?!";

            Log.d("ASYNC_POST_EXECUTE", "Result-code="+result+", Message="+statusmessage);
            Snackbar.make(dashboardView, statusmessage, Snackbar.LENGTH_LONG).show();
        }
    }
}