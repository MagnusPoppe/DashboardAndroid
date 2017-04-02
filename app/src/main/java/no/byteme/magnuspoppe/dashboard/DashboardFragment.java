package no.byteme.magnuspoppe.dashboard;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import static no.byteme.magnuspoppe.dashboard.ViewControllerActivity.last30Days;
import static no.byteme.magnuspoppe.dashboard.ViewControllerActivity.totalVisits;
import static no.byteme.magnuspoppe.dashboard.ViewControllerActivity.visitors;

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
    public static PieChart pieChart;
    public static LineChart lineChart;

    public DashboardFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    public static DashboardFragment newInstance()
    {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
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
        //pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        lineChart = (LineChart) view.findViewById(R.id.line_chart);
        return view;
    }

    public static void updateWidgets()
    {
        if (visits_month != null)
            visits_month.setText("" + last30Days.size());
        if (visits_total != null)
            visits_total.setText("" + totalVisits);
        if (unique_visitor != null)
            unique_visitor.setText("" + visitors.size());
    }

    public static void updateChartData()
    {
        // Formatting chart data:
        int[] visitsPerMonth2016 = new int[12];
        int[] visitsPerMonth2017 = new int[12];
        DateTime now = DateTime.now();
        for (Visitor visitor : visitors)
        {
            for (Visit visit : visitor.visits)
            {
                if (now.year == visit.datetime.year)
                    visitsPerMonth2017[visit.datetime.month - 1]++;
                else if (2016 == visit.datetime.year)
                    visitsPerMonth2016[visit.datetime.month - 1]++;
            }
        }

        // Creating entries for the chart based on the data above:
        List<Entry> entries2016 = new ArrayList<Entry>();
        List<Entry> entries2017 = new ArrayList<Entry>();
        for (int i = 0; i < visitsPerMonth2016.length; i++)
        {
            entries2016.add(new Entry(i+1, visitsPerMonth2016[i]));
            entries2017.add(new Entry(i+1, visitsPerMonth2017[i]));
        }
        //LineDataSet dataSet2016 = new LineDataSet(entries2016, "Visits 2016");
        LineDataSet dataSet2017 = new LineDataSet(entries2017, "Visits 2017");
        LineData ld = new LineData();
        ld.setValueTextColor(R.color.colorDashboardText);
        //ld.addDataSet(dataSet2016);
        ld.addDataSet(dataSet2017);
        lineChart.setData(ld);

        // Styling the chart:
        lineChart.setDrawGridBackground(false);

        // Formatting the labels shown on the x-axis (months)
        final String[] months = lineChart.getResources().getStringArray(R.array.months);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                    return months[(int) value-1];
            }
        };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        lineChart.invalidate();
    }

    public static void updatePieChartData()
    {
        int[] visitsPerMonth2016 = new int[12];
        int[] visitsPerMonth2017 = new int[12];
        DateTime now = DateTime.now();
        for (Visitor visitor : visitors)
        {
            for (Visit visit : visitor.visits)
            {
                if (now.year == visit.datetime.year)
                {
                    visitsPerMonth2017[visit.datetime.month - 1]++;
                }
                else if (2016 == visit.datetime.year)
                {
                    visitsPerMonth2016[visit.datetime.month - 1]++;
                }
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        String[] months = pieChart.getResources().getStringArray(R.array.months);

        for(int i = 0; i < visitsPerMonth2017.length; i++)
        {
            entries.add(new PieEntry(visitsPerMonth2017[i], months[i]));
        }

        PieDataSet set = new PieDataSet(entries, "Election Results");
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
