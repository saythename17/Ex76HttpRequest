package com.icandothisallday2020.ex76httprequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    ArrayList<BoardItem> items=new ArrayList<>();
    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //BoardItem 데이터 서버에서 불러오기
        loadDataFromServer();


        adapter=new ItemAdapter(this,items);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

    }

    void loadDataFromServer(){
        //서버의 DB를 읽어와 echo 해주는 loadDB.php 실행
        new Thread(){
            @Override
            public void run() {
                String serverUrl="http://soon0.dothome.co.kr/Android/loadDB.php";

                //Log.i("tag","aaa");
                try {
                    URL url =new URL(serverUrl);
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setUseCaches(false);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    //Log.i("tag","bbb");

                    //서버에서 echo 한 데이터 읽어오기
                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);
                    //Log.i("tag","ccc");

                    final StringBuffer buffer=new StringBuffer();
                    while (true){
                        String line=reader.readLine();
                        if(line==null) break;
                        buffer.append(line+"\n");
                        //Log.i("tag","ddd"+line);
                    }//Log.i("tag","eee"+buffer.toString());
                    //읽어들인 문자열데이터에서 각 레코드를 분리
                    String data=buffer.toString();
                    String[] rows=data.split(";");//| 를 기준으로 문자열을 분리하여리턴

                    //각 record 의 컬룸값 분리
                    for(int i=0;i<rows.length-1;i++){//rows.length : 마지막 문자 | 뒤에 빈문자열하나까지 저장됨
                        String[] cols=rows[i].split("&");//& 기준으로 문자열 분리
                        String no=cols[0];
                        String name=cols[1];
                        String message=cols[2];
                        String date=cols[3];

                        final BoardItem item=new BoardItem(no,name,message,date);
                        runOnUiThread(new Runnable() {//run() 안(별도 thread 안에서 화면 갱신)
                            @Override
                            public void run() {//notifyItemInserted: 하나하나 바꾸기
                                items.add(0,item);//Recycler 가 보여주는 대량의 데이터에 추가
                                adapter.notifyItemInserted(0);//최신글이 가장 먼저보이도록
                            }
                        });

                   }//for
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
}
