package com.example.final_project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.Date;

public class weather_act extends AppCompatActivity {
    TextView text;
    String weather_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherde);
        text = (TextView)findViewById(R.id.textView4);

        Button b = (Button) findViewById(R.id.btn_retrun);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Thread(new Runnable(){

            @Override
            public void run() {
                weather_data=weatherXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(weather_data);
                    }
                });
            }
        }).start();

    }

    String baseTime(String time){
        int int_time=Integer.parseInt(time);
        String baseTime;
        if(2<int_time&&int_time<5){
            baseTime="0200";
        }
        else if(5<=int_time&&int_time<8){
            baseTime="0500";
        }
        else if(8<=int_time&&int_time<11){
            baseTime="0800";
        }
        else if(11<=int_time&&int_time<14){
            baseTime="1100";
        }
        else if(14<=int_time&&int_time<1700){
            baseTime="1400";
        }
        else if(1700<=int_time&&int_time<2000){
            baseTime="1700";
        }
        else if(2000<=int_time&&int_time<2300){
            baseTime="2000";
        }
        else
            baseTime="2300";

        return baseTime;
    }

    String weatherXmlData(){
        StringBuffer buffer=new StringBuffer();
        SimpleDateFormat real_time = new SimpleDateFormat("yyyyMMdd"); /*yyyyMMdd형식으로 변환*/
        int nx=89; //대구 격자좌표
        int ny=98;
        SimpleDateFormat fBase_time = new SimpleDateFormat("H");
        Date time = new Date();
        String base_date=real_time.format(time);
        String base_time = baseTime(fBase_time.format(time));

        String queryUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=mVVCb8e1M0Xg6i4YuWcAAph6n%2BBTYEVVYk4QGdrZ%2FwnRs9BP1io%2BKoAtS0lQBpvDcSa5UZ2jy9kAvcRZGf3ijQ%3D%3D&pageNo=1&numOfRows=12&dataType=XML&base_date="+base_date+"&base_time="+base_time+"&nx="+nx+"&ny="+ny;
        try{
            URL url = new URL(queryUrl); //문자열로된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp =factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));//inputStream으로 xml 입력받기

            String tag;
            int i =0;
            xpp.next();
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag=xpp.getName(); //태그이름 가져오기

                        if(tag.equals("item")); //첫번째 검색결과
                        else if(tag.equals("fcstValue")){
                            i++;
                            if(i==1){
                                buffer.append("기온:");
                                xpp.next();
                                buffer.append(xpp.getText()+"℃");
                                buffer.append("\n");
                            }
                            if(i==2){
                                buffer.append("\n");
                                buffer.append("풍속(동서):");
                                xpp.next();
                                buffer.append(xpp.getText()+"m/s");
                                buffer.append("\n");
                            }
                            if(i==3){
                                buffer.append("\n");
                                buffer.append("풍속(남북):");
                                xpp.next();
                                buffer.append(xpp.getText()+"m/s");
                                buffer.append("\n");
                            }
                            if(i==4){
                                buffer.append("\n");
                                buffer.append("풍향:");
                                xpp.next();
                                buffer.append(xpp.getText()+"deg");
                                buffer.append("\n");
                            }
                            if(i==5){
                                buffer.append("\n");
                                buffer.append("풍속:");
                                xpp.next();
                                buffer.append(xpp.getText()+"m/s");
                                buffer.append("\n");
                            }
                            if(i==6){
                                buffer.append("\n");
                                buffer.append("하늘상태:");
                                xpp.next();
                                if(xpp.getText().equals("1")){
                                    buffer.append("맑음");
                                    buffer.append("\n");
                                }
                                else if(xpp.getText().equals("2")){
                                    buffer.append("구름많음");
                                    buffer.append("\n");
                                }
                                else if(xpp.getText().equals("4")){
                                    buffer.append("흐림");
                                    buffer.append("\n");
                                }
                            }
                            if(i==7){
                                buffer.append("\n");
                                buffer.append("강수형태:");
                                xpp.next();
                                if(xpp.getText().equals("0")){
                                    buffer.append("없음");
                                    buffer.append("\n");
                                }
                                else if(xpp.getText().equals("1")){
                                    buffer.append("비");
                                    buffer.append("\n");
                                }
                                else if(xpp.getText().equals("2")){
                                    buffer.append("눈");
                                    buffer.append("\n");
                                }
                                else if(xpp.getText().equals("3")){
                                    buffer.append("소나기");
                                    buffer.append("\n");
                                }
                            }
                            if(i==8){
                                buffer.append("\n");
                                buffer.append("강수확률:");
                                xpp.next();
                                buffer.append(xpp.getText()+"%");
                                buffer.append("\n");
                            }
                            if(i==11){
                                buffer.append("\n");
                                buffer.append("습도:");
                                xpp.next();
                                buffer.append(xpp.getText()+"%");
                                buffer.append("\n");
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item"));
                        break;
                }
                eventType=xpp.next();
            }
        }catch(Exception e){
        }
        return buffer.toString();
    }

}
