package com.example.chenxu.a20180413;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG="LOG";
    private Thread Tconnect;
    private Button connectBtn, sendBtn, disconnBtn;
    private EditText ipET, chatET;
    protected Socket socket;
    private Boolean conn;
    private TextView resultTV;
    protected Handler myHandler;
    private String updateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipET = findViewById(R.id.ipET);
        resultTV = findViewById(R.id.resultTV);

        myHandler = new Handler();

        conn = false;
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!conn) {
                    Tconnect = new Thread(Connection);
                    Tconnect.start();
                } else {
                    Toast.makeText(MainActivity.this, "已連線成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        disconnBtn = findViewById(R.id.disconnBtn);
        disconnBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!conn) {
                    Toast.makeText(MainActivity.this, "還沒連上，無法斷連", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        conn = false;
                        socket.close();
                    } catch (Exception e) {
                        Log.d(ACTIVITY_TAG, "This is Debug.：" + e);
                    }
                    Toast.makeText(MainActivity.this, "斷連成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatET = findViewById(R.id.chatET);

        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!conn) {
                    Log.d(ACTIVITY_TAG, "請先連線");
                    Toast.makeText(MainActivity.this, "請先連線", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        String s = chatET.getText().toString();
                        br.write(s);
                        br.flush();
                    } catch (Exception e) {
                        Log.d(ACTIVITY_TAG, "This is Debug.：" + e);
                    }
                }
            }
        });
    }

    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            try {
                String serverIP = ipET.getText().toString();
                int socketPort = 9999;
                socket = new Socket(serverIP, socketPort);

                conn = true;
                myHandler.post(updateTV);
                updateText = "成功連接！\n";
                Log.d(ACTIVITY_TAG, "成功連接!");

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!socket.isClosed() && conn) {
                    updateText = br.readLine();
                    if (updateText != null && updateText != "") {
                        updateText = "Server說" + updateText + "\n";
                        myHandler.post(updateTV);
                    }
                }
            } catch (Exception e) {
                Log.d(ACTIVITY_TAG, "This is Debug.：" + e);
            }
        }
    };

    private Runnable updateTV = new Runnable() {
        @Override
        public void run() {
            resultTV.append(updateText);
            Toast.makeText(MainActivity.this, "Updated UI", Toast.LENGTH_SHORT).show();
        }
    };
}
