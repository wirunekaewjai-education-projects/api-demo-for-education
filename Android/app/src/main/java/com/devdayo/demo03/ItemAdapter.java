package com.devdayo.demo03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devdayo.app.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wirune on 4/12/2016.
 */
// สร้างคลาสสำหรับเป็นตัวเชื่อมระหว่าง List<Item> กับ ListView หรือ Spinner
public class ItemAdapter extends BaseAdapter
{
    // ประกาศ List ทิ้งไว้โดยยังไม่สร้าง Instance
    private List<Item> items;

    // Constructor ทำเมื่อมีการ new ItemAdapter(...)
    public ItemAdapter(List<Item> items)
    {
        // กำหนดค่าให้ items
        this.items = items;
    }

    // เมธอดที่สร้างเองเพื่อใช้ลบข้อมูลใน List
    public void remove(Item item)
    {
        items.remove(item);
    }

    // เมธอดที่เขียนทับ BaseAdapter เพื่ออ่านค่าจำนวนข้อมูลใน List
    @Override
    public int getCount()
    {
        return items.size();
    }

    // เมธอดที่เขียนทับ BaseAdapter เพื่ออ่านข้อมูลใน List
    @Override
    public Item getItem(int position)
    {
        return items.get(position);
    }

    // เมธอดที่เขียนทับ BaseAdapter เพื่ออ่านไอของข้อมูลใน List ตาม position (index) ที่ต้องการ
    @Override
    public long getItemId(int position)
    {
        return items.get(position).getId();
    }

    // เมธอดที่เขียนทับ BaseAdapter เพื่อกำหนด View ที่ต้องการแสดงผล
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        /*
            ดูเรื่อง ListView และ Adapter ได้ที่
            https://www.youtube.com/watch?v=B0C6x8m-0ls&list=PLM3OudYHhzP3PyjhEAXwMRrKE64nvcAyP
         */

        // ประกาศตัวแปร ViewHolder ทิ้งไว้
        ViewHolder holder;

        // ตรวจสอบว่า convertView ถูกสร้างหรือยัง
        if(null == convertView)
        {
            /*
                convertView คือ view ที่เกิดมาเพื่อนำมาใช้ซ้ำๆ

                สมมติว่ามีข้อมูล 1000 ไอเทม และ หน้าจอโทรศัพท์สามารถรองรับการแสดงผลได้ประมาณ 10 View
                (แต่ละรุ่นขนาดต่างกัน จำนวน View ที่สามารถแสดงผลได้ก็ขึ้นอยู่กับขนาดของจอ)

                เมื่อจะสร้าง View ที่ 11 12 13 ++ มันก็จะไปอยู่นอกจอละ แล้วจะสร้างทำไม Adapter เลยบอกว่ายังไม่สร้างดีกว่า
                แล้วมันก็รอให้ผู้ใช้ Scroll หน้าจอเพื่อเลื่อน ListView ขึ้นลง ทีนี้ View ที่ 11 หรือมากกว่านั้นจะต้องถูกสร้างละ
                แต่ลองคิดอีกทีถ้าผู้ใช้ Scroll ไปถึงไอเทมที่ 100 มันไม่ต้องสร้าง 100 View เลยรึ ?

                ด้วยความชาญฉลาดของ Adapter มันมีการตรวจสอบด้วยว่า View ไหนที่สร้างแล้ว
                ถูก Scroll ออกไปนอกจอแสดงผลให้ทำการเก็บไว้ก่อนอย่าเพิ่งเอาไปทำลาย (ในที่นี้เรียกว่า Pool ละกัน)
                ที่นี่กลับมาที่ View 11, 12, 13 ... จะเห็นว่าตอนนี้เรามี View ใน Pool ที่ไม่ได้ใช้อยู่
                ก็เอามันมาใช้ซ้ำซะเลย แล้วตั้งชื่อให้มันว่า convertView

                ปล. ถ้าอ่านแล้วไม่เคลียร์ลองอ่านเรื่อง Object Pool Pattern เพิ่มครับ
                https://en.wikipedia.org/wiki/Object_pool_pattern
            */

            // ออบเจกต์สำหรับสร้าง Layout จาก XML
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            // สั่งให้ inflater สร้าง Layout โดยให้อ้างอิงขนาดจาก parent และยังไม่ต้องทำการ addView ที่สร้างลงใน parent (ตรง false)
            convertView = inflater.inflate(R.layout.demo_03_activity_02_item, parent, false);

            // สร้าง ViewHolder เพื่อเก็บ View ย่อยๆ ที่เชื่อมกับ convertView
            holder = new ViewHolder(convertView);

            // ฝาก ViewHolder ไว้กับ convertView (ประมาณปรสิตเลย ถ้า convertView ยังอยู่ ViewHolder ก็ยังไม่ตาย)
            convertView.setTag(holder);
        }
        else
        {
            // ในกรณีที่ convertView เคยสร้างแล้วจะเข้าเงื่อนไขนี้
            // ทำการดึงค่าที่ฝากไว้ใน convertView แล้วแปลงเป็น ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        // อัพเดท View จากข้อมูลใน Item
        holder.titleView.setText(item.getTitle());
        holder.excerptView.setText(item.getExcerpt());
        holder.createdDateView.setText(item.getCreatedDate());

        return convertView;
    }

    class ViewHolder
    {
        // @Bind ใช้แทน findViewById ซึ่งจะต้องติดตั้งไลบรารี่ ButterKnife ก่อนใช้

        @Bind(R.id.title_view)
        TextView titleView;

        @Bind(R.id.excerpt_view)
        TextView excerptView;

        @Bind(R.id.created_date_view)
        TextView createdDateView;

        public ViewHolder(View convertView)
        {
            // เชื่อมตัวแปรกับ View ใน convertView ตาม id ที่กำหนดไว้
            ButterKnife.bind(this, convertView);
        }
    }
}

