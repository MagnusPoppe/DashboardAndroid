package no.byteme.magnuspoppe.dashboard;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInfoFragment extends Fragment implements OnMapReadyCallback
{

    int visitorID;

    TextView ip;
    MapFragment mapFragment;
    LatLng latLng;

    TextInputLayout layoutOrg;
    EditText org;


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
    public void onMapReady(GoogleMap map)
    {
        map.addMarker(new MarkerOptions().position(latLng).title("Visitor location"));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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

        Visitor visitor = ViewControllerActivity.visitors.get(visitorID);
        latLng = new LatLng(visitor.latitude, visitor.longitude);

        // CREATING TEXTVIEWS:
        ip = (TextView) view.findViewById(R.id.edit_ip);
        ip.setText(visitor.ip);

        // GETTING THE MAP FRAGMENT:
        mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.otherMap);
        mapFragment.getMapAsync(this);

        // CREATING EDITTEXT FIELDS:
        org = (EditText) view.findViewById(R.id.edit_org);
        layoutOrg = (TextInputLayout) view.findViewById(R.id.edit_layout_org);

        return view;
    }
}
