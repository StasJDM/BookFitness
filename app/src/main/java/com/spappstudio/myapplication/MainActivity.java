package com.spappstudio.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.spappstudio.myapplication.mainfragments.BooksFragment;
import com.spappstudio.myapplication.mainfragments.HomeFragment;
import com.spappstudio.myapplication.mainfragments.ProfileFragment;
import com.spappstudio.myapplication.objects.Book;


public class MainActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "BookFitnessData";
    private static final String APP_PREFERENCES_LAST_BOOK_ID = "last_book_id";
    private static final String APP_PREFERENCES_GOAL = "goal";
    private static final String APP_PREFERENCES_GRAPH_TYPE = "graph_type";
    private static final String APP_PREFERENCES_GRAPH_TYPE_MONTH = "graph_type_month";

    private static final int NOTIFY_ID = 1;
    private static String CHANNEL_ID = "BookFitness channel";
    private static String CHANNEL_DESCRIPRION = "Notification channel for BookFitness app";

    String[] daysOfWeek;

    SharedPreferences sharedPreferences;
    DBHelper dbHelper;

    int pageCount;
    int yesterdayPageCount;
    int booksCount;
    int[] week;
    int[] month;
    int bookProgress;
    int dayOfWeek;
    int graphType;
    int graphTypeMonth;
    int goal;
    String bookTitle;

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment;
    BooksFragment booksFragment;
    ProfileFragment profileFragment;

    ImageButton imageButtonBar;
    ImageButton imageButtonLine;
    ImageButton imageButtonBarWeek;
    ImageButton imageButtonLineWeek;
    ImageButton imageButtonBarMonth;
    ImageButton imageButtonLineMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_2);

        createNotificationChannel();

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_book_white_48dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(NOTIFY_ID, builder.build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });



        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        dbHelper = new DBHelper(this);

        if (sharedPreferences.contains(APP_PREFERENCES_GOAL)) {
            goal = sharedPreferences.getInt(APP_PREFERENCES_GOAL, 20);
        } else {
            sharedPreferences.edit().putInt(APP_PREFERENCES_GOAL, 20).apply();
            goal = 20;
        }

        if(sharedPreferences.contains(APP_PREFERENCES_GRAPH_TYPE))
        {
            graphType = sharedPreferences.getInt(APP_PREFERENCES_GRAPH_TYPE, 0);
        } else {
            sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE, 0).apply();
            graphType = 0;
        }

        if(sharedPreferences.contains(APP_PREFERENCES_GRAPH_TYPE_MONTH))
        {
            graphTypeMonth = sharedPreferences.getInt(APP_PREFERENCES_GRAPH_TYPE_MONTH, 0);
        } else {
            sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE_MONTH, 0).apply();
            graphTypeMonth = 0;
        }

        homeFragment = new HomeFragment();
        booksFragment = new BooksFragment();
        profileFragment = new ProfileFragment();

        daysOfWeek = getResources().getStringArray(R.array.days_of_week);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_home:

                                Bundle bundle = new Bundle();
                                pageCount = dbHelper.getPagesToday();
                                dayOfWeek = dbHelper.getTodayDayOfWeek();
                                pageCount = dbHelper.getPagesToday();
                                yesterdayPageCount = dbHelper.getPagesYesterday();
                                week = dbHelper.getPagesPerWeek();
                                booksCount = dbHelper.getBooksCount();
                                if (booksCount == 0) {
                                    bookTitle = "Нажмите, чтобы добавить книгу";
                                } else {
                                    Book book;
                                    if (sharedPreferences.contains(APP_PREFERENCES_LAST_BOOK_ID)) {
                                        try {
                                            int last_book_id = sharedPreferences.getInt(APP_PREFERENCES_LAST_BOOK_ID, 0);
                                            book = dbHelper.getBookByID(last_book_id);
                                        } catch (Exception e) {
                                            book = dbHelper.getLastInsertedBook();
                                        }
                                    } else {
                                        book = dbHelper.getLastInsertedBook();
                                    }
                                    bookTitle = book.getTitle();
                                    bookProgress = book.getPercent();
                                }
                                goal = sharedPreferences.getInt(APP_PREFERENCES_GOAL, 20);
                                bundle.putInt("graphType", graphType);
                                bundle.putInt("goal", goal);
                                bundle.putInt("yesterday", yesterdayPageCount);
                                bundle.putInt("pageCount", pageCount);
                                bundle.putIntArray("week", week);
                                bundle.putInt("dayOfWeek", dayOfWeek);
                                bundle.putInt("bookProgress", bookProgress);
                                bundle.putString("bookTitle", bookTitle);
                                homeFragment.setArguments(bundle);
                                loadFragment(homeFragment);
                                return true;

                            case R.id.action_books:
                                loadFragment(booksFragment);
                                return true;
                            case R.id.action_profile:

                                int highScore = dbHelper.getHighScore();
                                int yesterday = dbHelper.getPagesYesterday();
                                int for_week = dbHelper.getPagesForWeek();
                                int for_month = dbHelper.getPagesForMount();
                                dayOfWeek = dbHelper.getTodayDayOfWeek();
                                week = dbHelper.getPagesPerWeek();
                                month = dbHelper.getPagesPerMonth();

                                bundle = new Bundle();
                                bundle.putInt("high_score", highScore);
                                bundle.putInt("today", pageCount);
                                bundle.putInt("yesterday", yesterday);
                                bundle.putInt("for_week", for_week);
                                bundle.putInt("for_month", for_month);
                                bundle.putIntArray("week", week);
                                bundle.putIntArray("month", month);
                                bundle.putInt("dayOfWeek", dayOfWeek);
                                bundle.putInt("graphTypeWeek", graphType);
                                bundle.putInt("graphTypeMonth", graphTypeMonth);
                                profileFragment.setArguments(bundle);

                                loadFragment(profileFragment);
                                return true;
                        }
                        return false;
                    }
                });
    }

    public void onClickEditGoal(View view) {
        Intent intent = new Intent(MainActivity.this, EditGoalActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            /*case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.refreshDrawableState();
    }

    public void onClickEditPages(View view) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onClickBooks(View view) {
        if (booksCount == 0) {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivityForResult(intent, 1);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_books);
        }
    }

    public void onClickAddBook (View view){
        Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
        startActivity(intent);
    }

    public void onClickLineGraph(View view) {
        graphType = 1;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE, 1).apply();
        imageButtonBar = findViewById(R.id.imageButtonBar);
        imageButtonLine = findViewById(R.id.imageButtonLine);
        imageButtonBar.setBackgroundColor(Color.WHITE);
        imageButtonLine.setBackgroundResource(R.drawable.rounded_corners_4);
        createGraph();
    }

    public void onClickBarGraph(View view) {
        graphType = 0;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE, 0).apply();
        imageButtonBar = findViewById(R.id.imageButtonBar);
        imageButtonLine = findViewById(R.id.imageButtonLine);
        imageButtonBar.setBackgroundResource(R.drawable.rounded_corners_4);
        imageButtonLine.setBackgroundColor(Color.WHITE);
        createGraph();
    }

    public void onClickLineGraphWeek(View view) {
        graphType = 1;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE, 1).apply();
        imageButtonBarWeek = findViewById(R.id.imageButtonBarWeek);
        imageButtonLineWeek =findViewById(R.id.imageButtonLineWeek);
        imageButtonBarWeek.setBackgroundColor(Color.WHITE);
        imageButtonLineWeek.setBackgroundResource(R.drawable.rounded_corners_4);
        createGraphWeek();
    }

    public void onClickBarGraphWeek(View view) {
        graphType = 0;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE, 0).apply();
        imageButtonBarWeek = findViewById(R.id.imageButtonBarWeek);
        imageButtonLineWeek = findViewById(R.id.imageButtonLineWeek);
        imageButtonBarWeek.setBackgroundResource(R.drawable.rounded_corners_4);
        imageButtonLineWeek.setBackgroundColor(Color.WHITE);
        createGraphWeek();
    }

    public void onClickLineGraphMonth(View view) {
        graphTypeMonth = 1;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE_MONTH, 1).apply();
        imageButtonBarMonth = findViewById(R.id.imageButtonBarMonth);
        imageButtonLineMonth = findViewById(R.id.imageButtonLineMonth);
        imageButtonBarMonth.setBackgroundColor(Color.WHITE);
        imageButtonLineMonth.setBackgroundResource(R.drawable.rounded_corners_4);
        createGraphMonth();
    }

    public void onClickBarGraphMonth(View view) {
        graphTypeMonth = 0;
        sharedPreferences.edit().putInt(APP_PREFERENCES_GRAPH_TYPE_MONTH, 0).apply();
        imageButtonBarMonth = findViewById(R.id.imageButtonBarMonth);
        imageButtonLineMonth = findViewById(R.id.imageButtonLineMonth);
        imageButtonBarMonth.setBackgroundResource(R.drawable.rounded_corners_4);
        imageButtonLineMonth.setBackgroundColor(Color.WHITE);
        createGraphMonth();
    }

    public void createGraph() {

        GraphView graph = findViewById(R.id.graph);
        graph.removeAllSeries();

        DataPoint[] dataPoint = new DataPoint[7];
        for (int i = 0; i < 7; i++) {
            dataPoint[i] = new DataPoint(i, week[6 - i]);
        }

        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<>(dataPoint);
        LineGraphSeries<DataPoint> line_series = new LineGraphSeries<>(dataPoint);
        bar_series.setColor(Color.parseColor("#388E3C"));
        line_series.setColor(Color.parseColor("#388E3C"));
        graph.setVisibility(View.INVISIBLE);
        graph.getGridLabelRenderer().setLabelsSpace(3);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(7);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHumanRounding(false, true);

        if (graphType == 0){
            bar_series.setDataWidth(0.5);
            graph.addSeries(bar_series);
        } else {
            graph.addSeries(line_series);
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
        graph.setVisibility(View.VISIBLE);
    }

    public void createGraphWeek() {
        GraphView graphViewWeek = findViewById(R.id.graphWeek);
        graphViewWeek.removeAllSeries();
        DataPoint[] dataPoint = new DataPoint[7];
        for (int i = 0; i < 7; i++) {
            dataPoint[i] = new DataPoint(i, week[6 - i]);
        }

        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<>(dataPoint);
        LineGraphSeries<DataPoint> line_series = new LineGraphSeries<>(dataPoint);
        bar_series.setColor(Color.parseColor("#388E3C"));
        line_series.setColor(Color.parseColor("#388E3C"));
        graphViewWeek.setVisibility(View.INVISIBLE);
        graphViewWeek.getGridLabelRenderer().setLabelsSpace(3);
        graphViewWeek.getViewport().setXAxisBoundsManual(true);
        graphViewWeek.getViewport().setMinY(0);
        graphViewWeek.getViewport().setMinX(0);
        graphViewWeek.getViewport().setMaxX(7);
        graphViewWeek.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphViewWeek.getGridLabelRenderer().setHumanRounding(false, true);

        imageButtonBarWeek.setBackgroundColor(Color.WHITE);
        imageButtonLineWeek.setBackgroundColor(Color.WHITE);

        if (graphType == 0){
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
        graphViewWeek.setVisibility(View.VISIBLE);
    }

    public void createGraphMonth()
    {
        GraphView graphViewMonth = findViewById(R.id.graphMonth);
        graphViewMonth.removeAllSeries();
        int dayInMonth = month.length;
        DataPoint[] dataPointMonth = new DataPoint[dayInMonth];
        for (int i = 0; i < dayInMonth; i++) {
            dataPointMonth[i] = new DataPoint(i, month[dayInMonth - i - 1]);
        }

        BarGraphSeries<DataPoint> bar_series_month = new BarGraphSeries<>(dataPointMonth);
        LineGraphSeries<DataPoint> line_series_month = new LineGraphSeries<>(dataPointMonth);
        bar_series_month.setColor(Color.parseColor("#388E3C"));
        line_series_month.setColor(Color.parseColor("#388E3C"));
        graphViewMonth.setVisibility(View.INVISIBLE);
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

        if (graphTypeMonth == 0){
            bar_series_month.setDataWidth(0.5);
            graphViewMonth.addSeries(bar_series_month);
        } else {
            graphViewMonth.addSeries(line_series_month);
        }
        graphViewMonth.setVisibility(View.VISIBLE);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = CHANNEL_DESCRIPRION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}