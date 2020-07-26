package com.spappstudio.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooksActivity extends AppCompatActivity {

    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_PROGRESS = "progress";

    Button buttonAddBook;
    ListView listViewBooks;
    ArrayList<Book> books;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        dbHelper = new DBHelper(this);

        buttonAddBook = (Button)findViewById(R.id.buttonAddBook);
        listViewBooks = (ListView)findViewById(R.id.listViewBooks);
    }

    @Override
    protected void onResume() {
        super.onResume();

        books = dbHelper.getAllBooks();

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(books.size());
        Map<String, Object> map;
        for (int i = 0; i < books.size(); i++) {
            map = new HashMap<String, Object>();
            map.put(ATTRIBUTE_NAME_TEXT, books.get(i).getTitle());
            map.put(ATTRIBUTE_NAME_PROGRESS, books.get(i).getPercent());
            data.add(map);
        }

        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_PROGRESS};
        int[] to = {R.id.tvBookName, R.id.pbBookProgress};


        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.list_item_book, from, to);
        simpleAdapter.setViewBinder(new MyViewBinder());
        listViewBooks.setAdapter(simpleAdapter);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BooksActivity.this, OneBookActivity.class);
                int book_id = books.get(position).id;
                intent.putExtra("book_id", book_id);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                break;
                            case R.id.action_books:
                                break;
                            case R.id.action_profile:
                                break;
                        }
                        return false;
                    }
                });
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {



        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            int i = 0;
            switch (view.getId()) {

                case R.id.pbBookProgress:
                    i = ((Integer) data).intValue();
                    ((ProgressBar)view).setProgress(i);
                    if (i == 100) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    return true;
            }
            return false;
        }
    }

    public void onClickAddBook(View view) {
        Intent intent = new Intent(BooksActivity.this, AddBookActivity.class);
        startActivityForResult(intent, 1);
    }
}
