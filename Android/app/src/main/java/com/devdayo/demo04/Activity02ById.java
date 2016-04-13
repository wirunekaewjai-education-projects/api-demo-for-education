package com.devdayo.demo04;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.devdayo.app.R;
import com.devdayo.demo03.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wirune on 4/13/2016.
 */
public class Activity02ById extends AppCompatActivity
{
    // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.title_view)
    EditText titleView;

    @Bind(R.id.content_view)
    EditText contentView;

    @Bind(R.id.created_date_view)
    TextView createdDateView;

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.demo_04_activity_02_by_id);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);

        // http://www.akexorcist.com/2013/02/android-code-intent-with-bundle-extra.html
        // ดึง Intent จาก Activity
        Intent intent = getIntent();

        // http://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
        // ดึง Intent จาก Bundle
        Bundle bundle = intent.getExtras();

        // ดึง id ที่ส่งมาจาก Activity02
        id = bundle.getLong("id");

        // รีเควสข้อมูลจาก id
        request();
    }

    private void request()
    {
        Context context = this;

        // เปลี่ยนจาก AsyncTask มาเป็น HttpTask ที่สร้างเอง (จริงๆ แล้ว HttpTask สืบทอดมาจาก AsyncTask)
        HttpTask<Long> task = new HttpTask<Long>(context, 0)
        {
            @Override
            protected Response onExecute(Long... params)
            {
                // อ่านสตริง url จาก XML (res/values/string.xml)
                String base_url = getContext().getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = base_url + "/Demo-04/03-ReadById.php";

                // https://en.wikipedia.org/wiki/Query_string
                // เพิ่ม queryString (params คือ สตริงที่ส่งมาตอน execute ซึ่ง length ของ params จะเท่ากับจำนวนสตริงที่ส่งมา)
                url = url + "?id=" + params[0];

                // สร้าง Request Builder และกำหนด URL ปลายทาง
                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // กำหนดให้ HTTP Request Method เป็น GET
                requestBuilder.get();

                // ใช้ Request Builder สร้าง Request อีกที
                Request request = requestBuilder.build();

                return doRequest(request);
            }

            @Override
            protected void onSuccessful(Response response)
            {
                try
                {
                    // แปลง response body เป็น สตริง
                    String json = response.body().string();

                    // แปลงสตริงเป็น JSONObject
                    JSONObject object = new JSONObject(json);

                    // แปลง JSONObject เป็น Item
                    Item item = Item.parse(object);

                    // อัพเดท View จากข้อมูลใน Item
                    titleView.setText(item.getTitle());
                    contentView.setText(item.getContent());
                    createdDateView.setText(item.getCreatedDate());
                }
                // เมื่อมีการแปลง response body เป็น สตริง ต้องดัก catch IOException เสมอ
                catch (IOException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }
                // เมื่อมีการแปลงสตริงเป็น JSONObject ต้องดัก catch JSONException เสมอ
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
                // ถ้ารีเควสโพสไม่สำเร็จให้ทำการแสดงผล Response Code และ Message ผ่าน Snackbar
                String msg = response.code() + " : " + response.message();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        };

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่ง id ไปด้วย
        task.execute(id);
    }

    public void onUpdateClick(View view)
    {
        Context context = this;

        // เปลี่ยนจาก AsyncTask มาเป็น HttpTask ที่สร้างเอง (จริงๆ แล้ว HttpTask สืบทอดมาจาก AsyncTask)
        HttpTask<String> task = new HttpTask<String>(context, 500)
        {
            @Override
            protected Response onExecute(String... params)
            {
                // อ่านสตริง url จาก XML (res/values/string.xml)
                String base_url = getContext().getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = base_url + "/Demo-04/04-Update.php";

                // สร้าง Request Body แบบ Form ด้วย FormBody.Builder
                FormBody.Builder formBodyBuilder = new FormBody.Builder();

                // ใส่ข้อมูลลงในฟอร์ม
                formBodyBuilder.add("id", params[0]);
                formBodyBuilder.add("title", params[1]);
                formBodyBuilder.add("content", params[2]);

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
                // ถ้าอัพเดทสำเร็จให้ทำการแสดงผล Response Code และ Message ผ่าน Snackbar
                String msg = response.code() + " : " + response.message();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }

            @Override
            protected void onFailure(Response response)
            {
                // ถ้าอัพเดทไม่สำเร็จให้ทำการแสดงผล Response Code และ Message ผ่าน Snackbar
                String msg = response.code() + " : " + response.message();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        };

        // อ่านค่าไอดีและที่ผู้ใช้กรอกแล้วแปลงเป็นสตริง
        String idString = id + "";
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่งสตริง id, title และ content ไปด้วย
        task.execute(idString, title, content);
    }

    public void onDeleteClick(View view)
    {
        Context context = this;

        // เปลี่ยนจาก AsyncTask มาเป็น HttpTask ที่สร้างเอง (จริงๆ แล้ว HttpTask สืบทอดมาจาก AsyncTask)
        HttpTask<Long> task = new HttpTask<Long>(context, 500)
        {
            @Override
            protected Response onExecute(Long... params)
            {
                // อ่านสตริง url จาก XML (res/values/string.xml)
                String base_url = getContext().getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = base_url + "/Demo-04/05-Delete.php";

                // สร้าง Request Body แบบ Form ด้วย FormBody.Builder
                FormBody.Builder formBodyBuilder = new FormBody.Builder();

                // ใส่ข้อมูลลงในฟอร์ม
                formBodyBuilder.add("id", params[0].toString());

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
                Activity02ById.this.finish();
            }

            @Override
            protected void onFailure(Response response)
            {
                // ถ้าลบไม่สำเร็จให้ทำการแสดงผล Response Code และ Message ผ่าน Snackbar
                String msg = response.code() + " : " + response.message();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        };

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่งสตริง id ไปด้วย
        task.execute(id);
    }
}
