package com.devdayo.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    protected LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.demo_layout);

        for(int i = 0; i < links.length; i++)
        {
            Object[] link = links[i];

            String text = link[0].toString();
            if(link.length == 1)
            {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.BLACK);
                tv.setText(text);

                linearLayout.addView(tv);
            }
            else
            {
                final Class<?> cls = (Class<?>) link[1];

                Button button = new Button(getApplicationContext());
                button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                button.setText(text);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getApplicationContext(), cls);
                        startActivity(intent);
                    }
                });

                linearLayout.addView(button);
            }
        }
    }

    private static final Object[][] links =
            {
                    { "Demo 01" },
                    { "01. Request", com.devdayo.demo01.Activity01.class },
                    { "02. Request With Params", com.devdayo.demo01.Activity02.class },

                    { "03. GET", com.devdayo.demo01.Activity03.class },
                    { "04. GET With Params", com.devdayo.demo01.Activity04.class },
                    { "05. GET JSON Object", com.devdayo.demo01.Activity05.class },
                    { "06. GET JSON Object With Params", com.devdayo.demo01.Activity06.class },
                    { "07. GET JSON Array", com.devdayo.demo01.Activity07.class },
                    { "08. GET JSON Array With Params", com.devdayo.demo01.Activity08.class },

                    { "09. POST", com.devdayo.demo01.Activity09.class },
                    { "10. POST With Params", com.devdayo.demo01.Activity10.class },
                    { "11. POST JSON Object", com.devdayo.demo01.Activity11.class },
                    { "12. POST JSON Object With Params", com.devdayo.demo01.Activity12.class },
                    { "13. POST JSON Array", com.devdayo.demo01.Activity13.class },
                    { "14. POST JSON Array With Params", com.devdayo.demo01.Activity14.class },
                    { "----" },

                    { "Demo 02" },
                    { "01. GET JSON Object Calculator", com.devdayo.demo02.Activity01.class },
                    { "02. GET JSON Array Sorter", com.devdayo.demo02.Activity02.class },

                    { "03. POST JSON Object Calculator", com.devdayo.demo02.Activity03.class },
                    { "04. POST JSON Array Sorter", com.devdayo.demo02.Activity04.class },
                    { "----" },
            };
}
