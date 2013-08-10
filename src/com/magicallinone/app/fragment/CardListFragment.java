package com.magicallinone.app.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.magicallinone.app.managers.FontManager;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.utils.ImageUtils;
import com.xtremelabs.imageutils.ImageLoader;

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

	private LoaderManager mLoaderManager;
	private SimpleCursorAdapter mAdapter;
	private ListView mListView;
	private String mSetId;
	private CursorLoader mCursorLoader;
	private ImageLoader mLoader;

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

		mSetId = getArguments().getString(Extras.SET);

		mLoaderManager = getLoaderManager();
		mLoader = ImageLoader.buildImageLoaderForFragment(this);
		mLoader.setDefaultOptions(MagicApplication.getImageLoaderOptions());
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
		mLoader = ImageLoader.buildImageLoaderForFragment(this);
		mLoader.setDefaultOptions(MagicApplication.getImageLoaderOptions());

		return view;
	}

	public void loadSet() {
		mLoaderManager.initLoader(0, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			if (text != null) {
				textView.setText(replaceManaSymbols(text), BufferType.SPANNABLE);
			} else {
				textView.setText(text);
			}
			return true;
		case R.id.list_item_card_flavour_text:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont(),
					Typeface.ITALIC);
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_card_name:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont(),
					Typeface.BOLD);
			textView.setTextSize(18);
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_card_mana_cost_layout:
			List<MatchResult> manaSymbols = new ArrayList<MatchResult>();
			getManaSymbols(cursor.getString(columnIndex), manaSymbols);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			linearLayout = (LinearLayout) view;
			linearLayout.removeAllViews();
			if (manaSymbols != null) {
				addManaSymbols(inflater, linearLayout, manaSymbols);
			}
			return true;
		case R.id.list_item_card_watermark:
			String watermark = cursor.getString(columnIndex);
			imageView = (ImageView) view;
			if (watermark != null && !watermark.equals(""))
				getWatermark(imageView, watermark);
			else
				imageView.setVisibility(View.GONE);
			return true;
		case R.id.list_item_card_image:
			imageView = (ImageView) view;
			mLoader.loadImage(imageView,
					ImageUtils.getImageUrl(mSetId, cursor.getInt(columnIndex)));
			return true;
		}
		return false;
	}

	private Spannable replaceManaSymbols(String text) {
		SpannableStringBuilder spannableText = new SpannableStringBuilder(text);
		List<MatchResult> matchResults = new ArrayList<MatchResult>();
		getManaSymbols(text, matchResults);
		for (MatchResult matchResult : matchResults) {
			Drawable drawable = ImageUtils.getDrawable(getActivity(),
					ImageUtils.Folders.MANA_SYMBOLS,
					createFilename(matchResult));
			if (drawable != null) {
				spannableText.setSpan(ImageUtils.getManaSymbolImageSpan(
						getActivity(), drawable), matchResult.start(),
						matchResult.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		return spannableText;
	}

	private void getWatermark(ImageView imageView, String watermark) {
		imageView.setImageDrawable(ImageUtils.getDrawable(getActivity(),
				ImageUtils.Folders.WATERMARKS, watermark));
		imageView.setVisibility(View.VISIBLE);
	}

	private void getManaSymbols(String manaCost, List<MatchResult> matchResults) {
		if (manaCost != null && !manaCost.equals("")) {
			Pattern pattern = Pattern.compile("\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(manaCost);
			while (matcher.find()) {
				matchResults.add(matcher.toMatchResult());
			}
		}
	}

	private void addManaSymbols(LayoutInflater inflater,
			LinearLayout linearLayout, List<MatchResult> manaSymbols) {
		for (MatchResult symbol : manaSymbols) {
			addSymbol(inflater, linearLayout, symbol);
		}
	}

	private void addSymbol(LayoutInflater inflater, LinearLayout linearLayout,
			MatchResult symbol) {
		View manaSymbol = inflater
				.inflate(R.layout.list_item_mana_symbol, null);
		((ImageView) manaSymbol.findViewById(R.id.list_item_mana_symbol_image))
				.setImageDrawable(ImageUtils
						.getDrawable(getActivity(),
								ImageUtils.Folders.MANA_SYMBOLS,
								createFilename(symbol)));
		linearLayout.addView(manaSymbol);
	}

	private String createFilename(MatchResult result) {
		return result.group().replace("/", "_").replace("{", "")
				.replace("}", "").toLowerCase();
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
}
