package com.spappstudio.myapplication.mainfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.spappstudio.myapplication.R;

public class ProfileFragment extends Fragment {

    String[] daysOfWeek = {"", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

    int today;
    int yesterday;
    int for_week;
    int for_month;
    int dayOfWeek;
    int graphTypeWeek;
    int graphTypeMonth;
    int dayInMonth;
    int week[];
    int month[];
    int week_sr;
    int month_sr;

    TextView textViewToday;
    TextView textViewYesterday;
    TextView textViewWeek;
    TextView textViewMonth;
    TextView textViewWeekSr;
    TextView textViewMonthSr;

    GraphView graphViewWeek;
    GraphView graphViewMonth;
    ImageButton imageButtonBarWeek;
    ImageButton imageButtonLineWeek;
    ImageButton imageButtonBarMonth;
    ImageButton imageButtonLineMonth;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewToday = (TextView)rootView.findViewById(R.id.textViewToday);
        textViewYesterday = (TextView)rootView.findViewById(R.id.textViewYesterday);
        textViewWeek = (TextView)rootView.findViewById(R.id.textViewWeek);
        textViewMonth = (TextView)rootView.findViewById(R.id.textViewMonth);
        textViewWeekSr = (TextView)rootView.findViewById(R.id.textViewWeekSr);
        textViewMonthSr = (TextView)rootView.findViewById(R.id.textViewMonthSr);
        graphViewWeek = (GraphView)rootView.findViewById(R.id.graphWeek);
        graphViewMonth = (GraphView)rootView.findViewById(R.id.graphMonth);
        imageButtonBarWeek = (ImageButton)rootView.findViewById(R.id.imageButtonBarWeek);
        imageButtonBarMonth = (ImageButton)rootView.findViewById(R.id.imageButtonBarMonth);
        imageButtonLineWeek = (ImageButton)rootView.findViewById(R.id.imageButtonLineWeek);
        imageButtonLineMonth = (ImageButton)rootView.findViewById(R.id.imageButtonLineMonth);

        Bundle bundle = getArguments();
        today = bundle.getInt("today", 0);
        yesterday = bundle.getInt("yesterday", 0);
        for_week = bundle.getInt("for_week", 0);
        for_month = bundle.getInt("for_month", 0);
        week = bundle.getIntArray("week");
        dayOfWeek = bundle.getInt("dayOfWeek");
        graphTypeWeek = bundle.getInt("graphTypeWeek", 0);
        graphTypeMonth = bundle.getInt("graphTypeMonth", 0);
        month = bundle.getIntArray("month");

        dayInMonth = month.length;

        week_sr = for_week / 7;
        month_sr = for_month / month.length;


        textViewToday.setText(String.valueOf(today));
        textViewYesterday.setText(String.valueOf(yesterday));
        textViewWeek.setText(String.valueOf(for_week));
        textViewMonth.setText(String.valueOf(for_month));
        textViewWeekSr.setText(String.valueOf(week_sr));
        textViewMonthSr.setText(String.valueOf(month_sr));

        DataPoint dataPoint[] = new DataPoint[7];
        for (int i = 0; i < 7; i++) {
            dataPoint[i] = new DataPoint(i, week[6 - i]);
        }


        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<DataPoint>(dataPoint);
        LineGraphSeries<DataPoint> line_series = new LineGraphSeries<DataPoint>(dataPoint);
        bar_series.setColor(Color.parseColor("#388E3C"));
        line_series.setColor(Color.parseColor("#388E3C"));
        graphViewWeek.getGridLabelRenderer().setLabelsSpace(3);
        graphViewWeek.getViewport().setXAxisBoundsManual(true);
        graphViewWeek.getViewport().setMinY(0);
        graphViewWeek.getViewport().setMinX(0);
        graphViewWeek.getViewport().setMaxX(7);
        graphViewWeek.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphViewWeek.getGridLabelRenderer().setHumanRounding(false, true);

        imageButtonBarWeek.setBackgroundColor(Color.WHITE);
        imageButtonLineWeek.setBackgroundColor(Color.WHITE);

        if (graphTypeWeek == 0){
            bar_series.setDataWidth(0.5);
            graphViewWeek.addSeries(bar_series);
            imageButtonBarWeek.setBackgroundResource(R.drawable.rounded_corners_4);
        } else {
            graphViewWeek.addSeries(line_series);
            imageButtonLineWeek.setBackgroundResource(R.drawable.rounded_corners_4);
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphViewWeek);
        staticLabelsFormatter.setHorizontalLabels(new String[]{
                daysOfWeek[dayOfWeek],
                daysOfWeek[dayOfWeek + 1],
                daysOfWeek[dayOfWeek + 2],
                daysOfWeek[dayOfWeek + 3],
                daysOfWeek[dayOfWeek + 4],
                daysOfWeek[dayOfWeek + 5],
                daysOfWeek[dayOfWeek + 6],
                " "});
        graphViewWeek.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphViewWeek.getViewport().setDrawBorder(true);

        //     ГРАФИК ЗА МЕСЯЦ     //



        DataPoint dataPointMonth[] = new DataPoint[dayInMonth];
        for (int i = 0; i < dayInMonth; i++) {
            dataPointMonth[i] = new DataPoint(i, month[dayInMonth - i - 1]);
        }


        BarGraphSeries<DataPoint> bar_series_month = new BarGraphSeries<DataPoint>(dataPointMonth);
        LineGraphSeries<DataPoint> line_series_month = new LineGraphSeries<DataPoint>(dataPointMonth);
        bar_series_month.setColor(Color.parseColor("#388E3C"));
        line_series_month.setColor(Color.parseColor("#388E3C"));
        graphViewMonth.getGridLabelRenderer().setLabelsSpace(3);
        graphViewMonth.getViewport().setXAxisBoundsManual(true);
        graphViewMonth.getViewport().setMinY(0);
        graphViewMonth.getViewport().setMinX(0);
        graphViewMonth.getViewport().setMaxX(dayInMonth);
        graphViewMonth.getViewport().setDrawBorder(true);
        graphViewMonth.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphViewMonth.getGridLabelRenderer().setHumanRounding(false, true);
        StaticLabelsFormatter staticLabelsFormatterMonth = new StaticLabelsFormatter(graphViewMonth);
        staticLabelsFormatterMonth.setHorizontalLabels(new String[]{" ", " "});
        graphViewMonth.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterMonth);

        imageButtonBarMonth.setBackgroundColor(Color.WHITE);
        imageButtonLineMonth.setBackgroundColor(Color.WHITE);

        if (graphTypeMonth == 0){
            bar_series_month.setDataWidth(0.5);
            graphViewMonth.addSeries(bar_series_month);
            imageButtonBarMonth.setBackgroundResource(R.drawable.rounded_corners_4);
        } else {
            graphViewMonth.addSeries(line_series_month);
            imageButtonLineMonth.setBackgroundResource(R.drawable.rounded_corners_4);
        }



        return rootView;
    }
}
