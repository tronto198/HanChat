package com.application.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
    ListView chating_list;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, CalendarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        connecter = HTTPConnecter.getinstance(IP, 55252);
        bt_go_cal = findViewById(R.id.bt_go_cal);
        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);


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


        ChatAdapterSetting();
        ButtonSetting();
    }

    private void ChatAdapterSetting(){

        // 채팅 리스트 관리하는 어댑터 객체 생성
        chatAdapter =  new ChatAdapter();
        chating_list = (ListView) findViewById(R.id.chating_list);
        chating_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //스크롤을 늘 리스트뷰의 제일 마지막으로
        chating_list.setStackFromBottom(true);  //아래로 계속 생성되도록 함
        chating_list.setAdapter(chatAdapter);

        // 임시 코드
        chatAdapter.add(0, "안녕하세요 HANCHAT 임시UI입니다!");
        chatAdapter.add(1,"내일 11시에 은행동에서 친구랑 만나");
        chatAdapter.add(0, "아직 기능은 구현되지 않았습니다.(여기까지 MainActivity 65번째줄 임시 코드)");
        chatAdapter.notifyDataSetChanged();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });

        // 채팅 전송
        bt_chat.setOnClickListener(new ButtonAction(this, connecter, et_chat, chatAdapter));
        imageManagement=new ImageManagement_mj(this, connecter, chatAdapter);
    }

    // + 버튼 눌렀을때 실행됨(나 다른방법 써서 버튼 세팅 안할듯)
    public void loadImagefromGallery(View view) {

        imageManagement.tedPermission();
        imageManagement.loadImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManagement.onActivityResult(requestCode, resultCode, data);
    }
}
