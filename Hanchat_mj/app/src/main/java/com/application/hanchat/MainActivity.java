package com.application.hanchat;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HTTPConnecter connecter;

    ImageManagement_mj imageManagement;

    final String IP = "18.219.204.210";

    Button bt_go_cal;

    EditText et_chat;
    Button bt_chat;
    Button bt_image;

    ChatAdapter chatAdapter;
    //ListView chating_list;

    String chat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // 앱 상단 툴바
//        Toolbar toolbar = findViewById(R.id.toolbar_main);
//        setSupportActionBar(toolbar);
//
//        // 좌측 상단 토글 (네비게이션 서랍)
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_cal = findViewById(R.id.bt_go_cal);

        connecter = new HTTPConnecter(IP, 55252);

        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);

        ButtonSetting();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        bt_go_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


        bt_chat.setOnClickListener(new ButtonAction(this, connecter, et_chat) {
            @Override
            public void onClick(View v) {
                chat = et_chat.getText().toString();

                chatAdapter.add(1, chat);    // 0은 챗봇, 1은 사용자
                et_chat.setText(null);

                chatAdapter.notifyDataSetChanged(); // 데이터 변화 시 갱신해 줌

            }
        });
    }

    // + 버튼 눌렀을때 실행됨(나 다른방법 써서 버튼 세팅 안할듯)
    public void loadImagefromGallery(View view) {

        imageManagement=new ImageManagement_mj(this, connecter);
        imageManagement.tedPermission();
        imageManagement.loadImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManagement.onActivityResult(requestCode, resultCode, data);
    }

    //이미지파일을 보내려면 /apptest/test로 sendImage
}
