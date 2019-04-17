package com.test.uiadapter;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.uiadapter.UIAdapter;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"toast适配测试",Toast.LENGTH_SHORT).show();
        tv=findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"toast适配测试",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Resources getResources() {
        float fontScale = super.getResources().getConfiguration().fontScale;
        Log.i("===============","======2========="+fontScale);
        return UIAdapter.adaptWidth(super.getResources(),750);
    }
}
