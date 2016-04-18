package com.devdayo.demo05;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.devdayo.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wirune on 4/13/2016.
 */
public class Activity01 extends AppCompatActivity
{
    // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.title_view)
    EditText titleView;

    @Bind(R.id.content_view)
    EditText contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.demo_04_activity_01);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onCreateClick(View view)
    {
        Context context = this;

        // เปลี่ยนจาก AsyncTask มาเป็น HttpTask แบบเดียวกับ demo04 ที่สร้างเอง (จริงๆ แล้ว HttpTask สืบทอดมาจาก AsyncTask)
        com.devdayo.demo04.HttpTask<String> task = new com.devdayo.demo04.HttpTask<String>(context, 500)
        {
            @Override
            protected Response onExecute(String... params)
            {
                // อ่านสตริง url จาก XML (res/values/string.xml)
                String baseUrl = getContext().getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = baseUrl + "/Demo-05/01-Create.php";

                // สร้าง Request Body แบบ Form ด้วย FormBody.Builder
                FormBody.Builder formBodyBuilder = new FormBody.Builder();

                // ใส่ข้อมูลลงในฟอร์ม
                formBodyBuilder.add("title", params[0]);
                formBodyBuilder.add("content", params[1]);

                FormBody formBody = formBodyBuilder.build();

                // สร้าง Request Builder และกำหนด URL ปลายทาง
                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // กำหนดให้ HTTP Request Method เป็น POST และส่งข้อมูลจากฟอร์มไปด้วย
                requestBuilder.post(formBody);

                // ใช้ Request Builder สร้าง Request อีกที
                Request request = requestBuilder.build();

                // เรียกเมธอด doRequest พร้อมส่งค่ากลับ (สร้างไว้ใน HttpTask)
                return doRequest(request);
            }

            @Override
            protected void onSuccessful(Response response)
            {
                // ถ้าสร้างสำเร็จให้ทำการปิด Activity ทันที
                Activity01.this.finish();
            }

            @Override
            protected void onFailure(Response response)
            {
                // ถ้าสร้างไม่สำเร็จให้ทำการแสดงผล Response Code และ Message ผ่าน Snackbar
                String msg = response.code() + " : "+response.message();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        };

        // อ่านค่าที่ผู้ใช้กรอกแล้วแปลงเป็นสตริง
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่งสตริง title และ content ไปด้วย
        task.execute(title, content);
    }
}
