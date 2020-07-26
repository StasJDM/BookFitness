package com.spappstudio.myapplication.mainfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.spappstudio.myapplication.Quotes;
import com.spappstudio.myapplication.R;

public class HomeFragment extends Fragment {

    String[] daysOfWeek = {"", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

    int pageCount;
    int deltaPageCount;
    int dayOfWeek;
    int week[];
    int bookProgress;
    int graphType;
    String bookTitle;


    TextView textViewPageCount;
    TextView textViewDeltaPageCount;
    TextView textViewQuote;
    TextView textViewBookName;
    ProgressBar progressBarBookProgress;
    GraphView graph;
    ImageButton imageButtonBar;
    ImageButton imageButtonLine;


    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textViewPageCount = (TextView)rootView.findViewById(R.id.textViewPageCount);
        textViewDeltaPageCount = (TextView)rootView.findViewById(R.id.textViewDeltaPageCount);
        textViewQuote = (TextView)rootView.findViewById(R.id.textViewQuote);
        textViewBookName = (TextView)rootView.findViewById(R.id.textViewBookName);
        progressBarBookProgress = (ProgressBar)rootView.findViewById(R.id.progressBarBookProgress);
        graph = (GraphView) rootView.findViewById(R.id.graph);
        imageButtonBar = (ImageButton)rootView.findViewById(R.id.imageButtonBar);
        imageButtonLine = (ImageButton)rootView.findViewById(R.id.imageButtonLine);

        Bundle bundle = getArguments();
        pageCount = bundle.getInt("pageCount", 0);
        graphType = bundle.getInt("graphType", 0);
        deltaPageCount = bundle.getInt("deltaPageCount", 0);
        week = bundle.getIntArray("week");
        dayOfWeek = bundle.getInt("dayOfWeek", 1);
        bookTitle = bundle.getString("bookTitle");
        bookProgress = bundle.getInt("bookProgress");

        textViewPageCount.setText(String.valueOf(pageCount));
        textViewDeltaPageCount.setText(String.valueOf(deltaPageCount));
        textViewQuote.setText(Quotes.getRandom());
        textViewBookName.setText(bookTitle);
        if (bookTitle.equals("Нажмите, чтобы добавить книгу")) {
            progressBarBookProgress.setVisibility(View.INVISIBLE);
        } else {
            progressBarBookProgress.setProgress(bookProgress);
        }

        if (deltaPageCount > 0) {
            textViewDeltaPageCount.setText("+" + String.valueOf(deltaPageCount));
            textViewDeltaPageCount.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            textViewDeltaPageCount.setText(String.valueOf(deltaPageCount));
            if (deltaPageCount < 0) {
                textViewDeltaPageCount.setTextColor(Color.parseColor("#f44336"));
            }
        }

        DataPoint dataPoint[] = new DataPoint[7];
        for (int i = 0; i < 7; i++) {
            dataPoint[i] = new DataPoint(i, week[6 - i]);
        }


        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<DataPoint>(dataPoint);
        LineGraphSeries<DataPoint> line_series = new LineGraphSeries<DataPoint>(dataPoint);
        bar_series.setColor(Color.parseColor("#388E3C"));
        line_series.setColor(Color.parseColor("#388E3C"));
        graph.getGridLabelRenderer().setLabelsSpace(3);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(7);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHumanRounding(false, true);


        imageButtonBar.setBackgroundColor(Color.WHITE);
        imageButtonLine.setBackgroundColor(Color.WHITE);

        if (graphType == 0){
            bar_series.setDataWidth(0.5);
            graph.addSeries(bar_series);
            imageButtonBar.setBackgroundResource(R.drawable.rounded_corners_4);
        } else {
            graph.addSeries(line_series);
            imageButtonLine.setBackgroundResource(R.drawable.rounded_corners_4);
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{
                daysOfWeek[dayOfWeek],
                daysOfWeek[dayOfWeek + 1],
                daysOfWeek[dayOfWeek + 2],
                daysOfWeek[dayOfWeek + 3],
                daysOfWeek[dayOfWeek + 4],
                daysOfWeek[dayOfWeek + 5],
                daysOfWeek[dayOfWeek + 6],
                " "});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setDrawBorder(true);

        return rootView;
    }


}
