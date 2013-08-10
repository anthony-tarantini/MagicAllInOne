package com.magicallinone.app.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

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
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 3,
				drawable.getIntrinsicHeight() * 3);
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
}
