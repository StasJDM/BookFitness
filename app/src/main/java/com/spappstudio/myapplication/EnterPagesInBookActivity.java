package com.spappstudio.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EnterPagesInBookActivity extends AppCompatActivity {

    int book_id;
    Book book;

    Button buttonPlus;
    Button buttonMinus;
    Button buttonSave;
    CheckBox checkBoxIsAdd;
    EditText editTextPages;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pages_in_book);

        buttonMinus = (Button)findViewById(R.id.buttonMinus);
        buttonPlus = (Button)findViewById(R.id.buttonPlus);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        checkBoxIsAdd = (CheckBox)findViewById(R.id.checkBoxIsAdd);
        editTextPages = (EditText)findViewById(R.id.editTextPages);

        editTextPages.setHint("0");

        dbHelper = new DBHelper(this);
        book_id = getIntent().getExtras().getInt("book_id");
        book = dbHelper.getBookByID(book_id);

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
        boolean isAdd = checkBoxIsAdd.isChecked();
        String c = editTextPages.getText().toString();
        int a = Integer.parseInt(c);

        dbHelper.updatePageInBook(book.id, book.addPages(a), book.pagesAll);
        if (isAdd) {
            dbHelper.updateAddPages(a);
        }

        Intent intent = new Intent();
        setResult(RESULT_OK);
        finish();
    }
}
