package com.example.chenxu.a20180323_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTV;
    private Button backBtn;
    private static final String TAG = "デバッグ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTV = findViewById(R.id.resultTV);
        backBtn = findViewById(R.id.backBtn);

        Bundle bundle = getIntent().getExtras();
        double height = Double.parseDouble(bundle.getString("height")) / 100;
        double weight = Double.parseDouble(bundle.getString("weight"));
        double BMI = weight / (height * height);
        String resultStr = "姓名：" + bundle.getString("name") + "\n" +
                    "性別：" + bundle.getString("gender") + "\n" +
                    "生日：" + bundle.getString("birth") + "\n" +
                    "血型：" + bundle.getString("blood") + "\n" +
                    "BMI：" + BMI + "\n";
        resultTV.setText(resultStr);
        Log.d(ResultActivity.TAG, resultStr);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 讀取Menu的XML
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Menu被按下後的事件
        Toast.makeText(ResultActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        finish();
        return super.onOptionsItemSelected(item);
    }
}