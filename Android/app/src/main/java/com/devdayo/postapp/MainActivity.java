package com.devdayo.postapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devdayo.postapp.demo01.*;

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
                    { "01. Request", Activity01.class },
                    { "02. Request With Params", Activity02.class },

                    { "03. GET", Activity03.class },
                    { "04. GET With Params", Activity04.class },
                    { "05. GET JSON Object", Activity05.class },
                    { "06. GET JSON Object With Params", Activity06.class },
                    { "07. GET JSON Array", Activity07.class },
                    { "08. GET JSON Array With Params", Activity08.class },
                    { "----" },
            };
}
