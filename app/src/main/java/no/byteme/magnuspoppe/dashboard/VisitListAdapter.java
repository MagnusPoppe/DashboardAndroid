package no.byteme.magnuspoppe.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class VisitListAdapter extends BaseAdapter
{
    Context mContext;
    ArrayList<Visitor> mVisitor;
    LayoutInflater mInflater;

    public VisitListAdapter(Context c, ArrayList<Visitor> visitors)
    {
        mContext = c;
        mVisitor = visitors;
        mInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return  mVisitor.size();
    }

    public Object getItem(int position)
    {
        return mVisitor.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        // Initializing the view element:
        VisitList list;
        if (view == null) // Only runs for the first object.
        {
            view = mInflater.inflate(R.layout.visit_list_element, null);
            list = new VisitList();
            list.textIP = (TextView) view.findViewById(R.id.visit_list_ip);
            list.textOrg = (TextView) view.findViewById(R.id.visit_list_org);
            list.textDate = (TextView) view.findViewById(R.id.visit_list_date);
            view.setTag(list);
        }
        else
        {
            list = (VisitList) view.getTag();
        }

        // Filling with data:
        Visitor current = mVisitor.get(position);
        list.textIP.setText(current.ip);
        list.textOrg.setText(current.organisation);

        // Formatting the time:
        DateTime dateTime = current.findLastVisit().datetime;

        if (dateTime != null)
            list.textDate.setText(dateTime.prettyPrint(view));
        else
            list.textDate.setText(view.getResources().getString(R.string.NotValidDate));
        return view;
    }

    private static class VisitList
    {
        public TextView textIP;
        public TextView textOrg;
        public TextView textDate;
    }
}