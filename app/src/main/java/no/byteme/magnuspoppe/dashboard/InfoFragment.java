package no.byteme.magnuspoppe.dashboard;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static no.byteme.magnuspoppe.dashboard.VisitListFragment.VISITOR_SELECTED;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment implements OnMapReadyCallback
{
    Fragment activeFragment;
    int visitorID;
    FloatingActionButton fabEdit, fabSave;
    MapFragment mapFragment;
    LatLng latLng;
    LinearLayout main_info_layout;

    public InfoFragment()
    {
        // Required empty public constructor
    }

    public static InfoFragment newInstance()
    {
        InfoFragment fragment = new InfoFragment();
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
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        main_info_layout = (LinearLayout) view.findViewById(R.id.main_layout_info);

        Bundle arguments = this.getArguments();
        if (arguments != null)
        {
            visitorID = arguments.getInt(VISITOR_SELECTED);
        }
        else getFragmentManager().popBackStack();

        // GETTING THE MAP FRAGMENT:
        mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Visitor visitor = ViewControllerActivity.visitors.get(visitorID);
        latLng = new LatLng(visitor.latitude, visitor.longitude);

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchViewEditInfo();
            }
        });

        fabSave = (FloatingActionButton) view.findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchViewDetailedInfo();
            }
        });

        // Setting the first seen view.
        // Setting up the fragment:
        Fragment activeFragment = new DetailedInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout_info, activeFragment);

        // Bundling additional information:
        Bundle item = new Bundle();
        item.putInt(VISITOR_SELECTED, visitorID);
        activeFragment.setArguments(item);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        return view;
    }

    public void switchViewEditInfo()
    {
        // Switching buttons:
        fabSave.setVisibility(View.VISIBLE);
        fabEdit.setVisibility(View.GONE);

        getFragmentManager().popBackStack();

        activeFragment = new EditInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout_info, activeFragment);

        // Bundling additional information:
        Bundle item = new Bundle();
        item.putInt(VISITOR_SELECTED, visitorID);
        activeFragment.setArguments(item);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void switchViewDetailedInfo()
    {
        // Saving data edited.
        if (activeFragment instanceof EditInfoFragment)
        {
            EditInfoFragment f = (EditInfoFragment) activeFragment;
            f.saveEditedData();
        }

        // Switching buttons:
        fabEdit.setVisibility(View.VISIBLE);
        fabSave.setVisibility(View.GONE);

        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();

        // Setting up the fragment:
        activeFragment = new DetailedInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout_info, activeFragment);
        transaction.addToBackStack(null);

        // Bundling additional information:
        Bundle item = new Bundle();
        item.putInt(VISITOR_SELECTED, visitorID);
        activeFragment.setArguments(item);

        // Commit the transaction
        transaction.commit();
    }
}
