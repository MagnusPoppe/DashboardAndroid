package no.byteme.magnuspoppe.dashboard;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment
{

    public static TextView time_online;
    public static TextView unique_visitor;
    public static TextView unique_visits;
    public static TextView visits_month;
    public static TextView visits_total;

    public DashboardFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance()
    {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        time_online = (TextView) view.findViewById(R.id.timer_online_value);
        unique_visitor = (TextView) view.findViewById(R.id.unique_visitor_value);
        unique_visits = (TextView) view.findViewById(R.id.unique_visits_value);
        visits_month = (TextView) view.findViewById(R.id.visitor_month_value);
        visits_total = (TextView) view.findViewById(R.id.visitor_total_value);

        return view;
    }
}
