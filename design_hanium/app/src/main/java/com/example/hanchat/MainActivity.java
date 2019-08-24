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
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends TapActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText chat_text;
    String chat = "";

    HTTPConnecter connecter;

    ListView chating_list;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 앱 상단 툴바 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* 좌측 상단 토글 (네비게이션 서랍) */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        /* 우측 상단 버튼 (캘린더 화면으로 이동) */
        Button button_cal = findViewById(R.id.button_cal);
        button_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        /* 데이터 전송 (HTTPConnecter) */
        connecter = new HTTPConnecter("18.219.204.210", 55252);

//        // 이미지 전송 버튼 (HTTPConnecter)
//        Button sendImagebutton = findViewById(R.id.sendImagebutton);
//        sendImagebutton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                // 여기다 이미지 전송 관련 작성하면 됨
//            }
//        });


        /* 채팅 리스트 관리하는 어댑터 객체 생성 */
        chatAdapter =  new ChatAdapter();
        chating_list = (ListView) findViewById(R.id.chating_list);
        chating_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //스크롤을 늘 리스트뷰의 제일 마지막으로
        chating_list.setStackFromBottom(true);  //아래로 계속 생성되도록 함
        chating_list.setAdapter(chatAdapter);

        // 채팅 데이터 전송 (HTTPConnecter)
        chat_text = findViewById(R.id.chat_text);

        /*임시코드*/
        chatAdapter.add("안녕하세요 HANCHAT 임시UI입니다!", 0);    // 0은 챗봇, 1은 사용자, 2는 날짜 구분선
        chatAdapter.add("", 1);
        chatAdapter.add("2019.08.08", 2);
        chatAdapter.add("안녕하세요 HANCHAT 임시UI입니다!", 0);
        chatAdapter.add("내일 11시에 은행동에서 친구랑 만나", 1);
        chatAdapter.add("아직 기능은 구현되지 않았습니다.", 0);

        Button chat_submitbutton = findViewById(R.id.chat_submitbutton);
        chat_submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat = chat_text.getText().toString();

                try {
                    chatAdapter.add(chat, 1);    // 0은 챗봇, 1은 사용자, 2는 날짜 구분선
                    chat_text.setText(null);

                    chatAdapter.notifyDataSetChanged(); // 데이터 변화 시 갱신해 줌
                }
                catch(Exception e){
                        //예외처리
                        e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "전송실패!", Toast.LENGTH_LONG).show();
                    }

            }
        });
        //임시코드끝

        // 채팅 데이터 전송 버튼 (HTTPConnecter)
//        Button chat_submitbutton = findViewById(R.id.chat_submitbutton);
//        chat_submitbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chat = chat_text.getText().toString();
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
//                            str = ReceiveString;    //저장한 데이터를 기록으로 사용
//                            chatAdapter.add(str, 1);    // 0은 챗봇, 1은 사용자, 2는 날짜 구분선
//                            chat_text.setText(null);
//
//                            chatAdapter.notifyDataSetChanged(); // 데이터 변화 시 갱신해 줌
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
        // 채팅 데이터 전송 끝 (HTTPConnecter)


        // 챗봇 데이터 받아오는 코드 있어야함 (HTTPConnecter) 0으로 받을 것
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
