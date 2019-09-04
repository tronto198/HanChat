package com.example.hanchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText id_text;
    EditText pswd_text;
    String id = "";
    String pswd = "";

    HTTPConnecter connecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 아이디, 비밀번호 데이터 전송 (HTTPConnecter)
        id_text = findViewById(R.id.id_text);
        pswd_text = findViewById(R.id.pswd_text);

        connecter = new HTTPConnecter("18.219.204.210", 55252);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = id_text.getText().toString();
                pswd = pswd_text.getText().toString();

                try{
                    //서버로 보낼 내용 : des

                    Map<String, String> data = new HashMap<>() ;
                    //텍스트 처리는 text, 이미지 처리는 image
                    data.put("id", id);
                    data.put("pswd", pswd);

                    //커넥터를 이용해 send
                    // chatbot_data 경로 대신 로그인 경로를 지정!!
                    connecter.Post("/chatbot_data", data, new HTTPConnecter.Callback() {
                        //여기는 데이터를 받아서 가공하는 곳
                        @Override
                        public Object DataReceived(String ReceiveString) {

                            return ReceiveString;
                        }


                        //여기는 가공한 데이터를 받아서 화면에 보여주는 곳
                        @Override
                        public void HandlerMethod(Object obj) {
                            //위의 함수에서 받은 내용을 토스트메시지로 출력
                            Toast.makeText(getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e){
                    //예외처리
                    e.printStackTrace();
                }

            }
        });
    }

}
