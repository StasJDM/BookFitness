package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spappstudio.myapplication.adapters.BooksRecyclerAdapter;
import com.spappstudio.myapplication.objects.Book;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    ArrayList<Book> books;
    TextView textViewEmptyBookMessage;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BooksRecyclerAdapter recyclerAdapter;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewEmptyBookMessage = findViewById(R.id.textViewEmpty);

        dbHelper = new DBHelper(this);
        books = dbHelper.getAllCurrentBooks();
        if (books.size() > 0) {
            textViewEmptyBookMessage.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.recyclerViewBooks);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new BooksRecyclerAdapter(this, books, "add_pages");
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        books = dbHelper.getAllCurrentBooks();
        recyclerAdapter.updateData(books, "add_pages");
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

    public void onClickAdd(View view) {
        Intent intent = new Intent(EditActivity.this, EnterPagesActivity.class);
        intent.putExtra("type", "add_without_book");
        startActivity(intent);
        finish();
    }

    public void onClickEdit(View view) {
        Intent intent = new Intent(EditActivity.this, EnterPagesActivity.class);
        intent.putExtra("type", "edit");
        startActivity(intent);
        finish();
    }

    public void onClickEditMoreDay(View view) {
        Intent intent = new Intent(EditActivity.this, EnterDatePagesActivity.class);
        startActivity(intent);
        finish();
    }
}
