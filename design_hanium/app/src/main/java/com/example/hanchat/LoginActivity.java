package com.example.hanchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;

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
                    //먼저 Json 형식으로 변환
                    JSONObject json = new JSONObject();

                    //텍스트 처리는 text, 이미지 처리는 image
                    json.put("id", id);
                    json.put("pswd", pswd);
                    String jsondata = json.toString();

                    //커넥터를 이용해 sendJSON
                    connecter.sendJSON("/chatbot_data", jsondata, new HTTPConnecter.Callback() {
                        //String str;

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
