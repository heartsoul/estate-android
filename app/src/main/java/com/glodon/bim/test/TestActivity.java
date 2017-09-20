package com.glodon.bim.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.glodon.bim.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_testImage,btn_testRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_testImage = (Button) findViewById(R.id.btn_testimage);
        btn_testRecyclerView = (Button) findViewById(R.id.btn_recyclerView);

        setListener();
    }

    private void setListener() {
        btn_testImage.setOnClickListener(this);
        btn_testRecyclerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.btn_testimage:
                intent.setClass(this,TestImageActivity.class);
                break;
            case R.id.btn_recyclerView:
                intent.setClass(this,TestRecyclerViewActivity.class);
                break;
        }
        startActivity(intent);
    }
}
