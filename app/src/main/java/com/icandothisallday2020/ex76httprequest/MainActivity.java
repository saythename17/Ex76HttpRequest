package com.icandothisallday2020.ex76httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {
    EditText etName,etMsg;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName=findViewById(R.id.et_name);
        etMsg=findViewById(R.id.et_msg);
        tv=findViewById(R.id.tv);
    }

    public void clickGet(View view) {
        //네트워크 작업은 반드시 별도의 Thread 가 진행하도록
        new Thread(){
            @Override
            public void run() {
                //보낼 데이터
                String name=etName.getText().toString();
                String msg=etMsg.getText().toString();

                //GET 방식으로 데이터를 보낼 서버의 주소
                String serverUrl="http://soon0.dothome.co.kr/Android/getTest.php";

                //Get: 보낼 데이터를 URL 뒤에 ?-덧붙여서 보내는 방식
                //한글은 url 에 사용 불가하기에 한글을 utf-8로 인코딩한뒤 사용
                try {
                    name=URLEncoder.encode(name,"utf-8");
                    msg=URLEncoder.encode(msg,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //데이터를 포함한 최종 요청 url
                String getUrl=serverUrl+"?Name="+name+"&Message="+msg;

                //getUrl 주소의 서버와 연결하는 무지개로드를 만들어주는 해임달객체 생성
                try {
                    URL url =new URL(getUrl);
                    //URL 은 InputStream 만 열 수 있음
                    //Input, Output 모두 열 수 있는 해임달의 후계자 객체 사용
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);//구글 권장사항: 성능을 위해 캐시메모리를 쓰지 않도록

                    //보낼 데이터가 있다면 여기서 OutputStream 을 만들어 write..
                    //But. GET:이미 URL 에 보낼데이터가 전달되었기에 별도의 Write 작업 불필요
                    //GET: 서버와 연결하는 순간(new URL(getUrl))에 이미 데이터를 보낸 것
//                    OutputStream os=connection.getOutputStream();
//                    os.write(); os.flush();

                    //getTest.php 가 echo 해준 문자열을 읽어오기 위해 InputStream 필요
                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);

                    final StringBuffer buffer=new StringBuffer();
                    while(true){
                        String line=reader.readLine();
                        if(line==null) break;
                        buffer.append(line+"\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(buffer.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clickPost(View view) {
        new Thread(){
            @Override
            public void run() {
                String name=etName.getText().toString();
                String msg=etMsg.getText().toString();
                String serverUrl="http://soon0.dothome.co.kr/Android/postTest.php";

                //POST : OutputStream 을 만들어 데이터를 write 해야 함
                try {
                    URL url=new URL(serverUrl);
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    //보낼데이터
                    String query="name="+name+"&msg="+msg;
                    OutputStream os=connection.getOutputStream();
                    OutputStreamWriter writer=new OutputStreamWriter(os);
                    writer.write(query,0,query.length());
                    writer.flush();
                    writer.close();

                    //postTest.php 에서 echo 된 결과 받아오기
                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);
                    final StringBuffer buffer=new StringBuffer();
                    while (true){
                        String line=reader.readLine();
                        if(line==null) break;
                        buffer.append(line+"\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(buffer.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clickUpload(View view) {
        //server DB 에 저장
        new Thread(){
            @Override
            public void run() {
                String name=etName.getText().toString();
                String msg=etMsg.getText().toString();

                String serverUrl="http://soon0.dothome.co.kr/Android/insertDB.php";
                try {
                    URL url=new URL(serverUrl);
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    String query="name="+name+"&msg="+msg;
                    OutputStream os=connection.getOutputStream();
                    os.write(query.getBytes());//String->byte[]
                    os.flush(); os.close();

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);
                    final StringBuffer buffer=new StringBuffer();
                    while(true){
                        String line=reader.readLine();
                        if(line==null) break;
                        buffer.append(line+"\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(buffer.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clickload(View view) {
        //server DB 정보를 읽어와 보여줌
        Intent intent=new Intent(this,BoardActivity.class);
        startActivity(intent);
    }
}
