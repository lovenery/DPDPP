package com.example.chenxu.a20180323;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText nameET;

    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB;

    private Calendar birthCal = Calendar.getInstance();
    private TextView birthTV;
    private Button birthBtn;

    private Spinner bloodSpinner;

    private EditText heightET, weightET;

    private CheckBox singCB, danceCB, movieCB;

    private String nameStr, genderStr, bloodStr, birthStr, heightStr, weightStr;
    private CheckBox sendCB;
    private Button sendBtn, testBtn, clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameET = findViewById(R.id.nameET);

        genderRG = findViewById(R.id.genderRG);
//        genderRG.setOnCheckedChangeListener(genderCheckedChangeListener);
        maleRB = findViewById(R.id.maleRB);
        femaleRB = findViewById(R.id.femaleRB);

        birthBtn = findViewById(R.id.birthBtn);
        birthBtn.setOnClickListener(birthBtnOnClickListener);
        birthTV = findViewById(R.id.birthTV);

        bloodSpinner = findViewById(R.id.bloodSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.blood, android.R.layout.simple_spinner_item);
        bloodSpinner.setAdapter(adapter);
//        bloodSpinner.setOnItemSelectedListener(bloodOnItemSelectedListener);

        heightET = findViewById(R.id.heightET);
        weightET = findViewById(R.id.weightET);

        singCB = findViewById(R.id.singCB);
        singCB.setOnCheckedChangeListener(interestOnCheckedChangeListener);
        danceCB = findViewById(R.id.danceCB);
        danceCB.setOnCheckedChangeListener(interestOnCheckedChangeListener);
        movieCB = findViewById(R.id.movieCB);
        movieCB.setOnCheckedChangeListener(interestOnCheckedChangeListener);

        sendCB = findViewById(R.id.sendCB);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = nameET.getText().toString();
                if (genderRG.getCheckedRadioButtonId() != -1) {
                    genderStr = ((RadioButton) findViewById(genderRG.getCheckedRadioButtonId())).getText().toString();
                }
                birthStr = birthTV.getText().toString();
                bloodStr = bloodSpinner.getSelectedItem().toString();
                heightStr = heightET.getText().toString();
                weightStr = weightET.getText().toString();

                if (!sendCB.isChecked()) {
                    Toast.makeText(MainActivity.this, "請勾選確認資料", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(genderStr) || TextUtils.isEmpty(birthStr) || TextUtils.isEmpty(bloodStr) || TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
                    Toast.makeText(MainActivity.this, "請確認資料是否漏填", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("name", nameStr);
                    bundle.putString("gender", genderStr);
                    bundle.putString("birth", birthStr);
                    bundle.putString("blood", bloodStr);
                    bundle.putString("height", heightStr);
                    bundle.putString("weight", weightStr);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        testBtn = findViewById(R.id.testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameET.setText("TEST_NAME");
                femaleRB.setChecked(true);
                birthTV.setText("2018/03/23");
                bloodSpinner.setSelection(1);
                heightET.setText("160");
                weightET.setText("47");
                sendCB.setChecked(true);
            }
        });

        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameET.setText("");
                genderRG.clearCheck();
                birthTV.setText("");
                bloodSpinner.setSelection(0);
                heightET.setText("");
                weightET.setText("");
                sendCB.setChecked(false);
            }
        });
    }

//    private RadioGroup.OnCheckedChangeListener genderCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup radioGroup, int i) {
//            switch (i) {
//                case R.id.maleRB:
//                    genderStr = String.valueOf(maleRB.getText());
//                case R.id.femaleRB:
//                    genderStr = String.valueOf(femaleRB.getText());
//                default:
//                    Toast.makeText(MainActivity.this, "你是" + genderStr, Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

    DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int w) {
            birthTV.setText(y + "/" + (m + 1) + "/" + w);
        }
    };
    private View.OnClickListener birthBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int year = birthCal.get(Calendar.YEAR);
            int month = birthCal.get(Calendar.MONTH);
            int day = birthCal.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(MainActivity.this, dateSet, year, month, day).show();
        }
    };

//    private AdapterView.OnItemSelectedListener bloodOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            bloodStr = adapterView.getSelectedItem().toString();
//            Toast.makeText(MainActivity.this, "你是" + adapterView.getSelectedItem().toString() + "型", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//    };

    private CompoundButton.OnCheckedChangeListener interestOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                Toast.makeText(MainActivity.this, "喜歡" + compoundButton.getText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "不喜歡" + compoundButton.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
