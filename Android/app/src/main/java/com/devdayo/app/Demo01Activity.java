package com.devdayo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.devdayo.demo01.*;

public class Demo01Activity extends AppCompatActivity
{
    // ประกาศตัวแปรเพื่อไว้ใช้เพิ่มปุ่มเมนู
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.activity_main);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        linearLayout = (LinearLayout) findViewById(R.id.demo_layout);

        // วนลูปตามจำนวนเมนูจากตัวแปร links
        for(int i = 0; i < links.length; i++)
        {
            // อ่านค่าแต่ละ index จากตัวแปร links และเก็บไว้ที่ตัวแปรอาร์เรย์ link
            Object[] link = links[i];

            // แปลงข้อมูลใน link[0] Object เป็น String
            String text = link[0].toString();

            // แปลงข้อมูลใน link[1] Object เป็น Class<?>
            // และกำหนดให้เป็นตัวแปรแบบ final เพื่อให้ใช้กับ OnClickListener ที่เป็น Anonymous Instance
            // https://en.wikipedia.org/wiki/Final_(Java)
            final Class<?> cls = (Class<?>) link[1];
            /*
                Note : เกี่ยวกับเรื่อง Class<?>
                Class<?> เป็นออบเจกต์ที่ใช้เก็บข้อมูลของคลาสที่ต้องการ
                ไม่ว่าจะเป็นตัวแปรหรือเมธอดต่างๆ ที่ประกาศไว้ในคลาสนั้น

                การที่จะได้ออบเจกต์ประเภทนี้มาใช้มี 2 วิธีด้วยกัน
                1. ชื่อคลาสตามด้วย .class เช่น
                    Class<?> cls1 = MainActivity.class;
                    Class<?> cls2 = Demo01Activity.class;

                2. ใช้ this.getClass(); เช่น
                    Class<?> cls = this.getClass();

                อ่านเพิ่มได้ที่ https://docs.oracle.com/javase/tutorial/reflect/
            */

            // สร้างปุ่มขึ้นมาเองโดยมาผ่าน XML
            Button button = new Button(getApplicationContext());

            // กำหนดให้ข้อความอยู่ทางซ้าย
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setText(text);

            // ตรวจจับการกดปุ่ม
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // สร้าง Intent สำหรับการเปิด Activity ของ Demo ต่างๆ
                    Intent intent = new Intent(getApplicationContext(), cls);

                    // เปิด Activity จากข้อมูลใน Intent
                    startActivity(intent);
                }
            });

            // ทำการเพิ่มปุ่มลงใน Linear Layout
            linearLayout.addView(button);
        }
    }

    // สร้างตัวแปรไว้ใช้เป็นข้อมูลเมนู
    private static final Object[][] links =
            {
                    { "01. Request", Activity01.class },
                    { "02. Request With Params", Activity02.class },

                    { "03. GET", Activity03.class },
                    { "04. GET With Params", Activity04.class },
                    { "05. GET JSON Object", Activity05.class },
                    { "06. GET JSON Object With Params", Activity06.class },
                    { "07. GET JSON Array", Activity07.class },
                    { "08. GET JSON Array With Params", Activity08.class },

                    { "09. POST", Activity09.class },
                    { "10. POST With Params", Activity10.class },
                    { "11. POST JSON Object", Activity11.class },
                    { "12. POST JSON Object With Params", Activity12.class },
                    { "13. POST JSON Array", Activity13.class },
                    { "14. POST JSON Array With Params", Activity14.class }
            };
}
