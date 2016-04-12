package com.devdayo.demo03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.devdayo.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Wirune on 4/12/2016.
 */
public class Activity02ById extends AppCompatActivity
{
    @Bind(R.id.title_view)
    TextView titleView;

    @Bind(R.id.excerpt_view)
    TextView contentView;

    @Bind(R.id.created_date_view)
    TextView createdDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_03_activity_02_item);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        long id = bundle.getLong("id");

        request(id);
    }

    private void request(long id)
    {
        // อ่านเรื่อง AsyncTask ได้ที่ http://devahoy.com/posts/android-asynctask-tutorial/
        // <Long, Void, String> => Parameter, Progress, Result
        // ในที่นี้ต้องการส่งตัวเลขเป็น Parameter ไม่มีการคำนวน Progress และ return ผลลัพธ์เป็นสตริง
        AsyncTask<Long, Void, String> task = new AsyncTask<Long, Void, String>()
        {
            private Context context;

            // อ่านเรื่อง ProgressDialog ได้ที่
            // https://examples.javacodegeeks.com/android/core/ui/progressdialog/android-progressdialog-example/

            // ประกาศตัวแปรไว้ใช้เก็บ ProgressDialog
            private ProgressDialog dialog;

            // ทำงานเป็นลำดับแรกหลังจากสั่ง execute() และทำงานใน UI Thread
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                /*
                    เก็บ Instance ของ Activity นี้ลงในตัวแปร Context
                    ซึ่ง AppCompatActivity สืบทอดจาก Activity และ Activity สืบทอดจาก Context
                    ดังนั้น Activity02WithId ที่สืบทอด AppCompatActivity จึงสามารถเปลี่ยนตัวเองเป็น Context ได้
                    ตามหลัก Polymorphism (https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
                 */
                context = Activity02ById.this;

                // แสดง ProgressDialog
                dialog = ProgressDialog.show(context, "Demo 03", "Executing...");
            }

            // เป็นการทำงานใน Background Thread ซึ่งมีไว้เพื่อทำงานเล็กหรือใหญ่โดยไม่ทำให้ UI กระตุก
            @Override
            protected String doInBackground(Long... params)
            {
                try
                {
                    // สั่งให้ Background Thread หยุดงานเป็นเวลา 0.2 วินาที (200 มิลลิวินาที)
                    Thread.sleep(200);

                    /*
                        จริงๆ ไม่ต้องสั่ง Thread.sleep ก็ได้นะครับ
                        ในที่นี้ต้องการให้ตอนรันโปรแกรม ผู้ใช้สามารถเห็น ProgressDialog สักพักนึงก่อน
                        เพราะบางครั้ง HTTP Request เสร็จไวมาก อาจจะทำให้ไม่เห็น ProgressDialog กันเลยทีเดียว
                    */
                }
                // เมื่อใช้ Thread.sleep จะต้องดัก catch(InterruptedException) เสมอ
                catch (InterruptedException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }

                // อ่านสตริง url จาก XML (res/values/string.xml)
                String base_url = context.getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = base_url + "/Demo-03/03-ReadById.php";

                // https://en.wikipedia.org/wiki/Query_string
                // เพิ่ม queryString (params คือ สตริงที่ส่งมาตอน execute ซึ่ง length ของ params จะเท่ากับจำนวนสตริงที่ส่งมา)
                url = url + "?id=" + params[0];

                /*
                    ลำดับต่อไปจะใช้ไลบรารี่ OkHttp3 ในการใช้งาน HTTP Request
                    http://www.artit-k.com/dev-okhttp-library-for-android/
                */

                // สร้าง Request Builder และกำหนด URL ปลายทาง
                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // กำหนดให้ HTTP Request Method เป็น GET
                requestBuilder.get();

                // ใช้ Request Builder สร้าง Request อีกที
                Request request = requestBuilder.build();

                // สร้างออบเจกต์สำหรับ HTTP Request เรียกว่า HttpClient
                OkHttpClient client = new OkHttpClient();

                // สร้างออกเจกต์สำหรับเรียกใช้งาน Request
                Call call = client.newCall(request);

                try
                {
                    // ทำการ Request
                    Response response = call.execute();

                    // 2xx ใน HTTP Response Code คือ สำเร็จ
                    // อ่านเรื่อง HTTP Response Code ได้ที่ https://goo.gl/MXVo6k
                    if(response.isSuccessful())
                    {
                        // ทำการอ่านผลลัพธ์ในรูปแบบออบเจกต์
                        ResponseBody responseBody = response.body();

                        // ส่ง ผลลัพธ์ ไป onPoseExecute โดยนำ responseBody มาแปลงเป็น string
                        return responseBody.string();
                    }
                    else
                    {
                        // ส่ง Response Code และ Message ไป onPostExecute
                        return response.code() + " : " + response.message();
                    }
                }
                // ในการใช้ call.execute() จะต้องดัก catch(IOException) เสมอ
                catch (IOException e)
                {
                    // ส่ง error ไปแสดงผลใน resultView
                    return e.getLocalizedMessage();
                }
            }

            // ทำเป็นลำดับสุดท้าย และจะเริ่มทำหลังจากมาการ return ค่าใน doInBackground
            // โดย onPostExecute จะทำใน UI Thread
            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                try
                {
                    // แปลงสตริงเป็น JSONObject
                    JSONObject object = new JSONObject(s);

                    String title = object.optString("title");
                    String content = object.optString("content");
                    String createdDate = object.optString("created_date");

                    titleView.setText(title);
                    contentView.setText(content);
                    createdDateView.setText(createdDate);
                }
                // เมื่อมีการแปลงสตริงเป็น JSONArray ต้องดัก catch JSONException เสมอ
                // เพราะสตริงอาจจะไม่ใช่ JSON Format ที่ถูกต้อง
                catch (JSONException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }

                // ปิด ProgressDialog
                dialog.dismiss();
            }
        };

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่ง id ไปด้วย
        task.execute(id);
    }
}
