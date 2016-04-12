package com.devdayo.demo03;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wirune on 4/12/2016.
 */
public class Item
{
    // กำหนดเป็น private เพื่อไม่ใช้ภายนอกเข้ามาแก้ไขโดยตรง
    private long id;
    private String title;
    private String excerpt;
    private String content;
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

    public String getContent()
    {
        return content;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;

        // กำหนดให้ excerpt มีขนาดไม่เกิน 64 ตัวอักษร
        int length = Math.min(content.length(), 64);
        this.excerpt = content.substring(0, length);
    }

    // แปลง JSONObject เป็น Item
    public static Item parse(JSONObject object)
    {
        // สร้างไอเทมใหม่
        Item item = new Item();

        // อ่านค่าจาก JSONObject ด้วย key ต่างๆ เพื่อนำมากำหนดให้ตัวแปรของ Item
        item.id = object.optInt("id");
        item.title = object.optString("title");
        item.excerpt = object.optString("excerpt");
        item.content = object.optString("content");
        item.createdDate = object.optString("created_date");

        return item;
    }

    // แปลง JSONArray เป็น List<Item>
    public static List<Item> parse(JSONArray array)
    {
        // เนื่องจาก List เป็น Abstract และมีคลาสลูกมากมายทั้ง ArrayList และ Vector ซึ่งในที่นี้เลือกใช้ ArrayList ครับ
        List<Item> items = new ArrayList<>();

        // อ่านค่าของจำนวนข้อมูลใน JSONArray และวนลูป
        int length = array.length();
        for (int i = 0; i < length; i++)
        {
            // ดึงค่า JSONObject จาก JSONArray index ที่ i
            JSONObject object = array.optJSONObject(i);

            // แปลง JSONObject เป็น Item
            Item item = parse(object);

            // เพิ่มไอเทมลงใน List
            items.add(item);
        }

        return items;
    }
}