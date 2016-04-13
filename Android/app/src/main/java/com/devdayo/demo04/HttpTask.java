package com.devdayo.demo04;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.devdayo.app.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wirune on 4/13/2016.
 */

// อ่านเรื่อง AsyncTask ได้ที่ http://devahoy.com/posts/android-asynctask-tutorial/
// กำหนดให้ Param เป็น GenericType ชื่อ T โดยจะต้องไปกำหนดตอนสร้างออบเจกต์อีกที
// อ่านเรื่อง GenericType ได้ที่ https://docs.oracle.com/javase/tutorial/java/generics/types.html
public abstract class HttpTask<T> extends AsyncTask<T, Void, Response>
{
    private Context context;
    private long delay;

    // อ่านเรื่อง ProgressDialog ได้ที่
    // https://examples.javacodegeeks.com/android/core/ui/progressdialog/android-progressdialog-example/

    // ประกาศตัวแปรไว้ใช้เก็บ ProgressDialog
    private ProgressDialog dialog;

    // สร้าง Constructor โดยรับ Context และ Delay มาด้วย (@NonNull หมายถึงค่าที่ส่งมาห้ามเป็น Null)
    public HttpTask(@NonNull Context context, long delay)
    {
        this.context = context;
        this.delay = delay;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        // อ่านค่าสตริงจาก R.string.app_name
        String title = context.getString(R.string.app_name);

        // แสดง ProgressDialog
        dialog = ProgressDialog.show(context, title, "Executing...");
    }

    @Override
    protected Response doInBackground(T... params)
    {
        if(delay > 0)
        {
            try
            {
                // สั่งให้ Background Thread หยุดงานเป็นเวลาที่กำหนดไว้ใน delay
                Thread.sleep(delay);

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
        }

        // เรียกใช้ abstract method
        return onExecute(params);
    }

    @Override
    protected void onPostExecute(Response response)
    {
        super.onPostExecute(response);

        if(null != response)
        {
            // 2xx ใน HTTP Response Code คือ สำเร็จ
            // อ่านเรื่อง HTTP Response Code ได้ที่ https://goo.gl/MXVo6k
            if(response.isSuccessful())
            {
                // เรียกใช้ abstract method
                onSuccessful(response);
            }
            else
            {
                // เรียกใช้ abstract method
                onFailure(response);
            }
        }

        // ปิด ProgressDialog
        dialog.dismiss();
    }

    protected Response doRequest(Request request)
    {
        // สร้างออบเจกต์สำหรับ HTTP Request เรียกว่า HttpClient
        OkHttpClient client = new OkHttpClient();

        // สร้างออกเจกต์สำหรับเรียกใช้งาน Request
        Call call = client.newCall(request);

        try
        {
            // ทำการ Request
            return call.execute();
        }
        // ในการใช้ call.execute() จะต้องดัก catch(IOException) เสมอ
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // รีเควสมีปัญหาหรืออาจจะลืมต่อเน็ตเลยรีเควสไม่ได้
        return null;
    }

    // ประกาศ abstract method ทิ้งไว้ให้ไป override การทำงานเพิ่มเติมเอง
    protected abstract Response onExecute(T... params);
    protected abstract void onSuccessful(Response response);
    protected abstract void onFailure(Response response);

    //
    public Context getContext()
    {
        return context;
    }
}
