package com.example.firsttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText tb_des;
    String des = "";
    TextView view;

    HTTPConnecter connecter;
    String ip = "18.219.204.210";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_des = findViewById(R.id.textedit);
        view = findViewById(R.id.view_Result);
        connecter = new HTTPConnecter(ip, 55252);

        Button bt_start = (Button) findViewById(R.id.bt_start);
        /*
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des = tb_des.getText().toString();
                view.setText("");

                try{
                    //서버로 보낼 내용 : des
                    //먼저 Json 형식으로 변환
                    Map<String, String> data = new HashMap<>();

                    //텍스트 처리는 text, 이미지 처리는 image
                    data.put("text", des);


                    //커넥터를 이용해 Post
                    //Pathname - 텍스트 처리는 "/apptest/chatbot", 이미지 처리는 "/apptest/image"
                    //Jsondata - json형식을 String으로 바꾼 데이터
                    //new 어쩌고 - 데이터 통신 이후에 실행될 콜백 함수들을 정의
                    connecter.Post("/apptest/chatbot", data, new HTTPConnecter.Callback() {
                        //String str;                   //

                        //여기는 데이터를 받아서 가공하는 곳
                        @Override
                        public Object DataReceived(String ReceiveString) {

                            //str = ReceiveString;      // 이런 식으로 저장해서 밑으로 넘길수도 있고
                            return ReceiveString;       // 이런 식으로 return으로 넘길 수도 있음

                            //일단 여기서는 텍스트를 받기만 하므로 그대로 리턴
                        }


                        //여기는 가공한 데이터를 받아서 화면에 보여주는 곳
                        @Override
                        public void HandlerMethod(Object obj) {
                            //위의 함수에서 받은 내용을 토스트메시지로 출력
                            Toast.makeText(getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();
                            view.append((String) obj);
                        }
                    });

                }
                catch (Exception e){
                    //예외처리
                    e.printStackTrace();
                }

            }
        });
        */

        bt_start.setOnClickListener(new buttonaction(this, connecter, tb_des));
    }

}
