package com.spappstudio.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AddBookActivity extends AppCompatActivity {

    EditText editTextAuthor;
    EditText editTextName;
    EditText editTextPagesAll;
    EditText editTextPage;
    TextView textViewTitle;
    CheckBox checkBoxIsEnd;
    DBHelper dbHelper;
    int book_id;
    String author;
    String name;
    String pagesAll;
    String page;
    int is_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        editTextAuthor = (EditText)findViewById(R.id.editTextAuthor);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextPagesAll = (EditText)findViewById(R.id.editTextPagesAll);
        editTextPage = (EditText)findViewById(R.id.editTextPage);
        checkBoxIsEnd = (CheckBox)findViewById(R.id.checkBoxIsEnd);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);

        dbHelper = new DBHelper(this);

        book_id = getIntent().getIntExtra("book_id", -1);
        if (book_id != -1) {
            textViewTitle.setText("Редактировать книгу");
            Book book = dbHelper.getBookByID(book_id);
            editTextAuthor.setText(book.author);
            editTextName.setText(book.name);
            editTextPagesAll.setText(String.valueOf(book.pagesAll));
            editTextPage.setText(String.valueOf(book.page));
            checkBoxIsEnd.setChecked(book.is_end);
        }

    }

    public void onClickAdd(View view) {
        author = editTextAuthor.getText().toString();
        name = editTextName.getText().toString();
        pagesAll = editTextPagesAll.getText().toString();
        page = editTextPage.getText().toString();
        if (checkBoxIsEnd.isChecked()) {
            is_end = 1;
        } else {
            is_end = 0;
        }

        if (!author.isEmpty()) {

            if (!name.isEmpty()) {

                if (!pagesAll.isEmpty()) {

                    if (!page.isEmpty()) {

                        if (book_id == -1) {
                            dbHelper.insertBook(
                                    author,
                                    name,
                                    Integer.valueOf(pagesAll),
                                    Integer.valueOf(page),
                                    is_end
                            );
                        } else {
                            dbHelper.updateBook(book_id,
                                    author,
                                    name,
                                    Integer.valueOf(pagesAll),
                                    Integer.valueOf(page),
                                    is_end);
                        }

                        Intent intent = new Intent();
                        setResult(RESULT_OK);
                        finish();


                    } else {
                        editTextPage.setHint("Введите номер страницы");
                        editTextPage.setHintTextColor(Color.RED);
                    }
                } else {
                    editTextPagesAll.setHint("Введите количество страниц");
                    editTextPagesAll.setHintTextColor(Color.RED);
                }
            } else {
                editTextName.setHint("Введите название книги");
                editTextName.setHintTextColor(Color.RED);
            }
        } else {
            editTextAuthor.setHint("Введите автора книги");
            editTextAuthor.setHintTextColor(Color.RED);
        }

    }
}
