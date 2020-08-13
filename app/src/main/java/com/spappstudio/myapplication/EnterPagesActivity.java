package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class EnterPagesActivity extends AppCompatActivity {

    public static final String ADD_IN_BOOK = "add_in_book";
    public static final String ADD_WITHOUT_BOOK = "add_without_book";

    int book_id;
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

        buttonSave = (Button)findViewById(R.id.buttonSave);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setWrapSelectorWheel(false);

        type = getIntent().getExtras().getString("type");
        if (type.equals(ADD_WITHOUT_BOOK)) {
            numberPicker.setValue(0);
        } else if (type.equals(ADD_IN_BOOK)) {
            book_id = getIntent().getExtras().getInt("book_id");
            pageCount = dbHelper.getPagesInBook(book_id);
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
        if (type.equals(ADD_WITHOUT_BOOK)) {
            dbHelper.insertPages(insertPageCount);
        } else if(type.equals(ADD_IN_BOOK)) {
            dbHelper.updatePageInBook(book_id, insertPageCount);
            dbHelper.insertPages(insertPageCount - pageCount, book_id);
        }
        setResult(RESULT_OK);
        finish();
    }
}
