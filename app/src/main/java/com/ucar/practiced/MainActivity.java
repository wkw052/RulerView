package com.ucar.practiced;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author kaiwen.wu
 * @date 2019/11/23
 */
public class MainActivity extends AppCompatActivity{

    private int measureNum = 0;

    private TextView tvRulerNumber;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRulerNumber = findViewById(R.id.tv_ruler_number);
        tvRulerNumber.setText(String.format(getResources().getString(R.string.ruler_number), measureNum));
        mSharedPreferences = getSharedPreferences("num", Context.MODE_PRIVATE);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            measureNum = mSharedPreferences.getInt("measure", 0);
            Log.e("position", "Activity measureNum is " + measureNum);
            tvRulerNumber.setText(String.format(getResources().getString(R.string.ruler_number), measureNum));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
