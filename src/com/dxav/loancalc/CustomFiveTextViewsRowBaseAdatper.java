/**
 *    Copyright 2014 Xavier Delaunay
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.dxav.loancalc;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomFiveTextViewsRowBaseAdatper extends BaseAdapter {
	
private static ArrayList<Vector<String>> rowDataList;
private LayoutInflater mInflater;

	/**
	 * Constructor
	 */
	public CustomFiveTextViewsRowBaseAdatper(Context context,
			ArrayList<Vector<String>> installments) {
		rowDataList = installments;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return rowDataList.size();
	}

	public Object getItem(int position) {
		return rowDataList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.custom_five_textviews_row_for_listview, null);
			holder = new ViewHolder();
			holder.txtView1 = (TextView) convertView
					.findViewById(R.id.textview1);
			holder.txtView2 = (TextView) convertView
					.findViewById(R.id.textview2);
			holder.txtView3 = (TextView) convertView
					.findViewById(R.id.textview3);
			holder.txtView4 = (TextView) convertView
					.findViewById(R.id.textview4);
			holder.txtView5 = (TextView) convertView
					.findViewById(R.id.textview5);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtView1.setText(rowDataList.get(position).get(0));
		holder.txtView2.setText(rowDataList.get(position).get(1));
		holder.txtView3.setText(rowDataList.get(position).get(2));
		holder.txtView4.setText(rowDataList.get(position).get(3));
		holder.txtView5.setText(rowDataList.get(position).get(4));

		return convertView;
	}

	static class ViewHolder {
		TextView txtView1;
		TextView txtView2;
		TextView txtView3;
		TextView txtView4;
		TextView txtView5;
	}

}