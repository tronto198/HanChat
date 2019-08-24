package com.example.hanchat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    // 채팅 기록을 보관하는 배열
    private ArrayList<ChatItem> chat_list;

    // 배열에 들어갈 채팅기록, 발화자
    class ChatItem {
        String text;
        int talker;
        ChatItem(String _text, int _talker) {
            this.text = _text;
            this.talker = _talker;
        }
    }

    //어댑터 생성자
    ChatAdapter() {
        chat_list = new ArrayList<>();
    }

    // 어댑터 대화 추가
    void add(String _text, int _talker) {
        chat_list.add(new ChatItem(_text, _talker));
    }

    // 어댑터 대화 삭제
    public void remove(int i) {
        chat_list.remove(i);
    }

    @Override
    public int getCount() {
        return chat_list.size();
    }

    @Override
    public Object getItem(int i) {
        return chat_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final int idx = i;
        final Context context = viewGroup.getContext();

        TextView textView = null;
        CustomHolder holder = null;
        LinearLayout layout = null;
        View viewRight = null;
        View viewLeft = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.data_line_view, viewGroup, false);

            layout    = (LinearLayout) convertView.findViewById(R.id.layout);
            textView    = (TextView) convertView.findViewById(R.id.textView);
            viewRight    = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft    = (View) convertView.findViewById(R.id.imageViewleft);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView   = textView;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder  = (CustomHolder) convertView.getTag();
            textView    = holder.m_TextView;
            layout  = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }

        // Text 등록
        textView.setText(chat_list.get(i).text);

        if( chat_list.get(i).talker == 0 ) {
            //textView.setBackgroundResource(R.drawable.); // 챗봇이 말하는 상황의 백그라운드 이미지
            layout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }else if(chat_list.get(i).talker == 1){
            //textView.setBackgroundResource(R.drawable.); // 사용자가 말하는 상황의 백그라운드 이미지
            layout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }else if(chat_list.get(i).talker == 2){
            //textView.setBackgroundResource(R.drawable.datebg); // 이건 저장안할거면 필요없을것같으니까 일단 나중에 삭제
            layout.setGravity(Gravity.CENTER);
            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.VISIBLE);
            /* 날짜 구분선 저장 안할거면 필요없을 것 같으니까 후에 삭제할 것
            연관 데이터로는 data_line_view 존재함
             */
        }



        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : "+chat_list.get(idx).text, Toast.LENGTH_SHORT).show();
            }
        });



        // 리스트 아이템을 길게 터치 했을때 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+chat_list.get(idx).text, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    private class CustomHolder {
        TextView    m_TextView;
        LinearLayout    layout;
        View viewRight;
        View viewLeft;
    }
}