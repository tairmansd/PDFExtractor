package com.bats.pdf.processor.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KeyValueExtractor
{
	private List<String> lineList = new ArrayList<String>();
	private List<List<TextBlock>> lineblocks = new ArrayList<List<TextBlock>>();
	private boolean blockProcessing = false;
	
	public KeyValueExtractor(String[] lines)
	{
		this.lineList = Arrays.asList(lines);
	}
	
	public KeyValueExtractor(List<String> lines)
	{
		this.lineList = lines;
	}
	
	public KeyValueExtractor(ArrayList<List<TextBlock>> lineBlocks)
	{
		this.lineblocks = lineBlocks;
		this.blockProcessing = true;
	} 
	
	public String find(String key) {
		String result = new String();
		if(!blockProcessing) {
			result = getValueText(key);
		} else {
			result = getValueFromBlock(key);
		}
		return result;
	};
	
	private String getValueFromBlock(String key)
	{
		for (List<TextBlock> blocks : lineblocks)
		{
			for (int i = 0; i < blocks.size(); i++)
			{
				TextBlock block = blocks.get(i);
				if(block.getText().trim().equals(key.trim())) {
					try
					{
						return blocks.get(i+1).getText().trim();
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						return null;
					}
				}
			}
		}
		
		for (List<TextBlock> blocks : lineblocks)
		{
			for (int i = 0; i < blocks.size(); i++)
			{
				TextBlock block = blocks.get(i);
				if(block.getText().trim().contains(key.trim())) {
					try
					{
						return blocks.get(i+1).getText().trim();
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						return null;
					}
				}
			}
		}
		
		return null;
	}

	private String getValueText(String key) {
		
		//perform exact match
		for (String line : lineList)
		{
			String[] split = line.split(Pattern.quote("|"));
			for (int i = 0; i < split.length; i++)
			{
				if(split[i].trim().equals(key.trim())) {
					
					//if next block seperated by pipe found
					if(i+1 < split.length) {
						return split[i+1].trim();
					} else {
						//find within the string 
						String withinString = getValueWithinString(key, line);
						if(withinString != null) {
							return withinString;
						}
					}
				}
			}
		}

		//perform contains match
		for (String line : lineList)
		{
			String[] split = line.split(Pattern.quote("|"));
			for (int i = 0; i < split.length; i++)
			{
				if(split[i].trim().contains(key.trim())) {
					//if next block seperated by pipe found
					if(i+1 < split.length) {
						return split[i+1].trim();
					} else {
						//find within the string 
						String withinString = getValueWithinString(key, line);
						if(withinString != null) {
							return withinString;
						}
					}
				}
			}
		}
		return null;
	}
	
	private String getValueWithinString(String key, String line) {
		int index = line.indexOf(key);
		index = index + key.length();
		String rmLimiter = line.replaceAll("\\|", " ");
		rmLimiter = rmLimiter.replaceAll(":", " ");
		String[] words = filterEmptySpaces(rmLimiter.split("  "));
		for (int i = 0; i < words.length; i++)
		{
			if(words[i].equals(key)) {
				if(i+1 != words.length) {
					return words[i+1]; 					
				}
			}
		}
		return null;
	}
	
	private String[] filterEmptySpaces(String[] words) {
		List<String> resultlist = new ArrayList<String>();
		for (String word : words)
		{
			if(!isSpaceOrEmpty(word.trim())) {
				resultlist.add(word.trim());
			}
		}
		return resultlist.toArray(new String[0]);
	}
	
	private boolean isSpaceOrEmpty(String word) {
		return word.trim().length() < 1;
	}
	
	public Map<String,String> find(List<String> keys) {
		Map<String,String> result = new LinkedHashMap<String, String>();
		for (String key : keys)
		{
			result.put(key, find(key));
		}
		return result;
	}
	
}
