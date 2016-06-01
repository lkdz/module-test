package com.lkdz.rfwirelessmoduletest1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkdz.rfwirelessmoduletest1.R;

import java.util.ArrayList;

/**
 * Created by 廖慧 on 2016/5/31.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> gList;
    private ArrayList<ArrayList<String>> cList;
    private Context context;

    public MyExpandableListViewAdapter(ArrayList<String> gList, ArrayList<ArrayList<String>> cList, Context context){

        this.gList=gList;
        this.cList=cList;
        this.context=context;
    }


    public Object getChild(int groupPosition, int childPosition) {
        return cList.get(groupPosition).get(childPosition);
    }

    private int selectedGroupPosition = -1;
    private int selectedChildPosition = -1;

    public void setSelectedPosition(int selectedGroupPosition, int selectedChildPosition) {
        this.selectedGroupPosition = selectedGroupPosition;
        this.selectedChildPosition = selectedChildPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return cList.get(groupPosition).size();
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setPadding(32, 10, 0, 10);
            convertView = textView;
            notifyDataSetChanged();
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(getChild(groupPosition, childPosition).toString());

        if (groupPosition == selectedGroupPosition) {
            if (childPosition == selectedChildPosition) {
                textView.setBackgroundColor(0xffb6ddee);
            } else {
                textView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
//        textView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                setSelectedPosition(groupPosition, childPosition);
//                notifyDataSetChanged();
//            }
//        });
        return textView;
    }

    public Object getGroup(int groupPosition) {
        return gList.get(groupPosition);
    }

    public int getGroupCount() {
        return gList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout cotain = new LinearLayout(context);
        cotain.setBackgroundResource(R.color.colorbutton);
        cotain.setPadding(0, 10, 0, 10);
        cotain.setGravity(Gravity.CENTER_VERTICAL);
        ImageView imgIndicator = new ImageView(context);
        TextView textView = new TextView(context);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(5, 0, 0, 0);

        if (isExpanded) {
            imgIndicator.setBackgroundResource(R.mipmap.ic_keyboard_arrow_down_black_24dp);
        } else {
            imgIndicator.setBackgroundResource(R.mipmap.ic_keyboard_arrow_right_black_24dp);
        }
        cotain.addView(imgIndicator);
        cotain.addView(textView);
        return cotain;

    }


    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
