package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Calendar;

public class EnterDatePagesActivity extends AppCompatActivity {

    int pages;
    String date;

    Button buttonSave;
    EditText editTextDate;
    NumberPicker numberPicker;

    DBHelper dbHelper;

    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_date_pages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonSave = (Button)findViewById(R.id.buttonSave);
        editTextDate = (EditText)findViewById(R.id.editTextDate);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setWrapSelectorWheel(false);

        dbHelper = new DBHelper(this);
        date = dbHelper.getTodayDateString();

        pages = dbHelper.getPages(date);


        editTextDate.setText(date);
        numberPicker.setValue(pages);
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

    public void setDate(View view) {
        new DatePickerDialog(EnterDatePagesActivity.this, dateSetListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickSave(View view) {
        pages = numberPicker.getValue();

        dbHelper.updatePages(date, pages);

        Intent intent = new Intent(EnterDatePagesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear++;
            String m;
            String d;
            if (monthOfYear < 9) {
                m = "0" + String.valueOf(monthOfYear);
            } else {
                m = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 9) {
                d = "0" + String.valueOf(dayOfMonth);
            } else {
                d = String.valueOf(dayOfMonth);
            }

            date = d + "." + m + "." + String.valueOf(year);
            pages = dbHelper.getPages(date);

            editTextDate.setText(date);
            numberPicker.setValue(pages);

        }
    };
}
