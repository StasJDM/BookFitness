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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    TextView textViewPageTitle;
    TextView textViewPagesAll;
    TextView textViewPagesAllTitle;
    TextView textViewEndingYear;
    TextView textViewEndingYearTitle;
    ImageButton buttonAdd;
    Button buttonChange;

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

        textViewBookTitle = findViewById(R.id.textViewBookTitle);
        textViewPage = findViewById(R.id.textViewPage);
        textViewPageTitle = findViewById(R.id.textViewPageTitle);
        textViewPagesAll = findViewById(R.id.textViewPagesAll);
        textViewPagesAllTitle = findViewById(R.id.textViewPagesAllTitle);
        textViewEndingYear = findViewById(R.id.textViewEndingYear);
        textViewEndingYearTitle = findViewById(R.id.textViewEndingYearTitle);
        buttonAdd = findViewById(R.id.imageButtonAdd);
        buttonChange = findViewById(R.id.buttonChange);

        book_id = getIntent().getExtras().getInt("book_id");

        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(APP_PREFERENCES_LAST_BOOK_ID, book_id).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        book = dbHelper.getBookByID(book_id);
        switch (book.type) {
            case "current":
                textViewPagesAll.setText(String.valueOf(book.pagesAll));
                textViewPage.setText(String.valueOf(book.page));

                textViewEndingYear.setVisibility(View.GONE);
                textViewEndingYearTitle.setVisibility(View.GONE);
                buttonAdd.setVisibility(View.VISIBLE);

                buttonChange.setVisibility(View.VISIBLE);
                buttonChange.setText(getString(R.string.finish_book));
                break;
            case "archive":
                textViewPage.setVisibility(View.GONE);
                textViewPageTitle.setVisibility(View.GONE);
                textViewPagesAll.setVisibility(View.GONE);
                textViewPagesAllTitle.setVisibility(View.GONE);
                buttonAdd.setVisibility(View.GONE);

                buttonChange.setVisibility(View.GONE);
                break;
            case "wishful":
                textViewPagesAll.setVisibility(View.GONE);
                textViewPage.setVisibility(View.GONE);
                textViewPage.setVisibility(View.GONE);
                textViewPageTitle.setVisibility(View.GONE);
                textViewPagesAll.setVisibility(View.GONE);
                textViewPagesAllTitle.setVisibility(View.GONE);
                textViewEndingYear.setVisibility(View.GONE);
                textViewEndingYearTitle.setVisibility(View.GONE);
                buttonAdd.setVisibility(View.GONE);

                buttonChange.setVisibility(View.VISIBLE);
                buttonChange.setText(getString(R.string.start_reading));
                break;
            default:
                break;

        }
        textViewBookTitle.setText(book.getTitle());
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

    public void onClickAddPages(View view) {
        Intent intent = new Intent(OneBookActivity.this, EnterPagesInBookActivity.class);
        intent.putExtra("book_id", book_id);
        startActivityForResult(intent, REQUEST_ADD_PAGES);
    }

    public void onClickChange(View view) {
        switch (book.type) {
            case "current":
                dbHelper.finishBook(book_id);
                finish();
            case "wishful":
                Intent intent = new Intent(OneBookActivity.this, AddBookActivity.class);
                intent.putExtra("book_id", book_id);
                intent.putExtra("is_start_reading", 1);
                startActivity(intent);
                finish();
        }
    }
}
