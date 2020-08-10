package com.spappstudio.myapplication.mainfragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.spappstudio.myapplication.AddBookActivity;
import com.spappstudio.myapplication.DBHelper;
import com.spappstudio.myapplication.OneBookActivity;
import com.spappstudio.myapplication.R;
import com.spappstudio.myapplication.adapters.BooksRecyclerAdapter;
import com.spappstudio.myapplication.objects.Book;

import java.util.ArrayList;

public class BooksFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BooksRecyclerAdapter recyclerAdapter;

    boolean nullBooks;

    Chip chip_current;
    Chip chip_archive;
    Chip chip_wishful;

    public BooksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewBooks);
        chip_current = (Chip)rootView.findViewById(R.id.chip_current);
        chip_archive = (Chip)rootView.findViewById(R.id.chip_archive);
        chip_wishful = (Chip)rootView.findViewById(R.id.chip_wishful);

        final DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<Book> books = dbHelper.getAllUnreadBook();

        chip_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.updateData(dbHelper.getAllUnreadBook(), "all");
            }
        });

        chip_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.updateData(dbHelper.getAllReadBook(), "archive");
            }
        });

        chip_wishful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.updateData(dbHelper.getAllWishfulBooks(), "wishful");
            }
        });

        chip_current.setChecked(true);

        if (books.size() == 0) {
            books.add(new Book(-1, getString(R.string.add_book_message), "", 0, 0, 0));
            nullBooks = true;
        }

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (books.size() > 0) {
            recyclerAdapter = new BooksRecyclerAdapter(getContext(), books, "all");
        } else {
            recyclerAdapter = new BooksRecyclerAdapter(getContext(), books, "null");
        }
        recyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    /*public void updateList() {
        simpleAdapter.notifyDataSetChanged();
    }*/
}
