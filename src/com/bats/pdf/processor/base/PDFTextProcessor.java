package com.bats.pdf.processor.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PDFTextProcessor extends PDFTextStripper
{
	private List<TextBlock> blockList = new ArrayList<TextBlock>(); 
	
	/**
     * Instantiate a new PDFTextStripper object.
     *
     * @throws IOException If there is an error loading the properties.
     */
    public PDFTextProcessor(File file) throws IOException
    {
    	PDDocument document = null;
        try
        {
            document = PDDocument.load( file );

            this.setSortByPosition( true );
            this.setStartPage( 0 );
            this.setEndPage( document.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            this.writeText(document, dummy);
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
    }
    
    public PDFTextProcessor(InputStream stream) throws IOException
    {
    	PDDocument document = null;
        try
        {
            document = PDDocument.load( stream );

            this.setSortByPosition( true );
            this.setStartPage( 0 );
            this.setEndPage( 1 );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            this.writeText(document, dummy);
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
    }
    
    /**
     * Override the default functionality of PDFTextStripper.
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException
    {
    	//System.out.println(string);
    	TextBlock block = new TextBlock(string, textPositions);
    	if(block.hasMultipleYblock()) {
    		List<TextBlock> blocks = block.fetchDistinctBlocks();
    		blockList.addAll(blocks);
    	} else {
    		blockList.add(block);    		
    	}
    }
    
    public String[] processText() {
    	//applying sort y-axis and then x-axis
    	//sort comparision print();
    	//print();
    	Collections.sort(blockList, new Comparator<TextBlock>() {
			public int compare(TextBlock o1, TextBlock o2)
            {
				//[1] for y axis sort
				Float x1 = ((TextBlock) o1).getYmean();
				Float x2 = ((TextBlock) o2).getYmean();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } else {
					Float x11 = ((TextBlock) o1).getXmean();
					Float x21 = ((TextBlock) o2).getXmean();
	               return x11.compareTo(x21);
	            }
            }
		});
    	//print();
    	
    	//check for group for Y-axis merge
    	blockList = mergeGroups();
    	
    	String[] result = new String[blockList.size()];
    	
    	for (int i = 0; i < blockList.size(); i++)
		{
    		result[i] = blockList.get(i).getText();
		}
    	
    	return result;
    }
    
    public void print() {
    	for (TextBlock list : blockList)
		{
			list.display();
		}
    }

	private List<TextBlock> mergeGroups()
    {
		Map<String, List<TextBlock>> blockGroup = new LinkedHashMap<String, List<TextBlock>>();
		List<TextBlock> result = new ArrayList<TextBlock>();
		for (TextBlock block : blockList)
		{
			String key = block.getYmean().toString();
			if(blockGroup.containsKey(key)) {
				blockGroup.get(key).add(block);
			} else {
				//Y-axis
				String existingKey = TextExtractorUtils.getSimilarKey(blockGroup.keySet().toArray(), block.getYmean());
				if(existingKey != null) {
					blockGroup.get(existingKey).add(block);
				} else {
					List<TextBlock> res = new ArrayList<TextBlock>();
					res.add(block);
					blockGroup.put(block.getYmean().toString(), res);
				}
			}
		}
		
		//each entry in this map represents a line in the pdf
		//System.out.println(blockGroup);
		
		for (Entry<String, List<TextBlock>> entry : blockGroup.entrySet())
		{
			result.add(TextBlock.mergeBlocks(entry.getValue()));
		}
		
	    return result;
    }

	public List<TextBlock> getBlockList()
    {
	    return blockList;
    }
    
}