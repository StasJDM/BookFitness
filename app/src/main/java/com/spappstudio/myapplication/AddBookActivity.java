package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.spappstudio.myapplication.dialogs.BackDialog;
import com.spappstudio.myapplication.objects.Book;

public class AddBookActivity extends AppCompatActivity {

    int book_id;

    DBHelper dbHelper;

    EditText editTextAuthor;
    EditText editTextName;
    EditText editTextPagesAll;
    EditText editTextPage;
    TextView textViewTitle;
    CheckBox checkBoxIsEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextAuthor = (EditText)findViewById(R.id.editTextAuthor);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextPagesAll = (EditText)findViewById(R.id.editTextPagesAll);
        editTextPage = (EditText)findViewById(R.id.editTextPage);
        checkBoxIsEnd = (CheckBox)findViewById(R.id.checkBoxIsEnd);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);

        dbHelper = new DBHelper(this);

        book_id = getIntent().getIntExtra("book_id", -1);
        if (book_id != -1) {
            textViewTitle.setText(getString(R.string.edit_book));
            Book book = dbHelper.getBookByID(book_id);
            editTextAuthor.setText(book.author);
            editTextName.setText(book.name);
            editTextPagesAll.setText(String.valueOf(book.pagesAll));
            editTextPage.setText(String.valueOf(book.page));
            checkBoxIsEnd.setChecked(book.is_end);
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
        int is_end;
        if (checkBoxIsEnd.isChecked()) {
            is_end = 1;
        } else {
            is_end = 0;
        }

        if (!editTextAuthor.getText().toString().isEmpty()) {

            if (!editTextName.getText().toString().isEmpty()) {

                if (!editTextPagesAll.getText().toString().isEmpty()) {

                    if (!editTextPage.getText().toString().isEmpty()) {

                        Book book = new Book(
                                book_id,
                                editTextAuthor.getText().toString(),
                                editTextName.getText().toString(),
                                Integer.valueOf(editTextPagesAll.getText().toString()),
                                Integer.valueOf(editTextPage.getText().toString()),
                                is_end
                        );

                        if (book_id == -1) {
                            dbHelper.insertBook(book);
                        } else {
                            dbHelper.updateBook(book);
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
            } else {
                editTextName.setHint(getString(R.string.enter_book_name));
                editTextName.setHintTextColor(Color.RED);
            }
        } else {
            editTextAuthor.setHint(getString(R.string.enter_book_author));
            editTextAuthor.setHintTextColor(Color.RED);
        }
    }

    @Override
    public void onBackPressed() {
        BackDialog backDialog = new BackDialog();
        backDialog.show(getSupportFragmentManager(), "Back");
    }
}
