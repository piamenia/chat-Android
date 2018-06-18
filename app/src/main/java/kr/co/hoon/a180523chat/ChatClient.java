package kr.co.hoon.a180523chat;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements  Runnable {

    BufferedReader br;
    PrintWriter pw;

    EditText nickname, msg;
    TextView display;
    Button send;
    // 처음 한번만 작업할 내용이 있어서 변수를 만듬
    int start = 0;

    // 생성자
    public ChatClient (EditText nickname, EditText msg, TextView display, Button send){
        super();
        this.nickname = nickname;
        this.msg = msg;
        this.display = display;
        this.send = send;
        // 버튼에 이벤트핸들러 연결
        this.send.setOnClickListener(eventHandler);
    }

    // 버튼 이벤트 핸들러
    // 보내기 버튼을 누르면 msg의 내용을 서버에 전송
    Button.OnClickListener eventHandler = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                if (start == 0) {
                    // 처음 한번만 수행할 내용
                    start = 1;
                    pw.println(nickname.getText().toString());
                    pw.flush();
                }
                // 계속 수행할 내용
                pw.println(msg.getText().toString());
                pw.flush();
                msg.setText("");
            }
            catch(Exception e){}
        }

    };

    // ChatHandler와 메시지를 주고받는 일을 하는 스레드를 생성하고 실행

    // 스레드로 수행할 내용
    @Override
    public void run() {
        try{
            Socket s = new Socket("192.168.0.218", 9999);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream());
            // 메시지를 받으면 수행될 내용을 호출
            // 작업별로 분류하기 위해서 메소드로 만들어서 호출
            execute();
        }catch (Exception e){

        }
    }

    // 메시지를 받으면 수행될 메소드
    public void execute(){
        try{
            while(true){
                String line = br.readLine();
                // 메시지를 출력하기 위해 핸들러에 메시지 전달
                Message message = new Message();
                message.obj = line;
                // 안드로이드는 메인스레드에서만 UI를 변경할 수 있기 때문에 Handler 이용
                handler.sendMessage(message);
            }
        }catch(Exception e){

        }finally{
            stop();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            display.append(message.obj.toString() + "\n");
        }
    };

    public void stop(){
        try{
            if(br!=null) br.close();
            if(pw!=null) pw.close();
        }catch(Exception e){

        }
    }
}
