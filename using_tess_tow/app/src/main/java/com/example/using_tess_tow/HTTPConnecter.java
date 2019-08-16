package com.example.using_tess_tow;


import android.os.Handler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class HTTPConnecter {

    //통신 완료 후에 실행될 콜백 함수 정의를 위한 인터페이스
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

    final public static String POST = "POST";
    final public static String GET = "GET";


    private int timeout = 10000;


    private String Hostname = "";
    private Handler handler = null;

    private int Port;

    //생성자로 ip주소, 포트번호를 전달받음
    public HTTPConnecter(String Hostip, int Port){
        Hostname = Hostip;
        this.Port = Port;
        this.handler = new Handler();
    }
    public HTTPConnecter(String DNS){
        Hostname = DNS;
    }

    //JSON형식의 파일을 전달할때
    //주소 뒤에 붙는 Pathname (ex : "/chatbot_data"), Json형식의 String과 통신 이후에 수행할 콜백을 받음
    //콜백은 위의 인터페이스 활용
    public void sendJSON(String Pathname, Map Jsondata, Callback callback){
        sendJSON(Pathname, Jsondata, "POST", callback);
    }
    public void sendJSON(String Pathname, Map Jsondata, String RequestType, Callback callback){
        String _url = "http://" + Hostname + ":" + Port + Pathname;
        SendingThread th_Sender = new sendJson();
        th_Sender.SetConnection(_url, RequestType);
        th_Sender.SetMessage(Jsondata);
        th_Sender.SetCallback(callback);
        th_Sender.start();

    }

    public void sendImage(String Pathname, Map Jsondata, String filepath, Callback callback){
        String _url = "http://" + Hostname + ":" + Port + Pathname;
        sendimage th_Sender = new sendimage();
        th_Sender.SetConnection(_url, "POST");
        th_Sender.SetMessage(Jsondata, filepath);
        th_Sender.SetCallback(callback);
        th_Sender.start();
    }


    public void setTimeout(int ms){
        timeout = ms;
    }


    private abstract class SendingThread extends Thread {
        private String _url = "";
        protected String reqtype = "POST";
        protected String contenttype;
        protected Map<String, String> map = null;
        private Callback cb = null;


        public SendingThread(String contenttype){
            this.contenttype = contenttype;
        }

        //연결할 url과 요청 타입 설정
        public void SetConnection(String url, String RequestType){
            _url = url;
            reqtype = RequestType;
        }

        //데이터를 받아서 처리할 콜백과 UI를 처리할 콜백 세팅
        public void SetCallback(Callback callback){
            cb = callback;
        }
        public void SetMessage(Map msg){
            this.map = msg;
        }


        protected HttpURLConnection openConnection() throws Exception {

            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(timeout);
                conn.setRequestMethod(reqtype);
                conn.setRequestProperty("Content-Type", contenttype);
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

            }
            else{
                throw new Exception("connection failed");
            }

            return conn;
        }

        abstract protected void sendMessage(HttpURLConnection conn) throws Exception;

        public void run(){
            String Receivedata = "";

            try{
                //연결 세팅하고 만들기
                HttpURLConnection conn = openConnection();

                //메시지 보내기
                sendMessage(conn);


                //메시지 받기
                final int ResponseCode = conn.getResponseCode();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;

                while (true) {
                    line = reader.readLine();
                    if (line == null) break;

                    Receivedata += line;
                }


                reader.close();
                conn.disconnect();

                //데이터 주고받기 완료
                //데이터를 주고받을때마다 연결을 햇다 끊엇다 해야하나?

            }
            catch (Exception e){
                e.printStackTrace();
            }

            final Object obj = cb.DataReceived(Receivedata);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    cb.HandlerMethod(obj);
                }
            });

        }
    }

    private class sendJson extends SendingThread {
        //final public static String JSON = "application/json; charset=UTF-8";

        public sendJson(){
            super("application/json; charset=UTF-8");
        }

        protected void sendMessage(HttpURLConnection conn) throws Exception{
            if(map == null) throw new Exception("data is null");

            JSONObject jsonObject = new JSONObject();
            for( Map.Entry<String, String> entry : map.entrySet() ) {
                String key = entry.getKey();
                String value = entry.getValue();
                jsonObject.put(key, value);
            }

            String jsonstr = jsonObject.toString();

            byte[] outputBytes = jsonstr.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();

        }

    }


    private class sendimage extends SendingThread {
        final private String lineEnd = "\r\n";
        final private String twoHyphens = "--";
        final private String boundary;

        private String filepath = null;

        public sendimage() {
            super("multipart/form-data; charset=utf-8; boundary=");
            String abc = "*___*";  //여기서 랜덤 돌리기
            boundary = abc;
            contenttype += boundary;

        }


        public void SetMessage(Map map, String filepath) {
            super.SetMessage(map);
            this.filepath = filepath;
        }

        @Override
        protected void sendMessage(HttpURLConnection conn) throws Exception {

            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            String msgbegin = lineEnd + twoHyphens + boundary + lineEnd;

            //이 3개가 한묶음 - 키 : 문자열 한가지를 보낼때
            //ds.writeBytes(msgbegin);
            //ds.writeBytes("Content-Disposition: form-data; name=\"{name}\"");
            //ds.writeBytes(lineEnd + lineEnd + data);

            //일반 데이터 전송
            if (map != null){
                for( Map.Entry<String, String> entry : map.entrySet() ) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    //이 3개가 한묶음 - { 키 : 문자열 }한가지를 보낼때
                    ds.writeBytes(msgbegin);
                    ds.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
                    ds.writeBytes(lineEnd + lineEnd + URLEncoder.encode(value, "utf-8"));
                }
            }


            //이미지 전송
            if(filepath != null) {

                byte[] buffer;
                int maxBufferSize = 5 * 1024 * 1024;

                ds.writeBytes(msgbegin);
                ds.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"imagefile\"");

                //이 문장은 써야하는지 모르겟
                //ds.writeBytes(lineEnd + "Content-Type: application/octet-stream");

                ds.writeBytes(lineEnd + lineEnd);

                FileInputStream fs = new FileInputStream(filepath);
                buffer = new byte[maxBufferSize];
                int length = -1;
                while((length= fs.read(buffer)) != -1){
                    ds.write(buffer, 0, length);
                }
                fs.close();
            }

            //여기가 데이터 전송 끝이다 선언언
            ds.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

            ds.flush();
            ds.close();

        }
    }

}
