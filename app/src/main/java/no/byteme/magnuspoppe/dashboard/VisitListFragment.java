package no.byteme.magnuspoppe.dashboard;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;


/**
 * A simple {@link Fragment}
 * Use the {@link VisitListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitListFragment extends Fragment
{

    public final static String VISITOR_SELECTED = "VisitorSelected";
    ListView visitsList;
    ArrayList<Visitor> visitors;
    SwipeRefreshLayout refreshLayout;
    ViewControllerActivity activity;
    VisitListAdapter adapter;

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
    public static VisitListFragment newInstance()
    {
        VisitListFragment fragment = new VisitListFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.menu_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
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

        // Setting up the toolbar:
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_toolbar);
        getActivity().setActionBar(toolbar);

        // Setting up the menu:
        setHasOptionsMenu(true);

        // Filling the list view:
        visitsList = (ListView) v.findViewById(R.id.visitList);
        activity = (ViewControllerActivity) getActivity();
        visitors = ViewControllerActivity.visitors;
        adapter = new VisitListAdapter(container.getContext(), visitors);
        visitsList.setAdapter(adapter);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );

        visitsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                if( visitors.get(position).visits.size() == 0)
                {
                    String message = getResources().getString(R.string.NotValidDate);
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                    return;
                }

                Bundle item = new Bundle();
                item.putInt(VISITOR_SELECTED, position);
                if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    InfoFragment fragment = new InfoFragment();
                    fragment.setArguments(item);
                    ft.replace(R.id.dashboard, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        return v;
    }

    private void refresh()
    {
        activity.updateDatabase();
        adapter.notifyDataSetChanged();
    }


    private void openSettings()
    {
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = new Preferences();
            ft.replace(R.id.dashboard, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
