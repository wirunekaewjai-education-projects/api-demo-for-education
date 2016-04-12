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
public class ItemAdapter extends BaseAdapter
{
    private List<Item> items;

    public ItemAdapter(List<Item> items)
    {
        this.items = items;
    }

    public void remove(Item item)
    {
        items.remove(item);
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

