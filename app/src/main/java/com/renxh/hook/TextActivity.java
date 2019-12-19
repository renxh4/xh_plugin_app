package com.renxh.hook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TextActivity extends AppCompatActivity {

    public static final String TARGET_COMPONENT = "TARGET_COMPONENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
    }
}
