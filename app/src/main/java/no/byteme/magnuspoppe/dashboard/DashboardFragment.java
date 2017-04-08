package no.byteme.magnuspoppe.dashboard;

import android.graphics.Color;
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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(1f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false);

        // Formatting the labels shown on the x-axis (months)
        final String[] months = lineChart.getResources().getStringArray(R.array.months_short);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return months[(int) value-1];
            }
        };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
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
        int[] visitsPerMonth2017 = new int[12];
        DateTime now = DateTime.now();
        for (Visitor visitor : visitors)
        {
            for (Visit visit : visitor.visits)
            {
                if (now.year == visit.datetime.year)
                    visitsPerMonth2017[visit.datetime.month - 1]++;
            }
        }

        // Creating entries for the chart based on the data above:
        List<Entry> entries2017 = new ArrayList<Entry>();
        for (int i = 0; i < visitsPerMonth2017.length; i++)
        {
            entries2017.add(new Entry(i+1, visitsPerMonth2017[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries2017, "Visits 2017");
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2f);
        dataSet.setColor(Color.WHITE);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setFillAlpha((int)(255f*0.54f)); // 54%
        dataSet.setFillColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });
        lineData.addDataSet(dataSet);

        // Updating the chart.
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}
