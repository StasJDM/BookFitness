package com.spappstudio.myapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.myapplication.DBHelper;
import com.spappstudio.myapplication.OneBookActivity;
import com.spappstudio.myapplication.R;

public class RateBookDialog extends DialogFragment implements RatingBar.OnRatingBarChangeListener {

    Button buttonSubmit;
    RatingBar ratingBar;
    DBHelper dbHelper;
    int rate;
    int book_id;

    public RateBookDialog() {
        rate = 1;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        book_id = getArguments().getInt("book_id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate_book, null);
        builder.setView(view);
        ratingBar = view.findViewById(R.id.ratingBar);
        buttonSubmit = view.findViewById(R.id.textViewSubmit);

        dbHelper = new DBHelper(getContext());

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OneBookActivity.class);
                intent.putExtra("book_id", book_id);
                startActivity(intent);
                getActivity().finish();
                dbHelper.finishBook(book_id, rate);
            }
        });
        ratingBar.setOnRatingBarChangeListener(this);
        return builder.create();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        rate = (int) ratingBar.getRating();
    }
}
