package com.spappstudio.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.spappstudio.myapplication.dialogs.DeleteDialog;
import com.spappstudio.myapplication.objects.Book;

public class OneBookActivity extends AppCompatActivity {


    private static final int REQUEST_ADD_PAGES = 1;
    private static final int REQUEST_EDIT_BOOK = 2;
    private static final String APP_PREFERENCES = "BookFitnessData";
    private static final String APP_PREFERENCES_LAST_BOOK_ID = "last_book_id";

    TextView textViewBookTitle;
    TextView textViewPage;
    TextView textViewPagesAll;
    CheckBox checkBoxIsEnd;

    Book book;
    int book_id;

    DBHelper dbHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewBookTitle = (TextView)findViewById(R.id.textViewBookTitle);
        textViewPage = (TextView)findViewById(R.id.textViewPage);
        textViewPagesAll = (TextView)findViewById(R.id.textViewPagesAll);
        checkBoxIsEnd = (CheckBox)findViewById(R.id.checkBoxIsEnd);

        book_id = getIntent().getExtras().getInt("book_id");

        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(APP_PREFERENCES_LAST_BOOK_ID, book_id).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        book = dbHelper.getBookByID(book_id);
        textViewBookTitle.setText(book.getTitle());
        textViewPagesAll.setText(String.valueOf(book.pagesAll));
        textViewPage.setText(String.valueOf(book.page));
        checkBoxIsEnd.setChecked(book.is_end);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.item_add_pages:
                intent = new Intent(OneBookActivity.this, EnterPagesInBookActivity.class);
                intent.putExtra("book_id", book_id);
                startActivityForResult(intent, REQUEST_ADD_PAGES);
                return true;
            case R.id.item_edit_book:
                intent = new Intent(OneBookActivity.this, AddBookActivity.class);
                intent.putExtra("book_id", book_id);
                startActivityForResult(intent, REQUEST_EDIT_BOOK);
                return true;
            case R.id.item_delete_book:
                DeleteDialog deleteDialog = new DeleteDialog();
                Bundle args = new Bundle();
                args.putInt("book_id", book_id);
                deleteDialog.setArguments(args);
                deleteDialog.show(getSupportFragmentManager(), "Delete");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickIsEnd(View view) {
        book.is_end = !book.is_end;
        dbHelper.updateIsEndInBook(book.id, book.is_end);
    }

    public void onClickAddPages(View view) {
        Intent intent = new Intent(OneBookActivity.this, EnterPagesInBookActivity.class);
        intent.putExtra("book_id", book_id);
        startActivityForResult(intent, REQUEST_ADD_PAGES);
    }
}
