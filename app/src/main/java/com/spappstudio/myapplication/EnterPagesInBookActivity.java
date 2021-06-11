package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.spappstudio.myapplication.objects.Book;

public class EnterPagesInBookActivity extends AppCompatActivity {

    int book_id;
    Book book;

    Button buttonSave;
    NumberPicker numberPicker;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pages_in_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonSave = (Button)findViewById(R.id.buttonSave);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(99999);
        numberPicker.setWrapSelectorWheel(false);

        dbHelper = new DBHelper(this);
        book_id = getIntent().getExtras().getInt("book_id");
        book = dbHelper.getBookByID(book_id);
        numberPicker.setValue(book.page);
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

        dbHelper.updatePageInBook(book.id, insertPageCount);
        dbHelper.insertPages(insertPageCount - book.page, book_id);

        setResult(RESULT_OK);
        finish();
    }
}
