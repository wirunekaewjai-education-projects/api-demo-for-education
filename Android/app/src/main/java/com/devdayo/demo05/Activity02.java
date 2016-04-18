package com.devdayo.demo05;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devdayo.app.R;
import com.devdayo.demo03.Item;
import com.devdayo.demo03.ItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class Activity02 extends AppCompatActivity
{
    // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.demo_04_activity_02);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);

        // ดักจับ Event การคลิก Item ของ ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // สร้าง Intent และกำหนดปลายทางไปยัง Activity02ById
                Intent intent = new Intent(getApplicationContext(), Activity02ById.class);

                // แนบ id ไปกับ Intent ด้วย
                intent.putExtra("id", id);

                // เปิด Activity จากข้อมูลใน Intent
                startActivity(intent);
            }
        });

    }

    // onStart จะทำหลังจาก onCreate และทำอีกครั้งหลังจากมีการปิด Activity02ById
    @Override
    protected void onStart()
    {
        super.onStart();

        // สั่งให้เริ่มต้นรีเควส
        request();
    }

    public void request()
    {
        Context context = this;

        // เปลี่ยนจาก AsyncTask มาเป็น HttpTask  แบบเดียวกับ demo04 ที่สร้างเอง (จริงๆ แล้ว HttpTask สืบทอดมาจาก AsyncTask)
        com.devdayo.demo04.HttpTask<Void> task = new com.devdayo.demo04.HttpTask<Void>(context, 500)
        {
            @Override
            protected Response onExecute(Void... params)
            {
                // อ่านสตริง url จาก XML (res/values/string.xml)
                String baseUrl = getContext().getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = baseUrl + "/Demo-05/02-Read.php";

                // สร้าง Request Builder และกำหนด URL ปลายทาง
                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // กำหนดให้ HTTP Request Method เป็น GET
                requestBuilder.get();

                // ใช้ Request Builder สร้าง Request อีกที
                Request request = requestBuilder.build();

                // เรียกเมธอด doRequest พร้อมส่งค่ากลับ (สร้างไว้ใน HttpTask)
                return doRequest(request);
            }

            @Override
            protected void onSuccessful(Response response)
            {
                try
                {
                    // แปลง response body เป็น สตริง
                    String json = response.body().string();

                    // แปลงสตริงเป็น JSONArray
                    JSONArray array = new JSONArray(json);

                    // แปลง JSONArray เป็น List<Item>
                    List<Item> items = Item.parse(array);

                    // สร้าง Adapter เพื่อเป็นตัวเชื่อมระหว่าง Spinner และ List<Item>
                    // ItemAdapter มาจาก Demo03
                    ItemAdapter adapter = new ItemAdapter(items);

                    // กำหนด Adapter ให้กับ Spinner
                    listView.setAdapter(adapter);
                }
                // เมื่อมีการแปลง response body เป็น สตริง ต้องดัก catch IOException เสมอ
                catch (IOException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }
                // เมื่อมีการแปลงสตริงเป็น JSONArray ต้องดัก catch JSONException เสมอ
                // เพราะสตริงอาจจะไม่ใช่ JSON Format ที่ถูกต้อง
                catch (JSONException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }

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

        // สั่งให้ AsyncTask เริ่มต้นทำงาน
        task.execute();
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onCreateClick(View view)
    {
        // สร้าง Intent เพื่อเปิด Activity สร้างโพส
        Intent intent = new Intent(getApplicationContext(), Activity01.class);

        // เปิด Activity ด้วยข้อมูล Intent ที่สร้างไว้
        startActivity(intent);
    }
}
