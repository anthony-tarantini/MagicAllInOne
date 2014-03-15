package com.magicallinone.app.utils;

import java.util.List;
import java.util.Map;

public class DBUtils {

	private static final class Types {
		private static final String ARTIFACT = "Artifact";
		private static final String BASIC = "Basic";
	}

	public static String getCardColours(final String[] colours) {
		final StringBuilder cardColoursBuilders = new StringBuilder();
		for (final String colour : colours) {
            cardColoursBuilders.append(colour.charAt(0));
        }
		return cardColoursBuilders.toString();
	}

	public static String getType(final List<String> superTypes, final List<String> typesList) {
		final StringBuilder typesBuilder = new StringBuilder();
        final boolean isArtifact = typesList != null && typesList.contains(Types.ARTIFACT);
        final boolean isBasic = superTypes != null && superTypes.contains(Types.BASIC);
        if (isArtifact) {
			typesBuilder.append(Types.ARTIFACT + " ");
			typesList.remove(Types.ARTIFACT);
		} else if (isBasic){
			typesBuilder.append(Types.BASIC + " ");
			typesList.remove(Types.BASIC);
		}

		for (final String type : typesList) {
			typesBuilder.append(type + " ");
		}
		
		return typesBuilder.toString();
	}
	
	public static String mapToString(final Map<String, String> columns){
		final StringBuilder builder = new StringBuilder();
		for (final String column : columns.keySet()) {
            builder.append(String.format("%s %s, ", column, columns.get(column)));
        }
		builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}
}
