package com.example.clipboard_cloud;

import android.content.Intent;
import android.os.Bundle;

import com.example.clipboard_cloud.ListViewActivty.DataActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public static String IP_ADDRESS = "locahost";
    public static int PORT = 2333;

    EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.body);

        FloatingActionButton fab = findViewById(R.id.submit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")){
                    new ConnectionThread(editText.getText().toString()).start();
                    editText.setText("");
                    Snackbar.make(view, "发送成功", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, R.string.tips, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, DataActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    class ConnectionThread extends Thread {
        String message = null;

        public ConnectionThread(String msg){
            message = msg;
        }

        @Override
        public void run() {

            try {
                Socket socket = new Socket(IP_ADDRESS,PORT);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(message);
                out.flush();
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
