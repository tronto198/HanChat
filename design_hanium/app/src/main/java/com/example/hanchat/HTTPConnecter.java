package com.example.hanchat;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//8.7

//먼저 맨위의 패키지 이름부터 바꾸고
//AndroidManifest.xml 파일에서 <uses-permission android:name="android.permission.INTERNET" /> 추가


public class HTTPConnecter {

    //통신 이후의 콜백 함수 정의를 위한 인터페이스
    public interface Callback{
        //서버에서 받은 데이터를 처리할 콜백 함수 (쓰레드로 실행됨)
        //UIChange로 전달할 데이터를 return
        public Object DataReceived(String ReceiveString);

        //데이터를 모두 처리하고 나서 UI 등을 처리할 콜백 함수 (메인 쓰레드에서 실행됨)
        //UI 등과 같이 메인 쓰레드가 아니면 실행할 수 없는 부분을 처리
        //위의 함수에서 Object를 받아서 처리하는 함수를 만드세요
        public void HandlerMethod(Object obj);


        //함수 실행 순서 : DataReceived  ->  UIChange
    }

    private String Hostname = "";
    private Handler handler = null;

    int Port;

    //생성자로 ip주소, 포트번호를 전달받음
    public HTTPConnecter(String hostip, int Port){
        Hostname = hostip;
        this.Port = Port;
        this.handler = new Handler();
    }

    //JSON형식의 파일을 전달할때
    //주소 뒤에 붙는 Pathname (ex : "/chatbot_data"), Json형식의 String과 통신 이후에 수행할 콜백을 받음
    //콜백은 위의 인터페이스 활용
    public void sendJSON(String Pathname, String Jsonstr, Callback callback){
        String _url = "http://" + Hostname + ":" + Port + Pathname;
        SendThread th_Sender = new SendThread();
        th_Sender.SetConnection(_url, Jsonstr);
        th_Sender.SetCallback(callback, handler);
        th_Sender.start();

    }


    class SendThread extends Thread{
        Callback cb = null;
        Handler handler = null;

        String _url = "";
        String outputstr = "";

        //연결할 url과 전달할 메세지 세팅
        public void SetConnection(String url, String msg){
            _url = url;
            outputstr = msg;
        }

        //데이터를 받아서 처리할 콜백과 UI를 처리할 콜백 세팅
        public void SetCallback(Callback callback, Handler handler){
            cb = callback;
            this.handler = handler;
        }

        public void run(){
            String data = "";

            try{
                URL url = new URL(_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");

                    byte[] outputBytes = outputstr.getBytes("UTF-8");
                    OutputStream os = conn.getOutputStream();
                    os.write(outputBytes);
                    os.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;

                    while(true){
                        line = reader.readLine();
                        if(line == null) break;

                        data += line;
                    }

                    reader.close();
                    conn.disconnect();

                    //데이터 주고받기 완료
                    //데이터를 주고받을때마다 연결을 햇다 끊엇다 해야하나?
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            final Object obj = cb.DataReceived(data);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    cb.HandlerMethod(obj);
                }
            });

        }
    }
}
