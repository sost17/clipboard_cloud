package com.example.clipboard_cloud.ListViewActivty;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clipboard_cloud.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DataActivity extends AppCompatActivity {

    public static String IP_ADDRESS = "localhost";
    public static int PORT = 3222;

    Handler handler = null;
    TextView textView = null;

    String message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_cloud);

        textView = findViewById(R.id.clipboard_body);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        new ConnectionThread().start();

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(DataActivity.this)
                        .setTitle("信息")
                        .setMessage(message)
                        .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("Label", message);

                                clipboardManager.setPrimaryClip(mClipData);
                                Toast.makeText(DataActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .show();
                return false;
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String  str = bundle.getString("data");
//                System.out.println("get Data++++++"+str);
                textView.setText(str);
                Toast.makeText(getBaseContext(),"内容获取成功",Toast.LENGTH_SHORT).show();
            }
        };
    }

    class ConnectionThread extends Thread {

        public ConnectionThread(){

        }

        @Override
        public void run() {

            try {
//                System.out.println("进来了");
                Socket socket = new Socket(IP_ADDRESS,PORT);
//                System.out.println("连接成功");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String strline = null;
                while((strline = in.readLine()) != null){
                    stringBuffer.append(strline +"\n");
                }
                message = stringBuffer.toString();
                socket.close();
//                System.out.println("信息"+ message);

                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data",message);
                msg.setData(b);
                handler.sendMessage(msg);
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
