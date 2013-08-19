package com.magicallinone.app.utils;

import java.util.List;
import java.util.Map;

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
	
	public static String mapToString(final Map<String, String> columns){
		final StringBuilder builder = new StringBuilder();
		for (final String column : columns.keySet())
			builder.append(String.format("%s %s, ", column, columns.get(column)));
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
