package kr.co.hoon.a180523chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nickname = (EditText)findViewById(R.id.nickname);
        EditText msg = (EditText)findViewById(R.id.msg);
        TextView display = (TextView)findViewById(R.id.display);
        Button send = (Button)findViewById(R.id.send);

        ChatClient chatClient = new ChatClient(nickname, msg, display, send);
        Thread th = new Thread(chatClient);
        th.start();
    }
}
