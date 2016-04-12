package com.devdayo.demo02;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.devdayo.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Activity01 extends AppCompatActivity
{
    // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

    @Bind(R.id.a_view)
    protected EditText aView;

    @Bind(R.id.b_view)
    protected EditText bView;

    @Bind(R.id.operator_spinner)
    protected Spinner operatorSpinner;

    @Bind(R.id.result_view)
    protected TextView resultView;

    @Bind(R.id.json_view)
    protected TextView jsonView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.demo_02_activity_01);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);

        // สร้าง Dropdown Item ของ Operator Spinner
        ArrayList<String> operators = new ArrayList<>();
        operators.add("add");
        operators.add("subtract");
        operators.add("multiply");
        operators.add("divide");

        // อ่านเรื่อง Spinner ได้ที่ http://devahoy.com/posts/android-spinner-example/
        // กำหนดรูปแบบเลย์เอาต์ของ Item โดยใช้เลย์เอาต์จากไลบรารี่ที่แอนดรอยด์ทำไว้อยู่แล้ว
        int spinner_layout = android.R.layout.simple_dropdown_item_1line;

        // สร้างตัวเชื่อมระหว่าง Spinner กับ Items ด้วย ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, spinner_layout, operators);
        operatorSpinner.setAdapter(adapter);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onExecuteClick(View view)
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
                dialog = ProgressDialog.show(context, "Demo 02", "Executing...");
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
                String url = base_url + "/Demo-02/01-GetJSONObjectCalculator.php";

                // https://en.wikipedia.org/wiki/Query_string
                // เพิ่ม queryString (params คือ สตริงที่ส่งมาตอน execute ซึ่ง length ของ params จะเท่ากับจำนวนสตริงที่ส่งมา)
                url = url + "?a=" + params[0] + "&b=" + params[1] + "&operator=" + params[2];

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

                try
                {
                    // แปลงสตริงเป็น JSONObject
                    JSONObject object = new JSONObject(s);

                    // อ่านค่าจาก key "result"
                    String result = object.optString("result");

                    // อัพเดทผลลัพธ์
                    resultView.setText(result);
                }
                // เมื่อมีการแปลงสตริงเป็น JSONObject ต้องดัก catch JSONException เสมอ
                // เพราะสตริงอาจจะไม่ใช่ JSON Format ที่ถูกต้อง
                catch (JSONException e)
                {
                    // แสดงผล Error ใน Log Monitor
                    e.printStackTrace();
                }

                // อัพเดทข้อความ
                jsonView.setText(s);

                // ปิด ProgressDialog
                dialog.dismiss();
            }
        };

        // อ่านค่าที่ผู้ใช้กรอกแล้วแปลงเป็นสตริง
        String a = aView.getText().toString();
        String b = bView.getText().toString();
        String op = operatorSpinner.getSelectedItem().toString();

        // สั่งให้ AsyncTask เริ่มต้นทำงาน โดยส่งสตริง a, b และ operator ไปด้วย
        task.execute(a, b, op);
    }

    // ทำการผูกเมธอดเอาไว้ใน XML
    public void onClearClick(View view)
    {
        // ลบข้อความ
        aView.setText("");
        bView.setText("");
        resultView.setText("");
        jsonView.setText("");
    }
}
