package com.application.hanchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    HTTPConnecter connecter;
    final String IP = "18.219.204.210";

    EditText et_chat;
    Button bt_chat;
    Button bt_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connecter = new HTTPConnecter(IP, 55252);

        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);

        ButtonSetting();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        bt_chat.setOnClickListener(new ButtonAction(this, connecter, et_chat));
    }

    //이미지파일을 보내려면 /apptest/test로 sendImage
}
