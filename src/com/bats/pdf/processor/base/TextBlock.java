package com.bats.pdf.processor.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

public class TextBlock
{
	public TextBlock(String text, List<TextPosition> positions)
	{
		this.textPosition = positions;
		this.text = text;
		
		Float[] meanarr = getMeanXY();
		
		this.xmean = meanarr[0];
		this.ymean = meanarr[1];
	}
	
	private List<TextPosition> textPosition;
	private String text = new String();
	private Float xmean = new Float(0);
	private Float ymean = new Float(0);

	public String getText()
	{
		return text;
	}
	
	public Float[] getXYforChar(String ch) {
		Float[] result = new Float[2];
		for (TextPosition ele : textPosition)
        {
			if(ele.toString().equals(ch)) {
				result[0] = ele.getXDirAdj();
				result[1] = ele.getYDirAdj();
			}
        }
		return result;
	}
	
	private Float[] getMeanXY() {
		Float sumX = new Float(0);
		Float sumY = new Float(0);
		Float[] result = new Float[2];
		for (TextPosition textPosition : textPosition)
        {
			sumX = sumX + textPosition.getXDirAdj();
			sumY = sumY + textPosition.getYDirAdj();
        }
		result[0] = sumX/textPosition.size();
		result[1] = sumY/textPosition.size();
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{	
		TextBlock block = (TextBlock) obj;
	    return this.text.equals(block.getText());
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((text == null) ? 0 : text.hashCode());
	    return result;
	}
	
	@Override
	public String toString()
	{
	    return this.text;
	}
	
	public void display() {
		String res = new String();
		res = "********" + this.text + "********\n"; 
		for (TextPosition pos : textPosition)
        {
			res = res + "[" + pos.getXDirAdj() +","+ pos.getYDirAdj() + "] " + "'" + pos.getUnicode() + "'\n"; 
        }
		res = res + "*********************************\n";
		System.out.println(res);
	}

	public boolean hasMultipleYblock()
	{
		return getDistinctBlocks().size() > 1;
	}

	public List<TextBlock> fetchDistinctBlocks()
	{
		List<String> blocks = getDistinctBlocks();
		List<TextBlock> result = new ArrayList<TextBlock>();
		for (String string : blocks)
		{
			result.add(getTextBlock(Float.parseFloat(string)));
		}
		return result;
	}

	private TextBlock getTextBlock(float parseFloat)
	{
		StringBuffer buffer = new StringBuffer();
		List<TextPosition> b = new ArrayList<TextPosition>();
		for (TextPosition textPosition : textPosition)
		{
			if(textPosition.getYDirAdj() == parseFloat) {
				buffer.append(textPosition.getUnicode());
				b.add(textPosition);
			}
		}
		return new TextBlock(buffer.toString(), b);
	}

	private List<String> getDistinctBlocks()
	{
		List<String> result = new ArrayList<String>();
		for (TextPosition textPosition : this.textPosition)
		{
			if(!result.contains(Float.valueOf(textPosition.getYDirAdj()).toString())) {
				result.add(Float.valueOf(textPosition.getYDirAdj()).toString());
			}
		}
		return result;
	}

	public Float getXmean()
	{
		return xmean;
	}

	public void setXmean(Float xmean)
	{
		this.xmean = xmean;
	}

	public Float getYmean()
	{
		return ymean;
	}

	public void setYmean(Float ymean)
	{
		this.ymean = ymean;
	}

	public static TextBlock mergeBlocks(List<TextBlock> value)
	{
		StringBuffer textStr = new StringBuffer();
		List<TextPosition> positions = new ArrayList<TextPosition>();
		
		Collections.sort(value, new Comparator<TextBlock>() {
			public int compare(TextBlock o1, TextBlock o2)
			{
				Float x11 = ((TextBlock) o1).getXmean();
				Float x21 = ((TextBlock) o2).getXmean();
				return x11.compareTo(x21);
			}
		});
		
		for (TextBlock block : value)
		{	
			textStr.append(block.getText());
			textStr.append("  |  ");
			positions.addAll(block.getTextPosition());
		}
		
		return new TextBlock(TextExtractorUtils.replaceLast(textStr.toString(), "  |  ", ""), positions);
	}

	public List<TextPosition> getTextPosition()
	{
		return textPosition;
	}

	public void setTextPosition(List<TextPosition> textPosition)
	{
		this.textPosition = textPosition;
	}
}
