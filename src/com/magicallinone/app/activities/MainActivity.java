package com.magicallinone.app.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.SetsListFragment;

public class MainActivity extends BaseFragmentActivity {
	private String[] mDrawerItems;
//	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	public static void newInstance(Context context){
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerItems = getResources().getStringArray(R.array.drawer_items);
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_drawer, mDrawerItems));
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.content_frame, SetsListFragment.newInstance(), SetsListFragment.class.getCanonicalName());
		fragmentTransaction.commit();
	}
}
