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

        int length = Math.min(content.length(), 64);
        this.excerpt = content.substring(0, length);
    }

    public static Item parse(JSONObject object)
    {
        Item item = new Item();

        item.id = object.optInt("id");
        item.title = object.optString("title");
        item.excerpt = object.optString("excerpt");
        item.content = object.optString("content");
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