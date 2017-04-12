package no.byteme.magnuspoppe.dashboard;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import static no.byteme.magnuspoppe.dashboard.ViewControllerActivity.dashboardView;
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
    private static LineData lineData;

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

        // Getting view compontents from xml file:
        time_online = (TextView) view.findViewById(R.id.timer_online_value);
        unique_visitor = (TextView) view.findViewById(R.id.unique_visitor_value);
        unique_visits = (TextView) view.findViewById(R.id.unique_visits_value);
        visits_month = (TextView) view.findViewById(R.id.visitor_month_value);
        visits_total = (TextView) view.findViewById(R.id.visitor_total_value);
        lineChart = (LineChart) view.findViewById(R.id.line_chart);

        // Setting up and styling the chart:
        lineData = new LineData();
        lineData.setValueTextColor(R.color.colorDashboardText);
        lineChart.setData(lineData);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        XAxis xAxis = lineChart.getXAxis();

        // EDITING SETTINGS WHILE IN PORTRAIT TO MAKE THE CHART LOOK PRETTY:
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
        {
            lineChart.setDrawGridBackground(false);
            lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

            leftAxis.setDrawAxisLine(false);    leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawGridLines(false);   leftAxis.removeAllLimitLines();

            rightAxis.setDrawAxisLine(false);    rightAxis.setDrawZeroLine(false);
            rightAxis.setDrawGridLines(false);   rightAxis.removeAllLimitLines();

            xAxis.setDrawGridLines(false);      xAxis.setDrawLabels(false);
        }
        xAxis.setTextColor(Color.WHITE);        xAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.WHITE);     leftAxis.setAxisMinimum(1f);
        rightAxis.setTextColor(Color.WHITE);    rightAxis.setAxisMinimum(1f);

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

        updateChartData();
    }

    public static void updateChartData()
    {
        // Formatting chart data:
        int[][] visits = new int[12][];
        DateTime now = DateTime.now();
        for (int i = 1; i <= visits.length; i++)
        {
            if( i == 2 )         visits[i-1] = new int[28];
            else if( i % 2 == 0) visits[i-1] = new int[30];
            else if( i % 2 != 0) visits[i-1] = new int[31];
        }

        for (Visitor visitor : visitors)
        {
            for (Visit visit : visitor.visits)
            {
                if (now.year == visit.datetime.year)
                {
                    visits[visit.datetime.month-1][visit.datetime.day-1]++;
                }
            }
        }

        // Creating entries for the chart based on the data above:
        List<ArrayList<Entry>> entries = new ArrayList<>();
        int dayInYear = 0;
        for (int i = 0; i < visits.length; i++)
        {
            ArrayList<Entry> entriesMonth = new ArrayList<Entry>();
            for(int j = 0; j < visits[i].length; j++)
            {
                // Set full year:
                //entriesMonth.add(new Entry(dayInYear++, visits[i][j]));
                // Set visits per day overlapping for all months:
                entriesMonth.add(new Entry(j+1, visits[i][j]));
            }
            entries.add(entriesMonth);
        }

        for( List <Entry> datalist : entries )
        {
            LineDataSet dataSet = new LineDataSet(datalist, "");
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(2f);
            dataSet.setColor(dashboardView.getResources().getColor(R.color.colorGraphLine));
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setFillColor(dashboardView.getResources().getColor(R.color.colorGraphFill));
            dataSet.setDrawFilled(true);
            dataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });
            lineData.addDataSet(dataSet);
        }

        // Updating the chart.
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}
