package com.magicallinone.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.magicallinone.app.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageUtils {
	private static final String IMAGE_BASE_URL = "http://magiccards.info/scans/en/";

	public static final class Folders {
		public static final String MANA_SYMBOLS = "mana_symbols";
		public static final String LOGOS = "set_logos";
		public static final String SET_SYMBOL = "set_symbols";
		public static final String WATERMARKS = "watermarks";
	}

	public static String getImageUrl(String set, int number) {
		String imageUrl = IMAGE_BASE_URL + set.toLowerCase() + "/" + number
				+ ".jpg";
		return imageUrl;
	}

	public static ImageSpan getManaSymbolImageSpan(Context context,
			Drawable drawable) {
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 6,
				drawable.getIntrinsicHeight() * 6);
		return new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
	}

	public static Drawable getDrawable(Context context, String folder,
			String filename) {
		Drawable drawable = null;
		String filePath = folder + "/" + filename + ".png";
		try {
			InputStream inputStream = context.getAssets().open(filePath);
			drawable = Drawable.createFromStream(inputStream, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static Spannable replaceManaSymbols(Context context, String text) {
		SpannableStringBuilder spannableText = new SpannableStringBuilder(text);
		List<MatchResult> matchResults = new ArrayList<MatchResult>();
		getManaSymbols(text, matchResults);
		for (MatchResult matchResult : matchResults) {
			Drawable drawable = getDrawable(context, Folders.MANA_SYMBOLS,
					createFilename(matchResult));
			if (drawable != null) {
				spannableText.setSpan(
						getManaSymbolImageSpan(context, drawable),
						matchResult.start(), matchResult.end(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		return spannableText;
	}

	public static void getWatermark(Context context, ImageView imageView,
			String watermark) {
		imageView.setImageDrawable(ImageUtils.getDrawable(context,
				Folders.WATERMARKS, watermark));
		imageView.setVisibility(View.VISIBLE);
	}

	public static void getManaSymbols(String manaCost, List<MatchResult> matchResults) {
		if (manaCost != null && !manaCost.equals("")) {
			Pattern pattern = Pattern.compile("\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(manaCost);
			while (matcher.find()) {
				matchResults.add(matcher.toMatchResult());
			}
		}
	}

	public static void addManaSymbols(Context context, LayoutInflater inflater,
			LinearLayout linearLayout, List<MatchResult> manaSymbols) {
		for (MatchResult symbol : manaSymbols) {
			addSymbol(context, inflater, linearLayout, symbol);
		}
	}

	public static void addSymbol(Context context, LayoutInflater inflater,
			LinearLayout linearLayout, MatchResult symbol) {
		View manaSymbol = inflater
				.inflate(R.layout.list_item_mana_symbol, null);
		((ImageView) manaSymbol.findViewById(R.id.list_item_mana_symbol_image))
				.setImageDrawable(getDrawable(context, Folders.MANA_SYMBOLS,
						createFilename(symbol)));
		linearLayout.addView(manaSymbol);
	}

	public static String createFilename(MatchResult result) {
		return result.group().replace("/", "_").replace("{", "")
				.replace("}", "").toLowerCase();
	}
}
