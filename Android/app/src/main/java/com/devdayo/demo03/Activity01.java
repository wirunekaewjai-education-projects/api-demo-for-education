package com.devdayo.demo03;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.devdayo.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Wirune on 4/12/2016.
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
        setContentView(R.layout.demo_03_activity_01);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onCreateClick(View view)
    {
        // อ่านเรื่อง AsyncTask ได้ที่ http://devahoy.com/posts/android-asynctask-tutorial/
        // <String, Void, String> => Parameter, Progress, Result
        // ในที่นี้ต้องการส่งสตริงเป็น Parameter ไม่มีการคำนวน Progress และ return ผลลัพธ์เป็นสตริง
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>()
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
                    ดังนั้น Activity01 ที่สืบทอด AppCompatActivity จึงสามารถเปลี่ยนตัวเองเป็น Context ได้
                    ตามหลัก Polymorphism (https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
                 */
                context = Activity01.this;

                // แสดง ProgressDialog
                dialog = ProgressDialog.show(context, "Demo 03", "Executing...");
            }

            // เป็นการทำงานใน Background Thread ซึ่งมีไว้เพื่อทำงานเล็กหรือใหญ่โดยไม่ทำให้ UI กระตุก
            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    // สั่งให้ Background Thread หยุดงานเป็นเวลา 1 วินาที (1000 มิลลิวินาที)
                    Thread.sleep(1000);

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
                String url = base_url + "/Demo-03/01-Create.php";

                /*
                    ลำดับต่อไปจะใช้ไลบรารี่ OkHttp3 ในการใช้งาน HTTP Request
                    http://www.artit-k.com/dev-okhttp-library-for-android/
                */

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

                        // ส่ง ผลลัพธ์ ไป onPostExecute โดยนำ responseBody มาแปลงเป็น string
                        return responseBody.string();
                    }
                    else
                    {
                        // ส่ง Response Code และ Message ไป onPostExecute
                        // ในกรณี้มีโอกาสเป็น 400 : Bad Request ถ้าผู้ใช้ลืมกรอก title
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

                // ปิด ProgressDialog
                dialog.dismiss();

                try
                {
                    // แปลงสตริงเป็น JSONObject
                    JSONObject object = new JSONObject(s);

                    // อ่านค่าจาก key "id"
                    int id = object.optInt("id");

                    // อัพเดทผลลัพธ์
                    String msg = "ID: " + id + " has been created.";

                    // สร้าง Snackbar เพื่อใช้เป็น Notification
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
                // เมื่อมีการแปลงสตริงเป็น JSONObject ต้องดัก catch JSONException เสมอ
                // เพราะสตริงอาจจะไม่ใช่ JSON Format ที่ถูกต้อง
                catch (JSONException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            }
        };

        // อ่านค่าที่ผู้ใช้กรอกแล้วแปลงเป็นสตริง
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่งสตริง title และ content ไปด้วย
        task.execute(title, content);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    // 400 : Bad Request
    public void onTestError400Click(View view)
    {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>()
        {
            private Context context;
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                context = Activity01.this;
                dialog = ProgressDialog.show(context, "Demo 03", "Executing...");
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                String base_url = context.getString(R.string.url);
                String url = base_url + "/Demo-03/01-Create.php";

                FormBody.Builder formBodyBuilder = new FormBody.Builder();

                // ไม่ใส่ข้อมูลลงในฟอร์ม
                // formBodyBuilder.add("title", params[0]);
                // formBodyBuilder.add("content", params[1]);

                FormBody formBody = formBodyBuilder.build();

                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);
                requestBuilder.post(formBody);

                Request request = requestBuilder.build();

                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);

                try
                {
                    Response response = call.execute();
                    return response.code() + " : " + response.message();
                }
                catch (IOException e)
                {
                    return e.getLocalizedMessage();
                }
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                dialog.dismiss();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        };

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยไม่ส่งอะไรไปเลย (ตามเงื่อนไขของ API ต้องห้ามปล่อย title ว่างไว้)
        task.execute();
    }


    // ทำการผูกเมธอดเอาไว้ใน XML
    // 405 : Method not Allowed
    public void onTestError405Click(View view)
    {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>()
        {
            private Context context;
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                context = Activity01.this;
                dialog = ProgressDialog.show(context, "Demo 03", "Executing...");
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                String base_url = context.getString(R.string.url);
                String url = base_url + "/Demo-03/01-Create.php";

                // ส่ง title และ content แบบ GET
                url = url + "?title=" + params[0] + "&content=" + params[1];

                // FormBody.Builder formBodyBuilder = new FormBody.Builder();

                // formBodyBuilder.add("title", params[0]);
                // formBodyBuilder.add("content", params[1]);

                // FormBody formBody = formBodyBuilder.build();

                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // เปลี่ยนเป็น GET
                // requestBuilder.post(formBody);
                requestBuilder.get();

                Request request = requestBuilder.build();

                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);

                try
                {
                    Response response = call.execute();
                    return response.code() + " : " + response.message();
                }
                catch (IOException e)
                {
                    return e.getLocalizedMessage();
                }
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                dialog.dismiss();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
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
