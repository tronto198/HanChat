package com.example.hanchat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    public class ChatContents {
        String text;
        int talker;
        //생성자
        ChatContents (String _text,int _talker) {
            this.text = _text;
            this.talker = _talker;
        }
    }

    // 채팅 기록을 보관하는 배열
    private ArrayList chatList;

    //생성자
    public ChatAdapter() {
        chatList = new ArrayList();
    }

    // 대화 추가
   public void add(String _text,int _talker) {

        chatList.add(new ChatContents(_text,_talker));
    }

    // 대화 삭제
    public void remove(int i) {
        chatList.remove(i);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //여기에서 뷰 관리
        return null;
    }

}