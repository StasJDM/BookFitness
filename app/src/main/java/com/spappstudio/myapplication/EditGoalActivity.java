package com.spappstudio.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

public class EditGoalActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "BookFitnessData";
    private static final String APP_PREFERENCES_GOAL = "goal";

    int goal;

    NumberPicker numberPicker;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
        setTitle(getString(R.string.change_goal));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        goal = sharedPreferences.getInt(APP_PREFERENCES_GOAL, 20);

        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setValue(goal);
        numberPicker.setWrapSelectorWheel(false);
    }

    public void onClickSaveGoal(View view) {
        goal = numberPicker.getValue();
        sharedPreferences.edit().putInt(APP_PREFERENCES_GOAL, goal).apply();
        setResult(RESULT_OK);
        finish();
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
}
