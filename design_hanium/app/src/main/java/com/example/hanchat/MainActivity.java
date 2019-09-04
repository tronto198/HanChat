package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends TapActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText chat_text;
    String chat = "";

    HTTPConnecter connecter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // fab 필요 하면 appbar에서 주석 풀기
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //우측 상단 버튼 (캘린더 화면으로 이동)
        Button button_cal = findViewById(R.id.button_cal);
        button_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // 네비게이션 서랍
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//
//        // 아이디, 비밀번호 데이터 전송 (HTTPConnecter)
//        chat_text = findViewById(R.id.chat_text);
//
//        connecter = new HTTPConnecter("18.219.204.210", 55252);
//
//        Button chat_submitbutton = findViewById(R.id.chat_submitbutton);
//        chat_submitbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chat = chat_text.getText().toString();
//
//                try{
//                    //서버로 보낼 내용 : des
//
//                    Map<String, String> data = new HashMap<>() ;
//                    data.put("id", chat);
//
//                    //커넥터를 이용해 send
//                    connecter.Post("/chatbot_data", data, new HTTPConnecter.Callback() {
//                        String str;
//
//                        //여기는 데이터를 받아서 가공하는 곳
//                        @Override
//                        public Object DataReceived(String ReceiveString) {
//                            str = ReceiveString;    //저장한 데이터를 기록으로 사용하자!!
//
//                            return ReceiveString;
//
//                            //일단 여기서는 텍스트를 받기만 하므로 그대로 리턴
//                        }
//
//
//                        //여기는 가공한 데이터를 받아서 화면에 보여주는 곳
//                        @Override
//                        public void HandlerMethod(Object obj) {
//                            //위의 함수에서 받은 내용을 토스트메시지로 출력
//                            Toast.makeText(getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//                catch (Exception e){
//                    //예외처리
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }
}
