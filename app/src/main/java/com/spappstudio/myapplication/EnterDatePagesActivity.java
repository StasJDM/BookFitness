package com.spappstudio.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class EnterDatePagesActivity extends AppCompatActivity {

    int pages;
    String date;

    Button buttonPlus;
    Button buttonMinus;
    Button buttonSave;
    EditText editTextPages;
    EditText editTextDate;

    DBHelper dbHelper;

    Calendar dateAndTime=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_date_pages);

        buttonMinus = (Button)findViewById(R.id.buttonMinus);
        buttonPlus = (Button)findViewById(R.id.buttonPlus);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        editTextPages = (EditText)findViewById(R.id.editTextPages);
        editTextDate = (EditText)findViewById(R.id.editTextDate);

        dbHelper = new DBHelper(this);
        date = dbHelper.getTodayDateString();

        pages = dbHelper.getPages(date);


        editTextDate.setText(date);
        editTextPages.setText(String.valueOf(pages));
    }

    public void onClickPlus(View view) {
        String c = editTextPages.getText().toString();
        if (!c.isEmpty()) {
            int a = Integer.parseInt(c);
            a++;
            editTextPages.setText(String.valueOf(a));
        } else {
            editTextPages.setText("1");
        }
    }

    public void onClickMinus(View view) {
        String c = editTextPages.getText().toString();
        if (!c.isEmpty()) {
            int a = Integer.parseInt(c);
            if (a == 0) {
                a = 0;
            } else {
                a--;
            }
            editTextPages.setText(String.valueOf(a));
        } else {
            editTextPages.setText("0");
        }
    }

    public void setDate(View view) {
        new DatePickerDialog(EnterDatePagesActivity.this, dateSetListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickSave(View view) {
        String c = editTextPages.getText().toString();
        pages = Integer.parseInt(c);

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

            date = d + "." + m + "." +String.valueOf(year);
            pages = dbHelper.getPages(date);

            editTextDate.setText(date);
            editTextPages.setText(String.valueOf(pages));

        }
    };
}
