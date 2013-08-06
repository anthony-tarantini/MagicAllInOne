package com.magicallinone.app.utils;

public class ImageUtils {
	private static final String IMAGE_BASE_URL = "http://magiccards.info/scans/en/";

	public static String getImageUrl(String set, int number) {
		String imageUrl = IMAGE_BASE_URL + set.toLowerCase() + "/" + number + ".jpg"; 
		return imageUrl;
	}
}
