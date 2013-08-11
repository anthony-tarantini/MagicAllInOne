package com.magicallinone.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.DeckListFragment;
import com.magicallinone.app.fragment.SetsListFragment;
import com.magicallinone.app.managers.FontManager;
import com.magicallinong.app.listeners.DrawerItemClickListener;

public class MainActivity extends BaseFragmentActivity implements
		DrawerItemClickListener, ViewBinder {
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private SimpleAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private int mCurrentFragment;

	public static String[] KEYS = { Keys.TITLE, };
	public static int[] VIEWS = { R.id.list_item_drawer_text, };

	public static final class Keys {
		public static final String ROW_ID = "rowid";
		public static final String TITLE = "title";
	}

	public static class DrawerItems {
		public static final int SETS = 0;
		public static final int LIFE_COUNTER = 1;
		public static final int SEARCH = 2;
		public static final int DECKBUILDER = 3;
	}

	public static void newInstance(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] strings = getResources().getStringArray(R.array.drawer_items);

		List<HashMap<String, String>> drawerItems = new ArrayList<HashMap<String, String>>();
		int count = 0;
		for (String string : strings) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Keys.ROW_ID, "" + count++);
			map.put(Keys.TITLE, string);
			drawerItems.add(map);
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
			}

			public void onDrawerOpened(View drawerView) {
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mAdapter = new SimpleAdapter(this, drawerItems,
				R.layout.list_item_drawer, KEYS, VIEWS);
		mAdapter.setViewBinder(this);
		mDrawerList.setAdapter(mAdapter);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.content_frame,
				SetsListFragment.newInstance(),
				SetsListFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		mCurrentFragment = DrawerItems.SETS;

		ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		TextView textView;
		switch (view.getId()) {
		case R.id.list_item_drawer_text:
			textView = (TextView) view;
			textView.setText(textRepresentation);
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			break;
		}
		return false;
	}

	public void goToFragment(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment, tag);
		fragmentTransaction.commit();
		closeDrawer();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case DrawerItems.SETS:
			if (mCurrentFragment != DrawerItems.SETS)
				goToFragment(SetsListFragment.newInstance(),
						SetsListFragment.class.getCanonicalName());
			break;
		case DrawerItems.SEARCH:
			break;
		case DrawerItems.DECKBUILDER:
			if (mCurrentFragment != DrawerItems.DECKBUILDER)
				goToFragment(DeckListFragment.newInstance(),
						DeckListFragment.class.getCanonicalName());
			break;
		case DrawerItems.LIFE_COUNTER:
			break;
		}
		mCurrentFragment = position;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggleDrawer();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}

	private void closeDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}
}
