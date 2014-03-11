package com.magicallinone.app.fragment;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.magicallinone.app.R;
import com.magicallinone.app.application.MagicApplication;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.listeners.OnCardSelectedListener;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.services.ApiService;
import com.magicallinone.app.utils.ImageUtils;
import com.xtremelabs.imageutils.ImageLoaderListener;
import com.xtremelabs.imageutils.ImageReturnedFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

public class CardListFragment extends BaseFragment implements ViewBinder,
		OnItemClickListener, LoaderCallbacks<Cursor> {

	public static final String[] COLUMNS = { CardsView.Columns.NAME,
			CardsView.Columns.MANA_COST, CardsView.Columns.RULES_TEXT,
			CardsView.Columns.FLAVOUR_TEXT, CardsView.Columns.NUMBER,
			CardsView.Columns.WATERMARK, };
	public static final int[] VIEW_IDS = { R.id.list_item_card_name,
			R.id.list_item_card_mana_cost_layout,
			R.id.list_item_card_rules_text, R.id.list_item_card_flavour_text,
			R.id.list_item_card_image, R.id.list_item_card_watermark, };

	public static final class Extras {
		public static final String SET = "set";
	}

	public class SetReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			loadSet();
			mProgressDialog.dismiss();
		}
	}

	private LoaderManager mLoaderManager;
	private SimpleCursorAdapter mAdapter;
	private ListView mListView;
	private String mSetId;
	private CursorLoader mCursorLoader;
	private SetReceiver mReceiver;
	private IntentFilter mFilter;
	private ProgressDialog mProgressDialog;
	private OnCardSelectedListener mOnCardSelectedListener;

	public static CardListFragment newInstance(String setId) {
		CardListFragment cardListFragment = new CardListFragment();
		Bundle args = cardListFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		args.putString(Extras.SET, setId);
		cardListFragment.setArguments(args);
		return cardListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Loading Cards");
		mProgressDialog.setMessage("Stand By ...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		mSetId = getArguments().getString(Extras.SET);

		mLoaderManager = getLoaderManager();
		getImageLoader().setDefaultOptions(
				MagicApplication.getImageLoaderOptions());

		createReceiver();
		startApiService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_list, container,
				false);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_card, null, COLUMNS, VIEW_IDS,
				SimpleCursorAdapter.NO_SELECTION);
		mAdapter.setViewBinder(this);
		mListView = (ListView) view.findViewById(R.id.card_listing);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mLoaderManager = getLoaderManager();

		return view;
	}

	public void loadSet() {
		mLoaderManager.initLoader(0, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int cardId = ((Integer) view.findViewById(R.id.list_item_card_image)
				.getTag()).intValue();
		mOnCardSelectedListener.onCardSelected(cardId);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		TextView textView;
		ImageView imageView;
		LinearLayout linearLayout;
		switch (view.getId()) {
		case R.id.list_item_card_rules_text:
			textView = (TextView) view;
			String text = cursor.getString(columnIndex);
			if (text != null) {
				textView.setText(
						ImageUtils.replaceManaSymbols(getActivity(), text),
						BufferType.SPANNABLE);
			} else {
				textView.setText(text);
			}
			return true;
		case R.id.list_item_card_flavour_text:
			textView = (TextView) view;
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_card_name:
			textView = (TextView) view;
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_card_mana_cost_layout:
			List<MatchResult> manaSymbols = new ArrayList<MatchResult>();
			ImageUtils.getManaSymbols(cursor.getString(columnIndex),
					manaSymbols);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			linearLayout = (LinearLayout) view;
			linearLayout.removeAllViews();
			if (manaSymbols != null) {
				ImageUtils.addManaSymbols(getActivity(), inflater,
						linearLayout, manaSymbols);
			}
			return true;
		case R.id.list_item_card_watermark:
			String watermark = cursor.getString(columnIndex);
			imageView = (ImageView) view;
			if (watermark != null && !watermark.equals(""))
				ImageUtils.getWatermark(getActivity(), imageView, watermark);
			else
				imageView.setVisibility(View.GONE);
			return true;
        //literally doing this in order to test
		case R.id.list_item_card_image:
			imageView = (ImageView) view;
			getImageLoader().loadImage(
					imageView,
					ImageUtils.getImageUrl(cursor.getString(cursor
							.getColumnIndex(CardsView.Columns.SET_ID)), cursor
							.getInt(columnIndex)), new ImageLoaderListener() {

						@Override
						public void onImageLoadError(String arg0) {
						}

						@Override
						public void onImageAvailable(final ImageView imageView,
								final Bitmap bitmap,
								final ImageReturnedFrom imageReturnedFrom) {
							imageView.clearAnimation();
							if (imageReturnedFrom != ImageReturnedFrom.MEMORY) {
								Animation myCardFlipAnimation = AnimationUtils
										.loadAnimation(getActivity(),
												R.anim.anim_to_middle);
								myCardFlipAnimation
										.setAnimationListener(new AnimationListener() {

											@Override
											public void onAnimationStart(
													Animation animation) {}

											@Override
											public void onAnimationRepeat(
													Animation animation) {}

											@Override
											public void onAnimationEnd(
													Animation animation) {
												imageView
														.setImageBitmap(bitmap);
												imageView
														.startAnimation(AnimationUtils
																.loadAnimation(
																		getActivity(),
																		R.anim.anim_from_middle));
											}
										});
								imageView.startAnimation(myCardFlipAnimation);
							} else {
                                imageView.setImageBitmap(bitmap);
                            }
						}
					});
			imageView.setTag(cursor.getInt(cursor
					.getColumnIndex(CardsView.Columns.CARD_ID)));
			return true;
		}
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = CardsView.Columns.SET_ID + " = ?";
		String[] selectionArgs = { mSetId, };
		String orderBy = CardsView.Columns.NUMBER + " ASC";
		mCursorLoader = new CursorLoader(getActivity(),
				MagicContentProvider.Uris.CARDS_URI, null, selection,
				selectionArgs, orderBy);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (mAdapter != null && cursor != null)
			mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void createReceiver() {
		mFilter = new IntentFilter(ApiService.Actions.SET_READ);
		mFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new SetReceiver();
	}

	private void startApiService() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.putExtra(ApiService.Extras.QUERY, mSetId);
		intent.putExtra(ApiService.Extras.OPERATION,
				ApiService.Operations.SINGLE_SET);
		getActivity().startService(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
		mReceiver = null;
		mOnCardSelectedListener = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		createReceiver();
		getActivity().registerReceiver(mReceiver, mFilter);
	}

	public void setOnCardSelectedListener(OnCardSelectedListener listener) {
		mOnCardSelectedListener = listener;
	}
}
