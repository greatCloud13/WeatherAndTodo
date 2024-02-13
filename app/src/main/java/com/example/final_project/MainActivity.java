package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String weather_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text_date = (TextView)findViewById(R.id.textView2);
        TextView text_time = (TextView)findViewById(R.id.text_time) ;
        Date date = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy년 MM월 dd일 EEEE", Locale.KOREAN);
        SimpleDateFormat time_format = new SimpleDateFormat("aa hh:mm",Locale.KOREAN);
        SimpleDateFormat baseTime_format = new SimpleDateFormat("HH");

        String date_text = date_format.format(date);
        String time_text = time_format.format(date);
        String now_time= baseTime_format.format(date);
        text_date.setText(date_text);
        text_time.setText(time_text);

        String str_baseTime = baseTime(now_time);


        //날씨 api 받아올 스레드
        new Thread(new Runnable(){

            @Override
            public void run() {
                weatherXmlData();

            }
        }).start();

        //최고기온 최저기온 받아올 스레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTmn();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dustNow();
            }
        }).start();


        ImageButton re= (ImageButton)findViewById(R.id.imageButton3);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat time_format = new SimpleDateFormat("aa hh:mm",Locale.KOREAN);
                String time_text = time_format.format(date);
                text_time.setText(time_text);

                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        weatherXmlData();
                    }
                }).start();
            }
        });





        ListView list;
        String[] name={};
        String[] detail={};

        Button to_todo = (Button) findViewById(R.id.to_todo_list);
        to_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, todolist_act.class);
                startActivity(intent);
            }
        });

        Button to_wather = (Button) findViewById(R.id.today);
        to_wather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, weather_act.class);
                startActivity(intent2);
            }
        });


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
    void weatherXmlData(){
        TextView wind_speed =(TextView)findViewById(R.id.wind_speed);
        TextView now_temp = (TextView)findViewById(R.id.now_temp);
        TextView rain_pop = (TextView)findViewById(R.id.rain_pop);
        TextView rain_type_txt = (TextView) findViewById(R.id.rain_type);
        ImageView img_weather = (ImageView)findViewById(R.id.img_weather);
        ImageView rain_type = (ImageView)findViewById(R.id.img_rain_type);
        SimpleDateFormat real_time = new SimpleDateFormat("yyyyMMdd"); /*yyyyMMdd형식으로 변환*/
        SimpleDateFormat fBase_time = new SimpleDateFormat("HH");
        Date time = new Date();
        String base_date=real_time.format(time);
        String base_time = baseTime(fBase_time.format(time));
        int nx=89; //대구 격자좌표
        int ny=98;

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

                        if(tag.equals("item"));
                        else if(tag.equals("fcstValue")){ //fcstValue 태그가 나올때마다 i카운트
                            i++;                          //원하는 정보가 나오면
                            if(i==1){
                                xpp.next();
                                now_temp.setText(xpp.getText()+"℃");
                            }
                            if(i==5){
                                xpp.next();
                                wind_speed.setText(xpp.getText()+"m/s");
                            }

                            if(i==6){
                                xpp.next();
                                if(xpp.getText().equals("1")){
                                    img_weather.setImageResource(R.drawable.sunny);
                                    rain_type.setImageResource(R.drawable.sunny);
                                }
                                else if(xpp.getText().equals("2")){
                                    img_weather.setImageResource(R.drawable.some);
                                    rain_type.setImageResource(R.drawable.some);
                                }
                                else if(xpp.getText().equals("4")){
                                    img_weather.setImageResource(R.drawable.cloud);
                                    rain_type.setImageResource(R.drawable.cloud);
                                }
                            }
                            if(i==7){
                                xpp.next();
                                if(xpp.getText().equals("0")){
                                    rain_type_txt.setText("없음");
                                }
                                else if(xpp.getText().equals("1")){
                                    rain_type.setImageResource(R.drawable.rainy);
                                    rain_type_txt.setText("비");
                                }
                                else if(xpp.getText().equals("2")){
                                    rain_type.setImageResource(R.drawable.snow);
                                    rain_type_txt.setText("눈");
                                }
                                else if(xpp.getText().equals("3")){
                                    rain_type.setImageResource(R.drawable.shower);
                                    rain_type_txt.setText("소나기");
                                }
                            }
                            if(i==8){
                                xpp.next();
                                rain_pop.setText(xpp.getText()+"%");
                            }


                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        break;
                }
                eventType=xpp.next();
            }
        }catch(Exception e){
        }
    }

    //최저기온 최고기온
    void getTmn(){
        TextView tmn = (TextView)findViewById(R.id.TMN);
        TextView tmx = (TextView)findViewById(R.id.TMX);
        String baseDate=yesterday();
        String queryUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=mVVCb8e1M0Xg6i4YuWcAAph6n%2BBTYEVVYk4QGdrZ%2FwnRs9BP1io%2BKoAtS0lQBpvDcSa5UZ2jy9kAvcRZGf3ijQ%3D%3D&pageNo=1&numOfRows=500&dataType=XML&base_date="+baseDate+"&base_time=0500&nx=89&ny=98";
        try{ // 익일 basetime 0500 TMN 302, Tmx411
            URL url = new URL(queryUrl); //문자열로된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp =factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            int i =0;
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag=xpp.getName();

                        if(tag.equals("item"));
                        else if(tag.equals("fcstValue")){
                            i++;
                            xpp.next();
                            if(i==302){
                                tmn.setText(xpp.getText()+"℃\n최저");
                            }
                            else if(i==411){
                                tmx.setText(xpp.getText()+"℃\n최고");
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        break;
                }
                eventType=xpp.next();
            }
        }catch(Exception e){
        }
    }

    String yesterday(){
        SimpleDateFormat real_time = new SimpleDateFormat("yyyyMMdd"); /*yyyyMMdd형식으로 변환*/
        Date time = new Date();
        String baseDate = real_time.format(time);
        int int_baseDate= Integer.parseInt(baseDate);
        int_baseDate=int_baseDate-1;
        String yesterdayString=Integer.toString(int_baseDate);
        return yesterdayString;
    }

    void dustNow(){
        TextView dustText = (TextView)findViewById(R.id.pm10Value);

        String queryUrl = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=mVVCb8e1M0Xg6i4YuWcAAph6n%2BBTYEVVYk4QGdrZ%2FwnRs9BP1io%2BKoAtS0lQBpvDcSa5UZ2jy9kAvcRZGf3ijQ%3D%3D&returnType=xml&numOfRows=133&pageNo=1&sidoName=%EB%8C%80%EA%B5%AC&ver=1.0";
        try{
            URL url = new URL(queryUrl); //문자열로된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp =factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));//inputStream으로 xml 입력받기

            String tag;
            int i =0;
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag=xpp.getName(); //태그이름 가져오기
                        if(tag.equals("item"));
                        else if(tag.equals("pm10Grade")){
                            i++;
                            xpp.next();
                            if(i==12){ //진천동 측정소
                                dustText.setText(pm10GradeValue(xpp.getText()));
                            }

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        break;
                }
                eventType=xpp.next();
            }
        }catch(Exception e){
        }
    }

    String pm10GradeValue(String input){
        String value;
        int count=Integer.parseInt(input);
        if (count==1){
            return "좋음";
        }
        else if(count==2){
            return "보통";
        }
        else if(count==3){
            return "나쁨";
        }
        else return "매우나쁨";
    }

}
