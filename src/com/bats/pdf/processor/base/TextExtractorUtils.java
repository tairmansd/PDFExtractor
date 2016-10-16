package com.bats.pdf.processor.base;


public class TextExtractorUtils
{
	public static final Float VERTICAL_MERGE_THRESHOLD = 2.3F;
	public static final Float HORIZONTAL_MERGE_THRESHOLD = 500.0F;
	
	public static boolean doesLieInVerticalTolerence(Float value, Float comparedTo) {
		return (value < comparedTo + VERTICAL_MERGE_THRESHOLD) && (value > comparedTo - VERTICAL_MERGE_THRESHOLD); 
	}
	
	public static boolean doesLieInHorizontalTolerence(Float value, Float comparedTo) {
		return comparedTo - value > HORIZONTAL_MERGE_THRESHOLD; 
	}
	
	public static String getSimilarKey( Object[] keys, Float value) {
		for (Object key : keys)
		{
			String compareTo = (String) key;
			if(doesLieInVerticalTolerence(value, Float.parseFloat(compareTo))) {
				return compareTo; 
			}
		}
		return null;
	}
	
	public static String replaceLast(String string, String substring, String replacement)
	{
		int index = string.lastIndexOf(substring);
		if (index == -1)
			return string;
		return string.substring(0, index)
						+ replacement
							+ string.substring(index + substring.length());
	}
}
