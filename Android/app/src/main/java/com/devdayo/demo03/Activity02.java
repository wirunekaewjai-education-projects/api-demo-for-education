package com.devdayo.demo03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devdayo.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class Activity02 extends AppCompatActivity
{
    // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // เชื่อม Activity กับ View XML (res/layout/...)
        setContentView(R.layout.demo_03_activity_02);

        // เชื่อมตัวแปรกับ View XML ตาม id ที่กำหนดไว้
        ButterKnife.bind(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), Activity02ById.class);
                intent.putExtra("id", id);

                startActivity(intent);
            }
        });

        // สั่งให้เริ่มต้นรีเควส
        request();
    }

    public void request()
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
                    ดังนั้น Activity02 ที่สืบทอด AppCompatActivity จึงสามารถเปลี่ยนตัวเองเป็น Context ได้
                    ตามหลัก Polymorphism (https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
                 */
                context = Activity02.this;

                // แสดง ProgressDialog
                dialog = ProgressDialog.show(context, "Demo 03", "Executing...");
            }

            // เป็นการทำงานใน Background Thread ซึ่งมีไว้เพื่อทำงานเล็กหรือใหญ่โดยไม่ทำให้ UI กระตุก
            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    // สั่งให้ Background Thread หยุดงานเป็นเวลา 0.5 วินาที (500 มิลลิวินาที)
                    Thread.sleep(500);

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
                String url = base_url + "/Demo-03/02-Read.php";

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
                    // แปลงสตริงเป็น JSONArray
                    JSONArray array = new JSONArray(s);
                    List<Item> items = Item.parse(array);

                    ListAdapter adapter = new ListAdapter(items);
                    listView.setAdapter(adapter);
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

        // สั่งให้ AsyncTask เริ่มต้นทำงาน
        task.execute();
    }
}

class ListAdapter extends BaseAdapter
{
    private List<Item> items;

    public ListAdapter(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Item getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if(null == convertView)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.demo_03_activity_02_item, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        holder.titleView.setText(item.getTitle());
        holder.excerptView.setText(item.getExcerpt());
        holder.createdDateView.setText(item.getCreatedDate());

        return convertView;
    }

    class ViewHolder
    {
        @Bind(R.id.title_view)
        TextView titleView;

        @Bind(R.id.excerpt_view)
        TextView excerptView;

        @Bind(R.id.created_date_view)
        TextView createdDateView;

        public ViewHolder(View convertView)
        {
            ButterKnife.bind(this, convertView);
        }
    }
}

class Item
{
    private long id;
    private String title;
    private String excerpt;
    private String createdDate;

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getExcerpt()
    {
        return excerpt;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public static Item parse(JSONObject object)
    {
        Item item = new Item();

        item.id = object.optInt("id");
        item.title = object.optString("title");
        item.excerpt = object.optString("excerpt");
        item.createdDate = object.optString("created_date");

        return item;
    }

    public static List<Item> parse(JSONArray array)
    {
        List<Item> items = new ArrayList<>();

        int length = array.length();
        for (int i = 0; i < length; i++)
        {
            JSONObject object = array.optJSONObject(i);
            Item item = parse(object);

            items.add(item);
        }

        return items;
    }
}