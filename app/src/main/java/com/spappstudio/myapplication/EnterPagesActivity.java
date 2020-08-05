package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class EnterPagesActivity extends AppCompatActivity {

    int pageCount;
    String type;

    Button buttonSave;
    NumberPicker numberPicker;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        pageCount = dbHelper.getPagesToday();

        buttonSave = (Button)findViewById(R.id.buttonSave);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setWrapSelectorWheel(false);

        type = getIntent().getExtras().getString("type");
        if (type.equals("add")) {
            numberPicker.setValue(0);
        }
        if (type.equals("edit")) {
            numberPicker.setValue(pageCount);
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
        if (type.equals("add")) {
            insertPageCount += pageCount;
        }
        dbHelper.updatePages(insertPageCount);

        setResult(RESULT_OK);
        finish();
    }
}
