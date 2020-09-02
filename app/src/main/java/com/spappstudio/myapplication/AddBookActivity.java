package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.spappstudio.myapplication.dialogs.BackDialog;
import com.spappstudio.myapplication.objects.Book;


public class AddBookActivity extends AppCompatActivity {

    int book_id;
    String type;

    DBHelper dbHelper;

    Chip chip_current;
    Chip chip_archive;
    Chip chip_wishful;
    ChipGroup chipGroup;
    EditText editTextAuthor;
    EditText editTextName;
    EditText editTextPagesAll;
    EditText editTextPage;
    EditText editTextDate;
    TextView textViewTitle;
    TextView textViewPagesAll;
    TextView textViewPageNow;
    TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        chip_current = findViewById(R.id.chip_current);
        chip_archive = findViewById(R.id.chip_archive);
        chip_wishful = findViewById(R.id.chip_wishful);
        chipGroup = findViewById(R.id.chip_group);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextName = findViewById(R.id.editTextName);
        editTextPagesAll = findViewById(R.id.editTextPagesAll);
        editTextPage = findViewById(R.id.editTextPage);
        editTextDate = findViewById(R.id.editTextDate);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPagesAll = findViewById(R.id.textViewNumberOfPages);
        textViewPageNow = findViewById(R.id.textViewPageNow);

        editTextDate.setVisibility(View.GONE);
        textViewDate.setVisibility(View.GONE);

        chip_current.setChecked(true);

        type = "current";

        chip_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "current";
                textViewPagesAll.setVisibility(View.VISIBLE);
                textViewPageNow.setVisibility(View.VISIBLE);
                textViewDate.setVisibility(View.GONE);
                editTextPage.setVisibility(View.VISIBLE);
                editTextPagesAll.setVisibility(View.VISIBLE);
                editTextDate.setVisibility(View.GONE);

            }
        });

        chip_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "archive";
                textViewPagesAll.setVisibility(View.GONE);
                textViewPageNow.setVisibility(View.GONE);
                textViewDate.setVisibility(View.VISIBLE);
                editTextPage.setVisibility(View.GONE);
                editTextPagesAll.setVisibility(View.GONE);
                editTextDate.setVisibility(View.VISIBLE);
            }
        });

        chip_wishful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "wishful";
                textViewPagesAll.setVisibility(View.GONE);
                textViewPageNow.setVisibility(View.GONE);
                textViewDate.setVisibility(View.GONE);
                editTextPage.setVisibility(View.GONE);
                editTextPagesAll.setVisibility(View.GONE);
                editTextDate.setVisibility(View.GONE);
            }
        });

        dbHelper = new DBHelper(this);

        book_id = getIntent().getIntExtra("book_id", -1);
        if (book_id != -1) {
            textViewTitle.setText(getString(R.string.edit_book));
            Book book = dbHelper.getBookByID(book_id);
            int mode = getIntent().getIntExtra("is_start_reading", 0);
            if (mode == 1) {
                book.type = "current";
            }
            type = book.type;
            editTextAuthor.setText(book.author);
            editTextName.setText(book.name);
            switch (type) {
                case "current":
                    if (book.pagesAll != 0) {
                        editTextPagesAll.setText(String.valueOf(book.pagesAll));
                    }
                    if (book.page != 0) {
                        editTextPage.setText(String.valueOf(book.page));
                    }
                    editTextDate.setVisibility(View.GONE);
                    textViewDate.setVisibility(View.GONE);
                    break;
                case "archive":
                    editTextPagesAll.setVisibility(View.GONE);
                    editTextPage.setVisibility(View.GONE);
                    textViewPagesAll.setVisibility(View.GONE);
                    textViewPageNow.setVisibility(View.GONE);
                    editTextDate.setText(book.end_year);
                    break;
                case "wishful":
                    editTextPagesAll.setVisibility(View.GONE);
                    editTextPage.setVisibility(View.GONE);
                    editTextDate.setVisibility(View.GONE);
                    textViewPagesAll.setVisibility(View.GONE);
                    textViewPageNow.setVisibility(View.GONE);
                    textViewDate.setVisibility(View.GONE);
                    break;
            }
            chipGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickAdd(View view) {

        if (!editTextAuthor.getText().toString().isEmpty()) {

            if (!editTextName.getText().toString().isEmpty()) {

                switch (type) {
                    case "current":
                        saveUnreadBook();
                        break;
                    case "archive":
                        saveArchiveBook();
                        break;
                    case "wishful":
                        saveWishfulBook();
                }
            } else {
                editTextName.setHint(getString(R.string.enter_book_name));
                editTextName.setHintTextColor(Color.RED);
            }
        } else {
            editTextAuthor.setHint(getString(R.string.enter_book_author));
            editTextAuthor.setHintTextColor(Color.RED);
        }
    }

    private void saveUnreadBook() {
        if (!editTextPagesAll.getText().toString().isEmpty()) {

            if (!editTextPage.getText().toString().isEmpty()) {

                Book book = new Book(
                        book_id,
                        editTextAuthor.getText().toString(),
                        editTextName.getText().toString(),
                        type,
                        Integer.valueOf(editTextPagesAll.getText().toString()),
                        Integer.valueOf(editTextPage.getText().toString()),
                        dbHelper.getTodayDateString()
                );

                if (book_id == -1) {
                    dbHelper.insertBook(book, type);
                } else {
                    dbHelper.updateBook(book, type);
                }

                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                editTextPage.setHint(getString(R.string.enter_page_number));
                editTextPage.setHintTextColor(Color.RED);
            }
        } else {
            editTextPagesAll.setHint(getString(R.string.enter_page_count));
            editTextPagesAll.setHintTextColor(Color.RED);
        }
    }

    private void saveArchiveBook() {
        Book book = new Book(
                book_id,
                editTextAuthor.getText().toString(),
                editTextName.getText().toString(),
                "archive",
                editTextDate.getText().toString()
        );
        if (book_id == -1) {
            dbHelper.insertArchiveBook(book);
        } else {
            dbHelper.updateArchiveBook(book);
        }
        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveWishfulBook() {
        Book book = new Book(
                book_id,
                editTextAuthor.getText().toString(),
                editTextName.getText().toString(),
                "wishful"
        );
        if (book_id == -1) {
            dbHelper.insertWishfulBook(book);
        } else {
            dbHelper.updateWishfulBook(book);
        }
        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        BackDialog backDialog = new BackDialog();
        backDialog.show(getSupportFragmentManager(), "Back");
    }
}
