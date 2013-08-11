package com.magicallinong.app.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public interface DrawerItemClickListener extends OnItemClickListener{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id);

}
