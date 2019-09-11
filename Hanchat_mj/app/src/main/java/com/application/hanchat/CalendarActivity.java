package com.application.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CalendarActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button bt_go_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 앱 상단 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 좌측 상단 토글 (네비게이션 서랍)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_chat = findViewById(R.id.bt_go_chat);

        ButtonSetting();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        bt_go_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //이미지파일을 보내려면 /apptest/test로 sendImage
}
