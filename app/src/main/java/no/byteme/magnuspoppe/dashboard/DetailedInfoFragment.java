package no.byteme.magnuspoppe.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedInfoFragment extends Fragment
{

    int visitorID;

    TextView ip, hostname, lastVisit, firstVisit, numVisits, latlong, address;
    MapFragment mapFragment;
    LatLng latLng;
    Context context;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    public DetailedInfoFragment()
    {
        // Required empty public constructor
    }

    public static DetailedInfoFragment newInstance()
    {
        DetailedInfoFragment fragment = new DetailedInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed_info, container, false);

        Bundle arguments = this.getArguments();
        if (arguments != null)
        {
            visitorID = arguments.getInt(VisitListFragment.VISITOR_SELECTED);
        }
        else getFragmentManager().popBackStack();


        final Visitor visitor = ViewControllerActivity.visitors.get(visitorID);

        // CREATING TEXTVIEWS:
        ip = (TextView) view.findViewById(R.id.detailed_ip);
        ip.setText(visitor.ip);

        hostname = (TextView) view.findViewById(R.id.detailed_hostname);
        hostname.setText( visitor.hostname.equals("NULL") ? visitor.organisation : visitor.hostname );

        lastVisit = (TextView) view.findViewById(R.id.detailed_last_visit);
        lastVisit.setText(visitor.findLastVisit().datetime.prettyPrint(view.getResources()));

        firstVisit = (TextView) view.findViewById(R.id.detailed_first_visit);
        firstVisit.setText(visitor.findFirstVisit().datetime.prettyPrint(view.getResources()));

        numVisits = (TextView) view.findViewById(R.id.detailed_total_number_of_visits);
        numVisits.setText(visitor.visits.size() + " visits");

        latlong = (TextView) view.findViewById(R.id.detailed_latlng);
        latlong.setText(visitor.latitude + ", " + visitor.longitude);
        latLng = new LatLng(visitor.latitude, visitor.longitude);

        address = (TextView) view.findViewById(R.id.detailed_address);
        address.setText(visitor.city + ", " + visitor.country);

        Button delete = (Button) view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                VisitorREST rest = new VisitorREST(context);
                rest.delete(visitor);
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
