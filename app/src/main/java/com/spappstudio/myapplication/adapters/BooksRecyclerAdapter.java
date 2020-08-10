package com.spappstudio.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.spappstudio.myapplication.AddBookActivity;
import com.spappstudio.myapplication.OneBookActivity;
import com.spappstudio.myapplication.R;
import com.spappstudio.myapplication.objects.Book;

import java.util.ArrayList;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Book> books;
    private String type;

    public BooksRecyclerAdapter(Context context, ArrayList<Book> books, String type) {
        this.inflater = LayoutInflater.from(context);
        this.books = books;
        this.type = type;
    }

    @Override
    public BooksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksRecyclerAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.textView.setText(book.author + " - " + book.name);
        if (type == "all") {
            holder.progressBar.setProgress(book.getPercent());
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateData(ArrayList<Book> books, String type) {
        this.books = books;
        this.type = type;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textView;
        final ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textView = (TextView)view.findViewById(R.id.tvBookName);
            progressBar = (ProgressBar)view.findViewById(R.id.pbBookProgress);
        }

        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            Intent intent;
            if (type == "null") {
                intent = new Intent(view.getContext(), AddBookActivity.class);
            } else {
                intent = new Intent(view.getContext(), OneBookActivity.class);
                int book_id = books.get(itemPosition).id;
                intent.putExtra("book_id", book_id);
            }
            view.getContext().startActivity(intent);
        }
    }
}
