package com.bats.pdf.processor.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PDFTextExtractor
{
	
	public PDFTextExtractor()
	{
		
	}
	
	public static void main(String[] args) throws IOException 
    {
		//File file = new File("D:/Invice_Formats/Line 07 - Attorney Fee-Approved Additional.pdf");
		InputStream in = PDFTextExtractor.class.getClassLoader().getResourceAsStream("Line 01 & 05 & 06 -  Attorney.pdf");
		PDFTextProcessor processor = new PDFTextProcessor(in);
		String[] data = processor.processText();
		KeyValueExtractor kv = new KeyValueExtractor(data);
		List<String> keys = new ArrayList<String>();
		keys.add("Phone No:");
		keys.add("Fax No");
		keys.add("Invoice #");
		keys.add("Invoice Status");
		keys.add("Input By");
		keys.add("Date Submitted");
		keys.add("Re");
		keys.add("Invoice Date");
		keys.add("Vendor Ref #");
		keys.add("Loan #");
		keys.add("Vendor Code");
		keys.add("Loan Type");
		keys.add("Payee Code");
		keys.add("Inv. ID / Cat. ID");
		keys.add("Type:");
		keys.add("Cost Center");
		keys.add("Referral Date");
		keys.add("CONV-FN Case No");
		keys.add("GSE Code");
		keys.add("Acquisition Date");
		keys.add("GSE REO Rem. Code");
		keys.add("Paid in Full Date");
		keys.add("Entity Code");
		keys.add("Foreclosure Removal Date");
		keys.add("MS Status");
		keys.add("Relief Requested Date");
		keys.add("Protection Begin Date");
		keys.add("Protection End Date");
		keys.add("First Legal Action");
		keys.add("Legal Action");
		keys.add("Invoice ID");
		keys.add("Asset Number");
		keys.add("Outsourcer");
		System.out.println(kv.find(keys));
    }
}

/*results
 * {GSE REO Rem. Code=null,
CONV-FN Case No=null,
Invoice Status=Check Requested(Exc),
Paid in Full Date=N/A,
Invoice Date=5/4/2016,
Fax No=(504) 588-2888,
Cost Center=Referral Date,
Relief Requested Date=N/A,
Input By=Jaune Monnerjahn,
Date Submitted=5/4/2016,
GSE Code=null,
Vendor Ref #=15-35994,
Phone No:=null,
Protection End Date=N/A,
Payee Code=0060600113,
Acquisition Date=null,
Legal Action=12/15/2015,
Loan Type=FannieMae,
Invoice ID=179816582,
Re=DEITCH THEODORE,
MS Status=N/A,
Vendor Code=JACMCP,
Protection Begin Date=N/A,
Entity Code=null,
Foreclosure Removal Date=N/A,
Outsourcer=LPS Default Solutions,
First Legal Action=N/A,
Referral Date=9/16/2015,
Inv. ID / Cat. ID=644  /  28835,
Invoice #=107571,
Asset Number=null,
Loan #=68575147, 
Type:=Judicial}*/
