package com.example.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    public String readDay = null;
    public String str = null;
    MaterialCalendarView calendarView;
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
        //textView3 = findViewById(R.id.textView3); //맨 위 달력이라 표시
        contextEditText = findViewById(R.id.contextEditText); //선택 날짜 일정 수정하는 칸
        RadioGroup ctype;
        RadioButton radIndividual;
        RadioButton radGroup;

        // 날짜가 변경될 때 이벤트를 받기 위한 리스너
        calendarView.setOnDateChangedListener(new OnDateSelectedListener()
        {
            // 선택된 날짜를 알려주는 메서드
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
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
                diaryTextView.setText(String.format("%d / %d / %d",  date.getYear(), date.getMonth() + 1, date.getDay()));
                contextEditText.setText("");
                //빨간 점 찍기
                calendarView.setSelectedDate(CalendarDay.today());
                calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));
                //이제 날짜 체크 후 일정 삽입 or 수정 작업
                checkDay(date.getYear(), date.getMonth() + 1, date.getDay());
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

//    private static class DayDecorator implements DayViewDecorator {
//
//        private final Drawable drawable;
//
//        public DayDecorator(Context context) {
//            drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector);
//        }
//
//        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
//        @Override
//        public boolean shouldDecorate(CalendarDay day) {
//            return true;
//        }
//
//
//
//        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
//        @Override
//        public void decorate(DayViewFacade view) {
//            view.setSelectionDrawable(drawable);
////            view.addSpan(new StyleSpan(Typeface.BOLD));   // 달력 안의 모든 숫자들이 볼드 처리됨
//        }
//    }

}

