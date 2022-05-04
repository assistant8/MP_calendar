package com.example.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
{
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //각 xml 위젯 가져옴
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView); //중간에 몇월 몇일 보여주는
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2); //일정 추가된 날짜 클릭 시 어떤 일정있나 보여주는 칸
        textView3 = findViewById(R.id.textView3); //맨 위 달력이라 표시
        contextEditText = findViewById(R.id.contextEditText); //선택 날짜 일정 수정하는 칸

        //
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                //중간 날짜 표시, 저장 버튼, 일정 수정 - 보이도록
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                //어떤 일정 있나, 수정, 삭제 버튼 - 안보이도록 - 아직 어떤 작업도 수행 안됐으니
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                //중간 날짜 어케 보여줄지 + 일정 추가되는 칸 초기화
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                //
                checkDay(year, month, dayOfMonth);
            }
        });
        //저장 버튼 클릭 시
        save_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveDiary(readDay); //아마 일정 저장
                str = contextEditText.getText().toString(); //일정을 쓰면 그 내용을 str로 저장
                textView2.setText(str); //t2(일정 보여주는)에 str 저장
                //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 수정, 삭제 버튼, 일정 보여주는 거 보이게함
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
        });
    }

    public void checkDay(int cYear, int cMonth, int cDay)
    {
        //받아온 연월일을 readday라는 스트링에 저장
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        //파일 내용 읽으려나 봄
        FileInputStream fis;

        try
        {
            //readday 스트링을 읽겠다
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            //readday를 무튼 fileData에 넣었음
            fis.read(fileData);
            fis.close();

            //fileData의 연월일 정보를 str에 넣음
            str = new String(fileData);

            //일정 수정 또는 삽입 후 일정 보여주는 그 시점
            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str); //일정 보여주는 칸에 연월일 정보인 str 넣음
            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            //수정 버튼 눌리면
            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //원래 있던 일정(str)이 edittext에 그대로 나오고
                    //마지막에 edit의 스트링을 text2에 넣어줌
                    //마찬가지로 수정 눌리면 edit과 저장버튼만 보이게 해주는거..
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            //삭제 버튼 눌리면
            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //text2 안보이고 edit이 비게 되고,
                    //edit과 저장버튼만 보이게됨
                    //readDay의 스트링은 삭제됨
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });

            //text2가 비어있다면 그냥 바로 edit과 저장버튼만 보이도록
            if (textView2.getText() == null)
            {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}