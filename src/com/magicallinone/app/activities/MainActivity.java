package com.magicallinone.app.activities;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.DeckListLoaderFragment;
import com.magicallinone.app.fragment.SetsListLoaderFragment;
import com.magicallinone.app.listeners.DrawerItemClickListener;
import com.xtremelabs.imageutils.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements DrawerItemClickListener, ViewBinder {
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private SimpleAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private int mCurrentFragment;

	public static final String[] KEYS = { Keys.TITLE, Keys.IMAGE, };
	public static final int[] VIEWS = { R.id.list_item_drawer_text, R.id.list_item_drawer_image, };

	public static final class Keys {
		public static final String ROW_ID = "rowid";
		public static final String TITLE = "title";
		public static final String IMAGE = "image";
	}

	public static class DrawerItems {
		public static final int SETS = 0;
		public static final int LIFE_COUNTER = 1;
		public static final int SEARCH = 2;
		public static final int DECKBUILDER = 3;
	}

	public static void newInstance(final Context context) {
		final Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final String[] strings = getResources().getStringArray(R.array.drawer_items);
		final String[] images = {String.valueOf(R.drawable.set), String.valueOf(R.drawable.dice), String.valueOf(R.drawable.search), String.valueOf(R.drawable.card_stack)};

		final List<HashMap<String, String>> drawerItems = new ArrayList<HashMap<String, String>>();
		int count = 0;
		for (int i = 0; i < 4; i++) {
			final HashMap<String, String> map = new HashMap<String, String>();
			map.put(Keys.ROW_ID, "" + count++);
			map.put(Keys.TITLE, strings[i]);
			map.put(Keys.IMAGE, images[i]);
			drawerItems.add(map);
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
			}

			public void onDrawerOpened(View drawerView) {
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mAdapter = new SimpleAdapter(this, drawerItems, R.layout.list_item_drawer, KEYS, VIEWS);
		mAdapter.setViewBinder(this);
		mDrawerList.setAdapter(mAdapter);

		final FragmentManager fragmentManager = getFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final SetsListLoaderFragment setsListFragment = SetsListLoaderFragment.newInstance();
        fragmentTransaction.add(R.id.content_frame, setsListFragment, SetsListLoaderFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		mCurrentFragment = DrawerItems.SETS;

		final ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean setViewValue(final View view, final Object data, final String textRepresentation) {
		final TextView textView;
		final ImageView imageView;
		switch (view.getId()) {
		case R.id.list_item_drawer_text:
			textView = (TextView) view;
			textView.setText(textRepresentation);
			return true;
		case R.id.list_item_drawer_image:
			imageView = (ImageView) view;
			final int resource = Integer.parseInt(textRepresentation);
            final ImageLoader imageLoader = getImageLoader();
            imageLoader.loadImageFromResource(imageView, resource);
			return true;
		}
		return false;
	}

	public void goToFragment(final Fragment fragment, final String tag) {
		final FragmentManager fragmentManager = getFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment, tag);
		fragmentTransaction.commit();
		closeDrawer();
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		switch (position) {
		case DrawerItems.SETS:
			if (mCurrentFragment != DrawerItems.SETS) {
                final SetsListLoaderFragment setsListFragment = SetsListLoaderFragment.newInstance();
                goToFragment(setsListFragment, SetsListLoaderFragment.class.getCanonicalName());
            }
			break;
		case DrawerItems.SEARCH:
			break;
		case DrawerItems.DECKBUILDER:
			if (mCurrentFragment != DrawerItems.DECKBUILDER) {
                final DeckListLoaderFragment deckListFragment = DeckListLoaderFragment.newInstance();
                goToFragment(deckListFragment, DeckListLoaderFragment.class.getCanonicalName());
            }
			break;
		case DrawerItems.LIFE_COUNTER:
			break;
		}
		mCurrentFragment = position;
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
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
