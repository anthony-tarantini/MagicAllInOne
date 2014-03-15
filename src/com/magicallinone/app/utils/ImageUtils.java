package com.magicallinone.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.magicallinone.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {
	private static final String IMAGE_BASE_URL = "http://magiccards.info/scans/en/";

	public static final class Folders {
		public static final String MANA_SYMBOLS = "mana_symbols";
		public static final String LOGOS = "set_logos";
		public static final String SET_SYMBOL = "set_symbols";
		public static final String WATERMARKS = "watermarks";
	}

	public static String getImageUrl(final String set, final int number) {
		final String imageUrl = IMAGE_BASE_URL + set.toLowerCase() + "/" + number + ".jpg";
		return imageUrl;
	}

	public static ImageSpan getManaSymbolImageSpan(final Drawable drawable) {
        final int width = drawable.getIntrinsicWidth() * 6;
        final int height = drawable.getIntrinsicHeight() * 6;
		drawable.setBounds(0, 0, width, height);
		return new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
	}

	public static Drawable getDrawable(final Context context, final String folder, final String filename) {
		Drawable drawable = null;
		final String filePath = folder + "/" + filename + ".png";
		try {
			final InputStream inputStream = context.getAssets().open(filePath);
			drawable = Drawable.createFromStream(inputStream, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static Spannable replaceManaSymbols(final Context context, final String text) {
		final SpannableStringBuilder spannableText = new SpannableStringBuilder(text);
		final List<MatchResult> matchResults = getManaSymbols(text);

        for (final MatchResult matchResult : matchResults) {
            final String filename = createFilename(matchResult);
			final Drawable drawable = getDrawable(context, Folders.MANA_SYMBOLS, filename);
			if (drawable != null) {
                final ImageSpan imageSpan = getManaSymbolImageSpan(drawable);
                final int start = matchResult.start();
                final int end = matchResult.end();
				spannableText.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		return spannableText;
	}

	public static void getWatermark(final Context context, final ImageView imageView, final String watermark) {
		imageView.setImageDrawable(ImageUtils.getDrawable(context, Folders.WATERMARKS, watermark));
		imageView.setVisibility(View.VISIBLE);
	}

	public static List<MatchResult> getManaSymbols(final String manaCost) {
        final List<MatchResult> matchResults = new ArrayList<MatchResult>();
        final boolean hasManaCost = manaCost != null && !manaCost.isEmpty();
        if (hasManaCost) {
			final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
			final Matcher matcher = pattern.matcher(manaCost);
			while (matcher.find()) {
                final MatchResult matchResult = matcher.toMatchResult();
				matchResults.add(matchResult);
			}
		}
        return matchResults;
	}

	public static void addManaSymbols(final Context context, final LayoutInflater inflater, final LinearLayout linearLayout, final List<MatchResult> manaSymbols) {
		for (final MatchResult symbol : manaSymbols) {
			addSymbol(context, inflater, linearLayout, symbol);
		}
	}

	public static void addSymbol(final Context context, final LayoutInflater inflater, final LinearLayout linearLayout, final MatchResult symbol) {
		final View manaSymbol = inflater.inflate(R.layout.list_item_mana_symbol, null);
        final ImageView imageView = (ImageView) manaSymbol.findViewById(R.id.list_item_mana_symbol_image);
        final String filename = createFilename(symbol);
        final Drawable drawable = getDrawable(context, Folders.MANA_SYMBOLS, filename);
        imageView.setImageDrawable(drawable);
		linearLayout.addView(manaSymbol);
	}

	public static String createFilename(final MatchResult result) {
        String filename = result.group();
        filename.replace("/", "_");
        filename.replace("{", "");
        filename.replace("}", "");
        filename.toLowerCase(Locale.getDefault());
		return filename;
	}
}
