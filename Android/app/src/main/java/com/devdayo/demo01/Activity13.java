package com.devdayo.demo01;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devdayo.app.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Activity13 extends AppCompatActivity
{
    // ประกาศตัวแปรเพื่อไว้ใช้กำหนดข้อความผลลัพธ์
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...) โดยใช้แบบเดียวกับ Activity01
        setContentView(R.layout.demo_01_activity_01);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        resultView = (TextView) findViewById(R.id.result_view);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onExecuteClick(View view)
    {
        // อ่านเรื่อง AsyncTask ได้ที่ http://devahoy.com/posts/android-asynctask-tutorial/
        // <Void, Void, String> => Parameter, Progress, Result
        // ในที่นี้ไม่มี Parameter ไม่มีการคำนวน Progress และ return ผลลัพธ์เป็นสตริง
        // (ดูความแตกต่างได้ใน demo01/Activity02)
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
        {
            private Context context;

            // อ่านเรื่อง ProgressDialog
            // ได้ที่ https://examples.javacodegeeks.com/android/core/ui/progressdialog/android-progressdialog-example/

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
                    ดังนั้น Activity13 ที่สืบทอด AppCompatActivity จึงสามารถเปลี่ยนตัวเองเป็น Context ได้
                    ตามหลัก Polymorphism (https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
                 */
                context = Activity13.this;

                // แสดง ProgressDialog
                dialog = ProgressDialog.show(context, "Demo 01", "Executing...");
            }

            // เป็นการทำงานใน Background Thread ซึ่งมีไว้เพื่อทำงานเล็กหรือใหญ่โดยไม่ทำให้ UI กระตุก
            // Void... params อาจจะงงตรง ... ไปอ่านได้ที่ http://docs.oracle.com/javase/tutorial/java/javaOO/arguments.html#varargs
            @Override
            protected String doInBackground(Void... params)
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
                String baseUrl = context.getString(R.string.url);

                // ทำการกำหนด url ที่ต้องการเรียก HTTP Request
                String url = baseUrl + "/Demo-01/13-PostJSONArray.php";

                /*
                    ลำดับต่อไปจะใช้ไลบรารี่ OkHttp3 ในการใช้งาน HTTP Request
                    http://www.artit-k.com/dev-okhttp-library-for-android/
                */

                // สร้าง Request Body แบบ Form ด้วย FormBody.Builder
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                FormBody formBody = formBodyBuilder.build();

                // สร้าง Request Builder และกำหนด URL ปลายทาง
                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                // กำหนดให้ HTTP Request Method เป็น POST และไม่มีข้อมูลใดๆ ส่งไป
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

                    // ทำการอ่านผลลัพธ์ในรูปแบบออบเจกต์
                    ResponseBody responseBody = response.body();

                    // ส่ง ผลลัพธ์ ไปแสดงผลใน resultView โดยนำ responseBody มาแปลงเป็น string
                    return responseBody.string();
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

                // อัพเดทข้อความ
                resultView.setText(s);

                // ปิด ProgressDialog
                dialog.dismiss();
            }
        };

        // สั่งให้ AsyncTask เริ่มต้นทำงาน
        task.execute();
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onClearClick(View view)
    {
        // ลบข้อความ
        resultView.setText("");
    }

}
