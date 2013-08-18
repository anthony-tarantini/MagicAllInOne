package com.magicallinone.app.utils;

import java.util.List;

public class DBUtils {

	private static final class Types {
		private static final String ARTIFACT = "Artifact";
		private static final String BASIC = "Basic";
	}

	public static String getCardColours(String[] colours) {
		String cardColours = "";
		for (String colour : colours)
			cardColours += colour.charAt(0);
		return cardColours.toLowerCase();
	}

	public static String getType(List<String> superTypes, List<String> typesList) {
		String types = "";
		if (typesList.contains(Types.ARTIFACT)) {
			types += Types.ARTIFACT + " ";
			typesList.remove(Types.ARTIFACT);
		} else if (superTypes.contains(Types.BASIC)){
			types += Types.BASIC + " ";
			typesList.remove(Types.BASIC);
		}
		for (String type : typesList) {
			types += type + " ";
		}
		
		return types;
	}
}
