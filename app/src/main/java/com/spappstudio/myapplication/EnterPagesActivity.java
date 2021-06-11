package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;

public class EnterPagesActivity extends AppCompatActivity {

    public static final String ADD_IN_BOOK = "add_in_book";
    public static final String ADD_WITHOUT_BOOK = "add_without_book";
    public static final String EDIT = "edit";

    int book_id;
    int pageCount;
    int pages;
    String type;

    Button buttonSave;
    NumberPicker numberPicker;
    AdView adView;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        adView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        dbHelper = new DBHelper(this);

        pages = dbHelper.getPagesToday();

        buttonSave = (Button)findViewById(R.id.buttonSave);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(99999);
        numberPicker.setWrapSelectorWheel(false);

        type = getIntent().getExtras().getString("type");
        if (type.equals(ADD_WITHOUT_BOOK)) {
            numberPicker.setValue(0);
        } else if (type.equals(ADD_IN_BOOK)) {
            book_id = getIntent().getExtras().getInt("book_id");
            pageCount = dbHelper.getPagesInBook(book_id);
            numberPicker.setValue(pageCount);
        } else if (type.equals(EDIT)) {
            numberPicker.setValue(pages);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickSave(View view) {
        int insertPageCount = numberPicker.getValue();
        if (type.equals(ADD_WITHOUT_BOOK)) {
            dbHelper.insertPages(insertPageCount);
        } else if(type.equals(ADD_IN_BOOK)) {
            dbHelper.updatePageInBook(book_id, insertPageCount);
            dbHelper.insertPages(insertPageCount - pageCount, book_id);
        } else if (type.equals(EDIT)) {
            int new_pages_value = numberPicker.getValue();
            int d_pages = new_pages_value - pages;
            dbHelper.insertPages(d_pages);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
