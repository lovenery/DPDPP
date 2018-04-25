package com.example.chenxu.a20180413_server;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG="LOG";
    private Thread serverThread;
    private Button sendBtn;
    private EditText chatET;
    private TextView ipTV, resultTV;
    protected ServerSocket serverSocket;
    protected Handler myHandler;

    private static ArrayList players = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(ACTIVITY_TAG, "IP：" + getMyWifiIp());
        ipTV = findViewById(R.id.ipTV);
        ipTV.setText(getMyWifiIp());

        resultTV = findViewById(R.id.resultTV);

        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (players.size() == 0) {
                    Log.d(ACTIVITY_TAG, "請先連線");
                    Toast.makeText(MainActivity.this, "請先連線", Toast.LENGTH_SHORT).show();
                } else {
                    Socket[] ps = new Socket[players.size()];
                    players.toArray(ps);
                    for (Socket socket : ps) {
                        try {
                            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            String s = chatET.getText().toString();
                            br.write(s + '\n');
                            br.flush();
                        } catch (Exception e) {
                            Log.d(ACTIVITY_TAG, "This is Debug 1.：" + e);
                            updateTV("Client中斷連線！\n");
                            players.remove(socket);
                        }
                    }
                }
            }
        });

        myHandler = new Handler();
        serverThread = new Thread(Server);
        serverThread.start();
    }

    private Runnable Server = new Runnable() {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(9999);
                updateTV("開啟Socket！\n");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    updateTV("Client成功連接！\n");
                    players.add(clientSocket);
                    Thread clientThread = new Thread(new ReceiveMsg(clientSocket));
                    clientThread.start();
                }
            } catch (Exception e) {
                Log.d(ACTIVITY_TAG, "This is Debug 2.：" + e);
            }
        }
    };

    class ReceiveMsg implements Runnable {
        Socket c;
        ReceiveMsg(Socket socket) { c = socket; }
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                while (!c.isClosed()) {
                    String sendStr = br.readLine();
                    if (sendStr != null && sendStr != "") {
                        updateTV("Client說：" + sendStr + "\n");
                    }
                }
            } catch (Exception e) {
                Log.d(ACTIVITY_TAG, "This is Debug 3.：" + e);
            } finally {
                updateTV("Client中斷連線！\n");
                players.remove(c);
            }
        }
    }

    private void updateTV(final String updateStr) {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                resultTV.append(updateStr);
            }
        });
    }

    private String getMyWifiIp() {
        WifiManager wifi_service = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        return ip;
    }
}
