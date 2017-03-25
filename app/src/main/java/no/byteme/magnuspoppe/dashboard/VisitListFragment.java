package no.byteme.magnuspoppe.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment}
 * Use the {@link VisitListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitListFragment extends Fragment
{
    public VisitListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment VisitListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitListFragment newInstance(String param1, String param2)
    {
        VisitListFragment fragment = new VisitListFragment();
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
        View v = inflater.inflate(R.layout.fragment_visit_list, container, false);
        return v;
    }
}
