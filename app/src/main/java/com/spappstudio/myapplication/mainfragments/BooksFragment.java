package com.spappstudio.myapplication.mainfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;

import com.spappstudio.myapplication.AddBookActivity;
import com.spappstudio.myapplication.OneBookActivity;
import com.spappstudio.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooksFragment extends Fragment {

    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_PROGRESS = "progress";

    ListView listViewBooks;
    ArrayList<String> booksTitle;
    int booksProgress[];
    int booksId[];
    boolean nullBooks;

    public BooksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        listViewBooks = (ListView)rootView.findViewById(R.id.listViewBooks);

        Bundle bundle = getArguments();
        booksTitle = bundle.getStringArrayList("booksTitle");
        booksProgress = bundle.getIntArray("booksProgress");
        booksId = bundle.getIntArray("booksId");

        if (booksTitle.size() == 0) {
            booksTitle.add("Нажмите, чтобы добавить книгу");
            booksProgress = new int[] {0};
            nullBooks = true;
        }

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(booksTitle.size());
        Map<String, Object> map;
        for (int i = 0; i < booksTitle.size(); i++) {
            map = new HashMap<String, Object>();
            map.put(ATTRIBUTE_NAME_TEXT, booksTitle.get(i));
            map.put(ATTRIBUTE_NAME_PROGRESS, booksProgress[i]);
            data.add(map);
        }

        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_PROGRESS};
        int[] to = {R.id.tvBookName, R.id.pbBookProgress};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_book, from, to);
        simpleAdapter.setViewBinder(new MyViewBinder());

        listViewBooks.setAdapter(simpleAdapter);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (nullBooks) {
                    intent = new Intent(getActivity(), AddBookActivity.class);
                } else {
                    intent = new Intent(getActivity(), OneBookActivity.class);
                    int book_id = booksId[position];
                    intent.putExtra("book_id", book_id);
                }
                startActivity(intent);
            }
        });

        return rootView;
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
                    if (nullBooks) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    /*if (i == 100) {
                        Log.d("LOG progress: ", String.valueOf(i));
                        view.setVisibility(View.INVISIBLE);
                    }*/
                    return true;
            }
            return false;
        }
    }
}
