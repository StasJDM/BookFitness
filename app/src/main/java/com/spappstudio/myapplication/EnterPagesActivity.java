package com.spappstudio.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterPagesActivity extends AppCompatActivity {

    int pageCount;

    String type;
    String dateStr;
    Button buttonPlus;
    Button buttonMinus;
    Button buttonSave;
    EditText editTextPages;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pages);

        buttonMinus = (Button)findViewById(R.id.buttonMinus);
        buttonPlus = (Button)findViewById(R.id.buttonPlus);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        editTextPages = (EditText)findViewById(R.id.editTextPages);

        dbHelper = new DBHelper(this);

        pageCount = dbHelper.getPagesToday();

        type = getIntent().getExtras().getString("type");
        if (type.equals("add")) {
            editTextPages.setHint("0");
        }
        if (type.equals("edit")) {
            if (pageCount > 0) {
                editTextPages.setText(String.valueOf(pageCount));
            } else {
                editTextPages.setHint("0");
            }
        }
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

    public void onClickSave(View view) {
        String c = editTextPages.getText().toString();
        int a = Integer.parseInt(c);

        if (type.equals("add")) {
            a += pageCount;
        }

        dbHelper.updatePages(a);

        Intent intent = new Intent(EnterPagesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
