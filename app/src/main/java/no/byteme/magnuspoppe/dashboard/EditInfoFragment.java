package no.byteme.magnuspoppe.dashboard;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInfoFragment extends Fragment
{

    int visitorID;
    TextView ip;
    TextInputEditText editOrg, editHost;


    public EditInfoFragment()
    {
        // Required empty public constructor
    }

    public static EditInfoFragment newInstance()
    {
        EditInfoFragment fragment = new EditInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);

        Bundle arguments = this.getArguments();
        if (arguments != null)
        {
            visitorID = arguments.getInt(VisitListFragment.VISITOR_SELECTED);
        }
        else getFragmentManager().popBackStack(); // GÅ TILBAKE...

        final Visitor visitor = ViewControllerActivity.visitors.get(visitorID);

        // CREATING TEXTVIEWS:
        ip = (TextView) view.findViewById(R.id.edit_ip);
        ip.setText(visitor.ip);

        // CREATING EDITTEXT FIELDS:

        editOrg = (TextInputEditText) view.findViewById(R.id.edit_org);
        editHost = (TextInputEditText) view.findViewById(R.id.edit_hostname);

        Button lookupButton = (Button) view.findViewById(R.id.lookupButton);

        lookupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ipLookup(visitor.ip);
            }
        });

        return view;
    }

    /**
     * Executes the ip-lookup using the ipinfo.io service.
     * @param ip
     */
    private void ipLookup(String ip)
    {
        AsyncIPLookup lookup = new AsyncIPLookup();
        lookup.execute("http://ipinfo.io/"+ip+"/json", "GET");
    }

    private class AsyncIPLookup extends AsyncTask<String, Void, Long>
    {
        // HTTP statuscodes:
        final static int URL = 0;
        final static int HTTP_METHOD = 1;

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
                connection.setDoInput(true); // SIER IFRA AT VI SKAL HA TILBAKE DATA
                connection.setRequestMethod(params[HTTP_METHOD]);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder payload = new StringBuilder();
                String responseString;

                while ((responseString = reader.readLine()) != null)
                    payload.append(responseString);

                parseData(payload.toString());
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
            JSONObject obj = new JSONObject(json);
            hostname = obj.getString("hostname");
            org = obj.getString("org");
        }

        String hostname, org;

        @Override
        protected void onPostExecute(Long result)
        {
            super.onPostExecute(result);

            String statusmessage = "";

            if (result == OK)
            {
                statusmessage = "Updated textFields.";

                // Fikk rar feilmelding når jeg prøvde å oppdatere disse fra async. Måtte endre litt.                 editHost.setText(hostname);
                editOrg.setText(org);
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
