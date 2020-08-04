package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    int pageCount;
    TextView textViewPageCount;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewPageCount = (TextView)findViewById(R.id.textViewPageCount);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

    }

    @Override
    public void onResume() {
        super.onResume();
        pageCount = dbHelper.getPagesToday();
        textViewPageCount.setText(String.valueOf(pageCount));
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
        intent.putExtra("type", "add");
        startActivity(intent);
    }

    public void onClickEdit(View view) {
        Intent intent = new Intent(EditActivity.this, EnterPagesActivity.class);
        intent.putExtra("type", "edit");
        startActivity(intent);
    }

    public void onClickEditMoreDay(View view) {
        Intent intent = new Intent(EditActivity.this, EnterDatePagesActivity.class);
        startActivity(intent);
    }

}
