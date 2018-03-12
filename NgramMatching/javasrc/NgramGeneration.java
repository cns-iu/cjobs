/*Function name: NgramGeneration
 *Author: Shutian Ma
 *Date Written: 1.2018
 *Function Description: Generate 3grams
 */

package cjobs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class NgramGeneration {

	private static final  HashSet<String> WrongDic = new HashSet<String>(1000); 
	private static String wrongpath="Dicts/wrong.txt";
	static
	{
		try {
			List<String> lines1 = Files.readAllLines(Paths.get(wrongpath), Charset.forName("utf8"));
			for(String line1 : lines1)
			{
				line1=line1.trim();
				WrongDic.add(line1.toLowerCase());
			}
			}
		catch (IOException ex)
		{
			System.err.println("dict wrong"+ex.getMessage());
		}
	}

	public String ngrams(int n, String str) 
	{
		String ngramwords="";
		String[] words = str.trim().split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
		{
			ngramwords=ngramwords+"\t"+(concat(words, i, i+n));
		}
		return ngramwords.trim();
	}
	
	public static boolean isNumeric (String str)
	{
		for (int i = 0; i < str.length(); i++) 
		{
			if (!Character.isDigit(str.charAt(i))) 
			{
				return false;
			}
		}
		return true;
	}
	
	public String concat(String[] words, int start, int end) 
	{
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
		{
			sb.append((i > start ? " " : "") + words[i]);
		}
		String right=sb.toString();
		String [] gStrings =right.split(" ");
		Map<String,Double> wMap = new HashMap<String,Double>();
		for (int j = 0; j < gStrings.length; j++) 
		{
			wMap.put(gStrings[j], 1.0);
			if (isNumeric(gStrings[j])) 
			{
				return "";
			}
			
		}
		for (String s : WrongDic)
		{
			if (wMap.containsKey(s))
			{
				return "";
			}
		}
		return right.trim();
	}
	
	public  int wl(String text)
	{
		int wordnum=0;
		String [] words = text.split(" ");
		wordnum = words.length;
		return wordnum;
	}

	public String Write2List(String str) throws IOException, FileNotFoundException 
	{
		NgramGeneration nGeneration = new NgramGeneration();
		str = str.replaceAll("\\s{1,}", " ");
		String gram3= nGeneration.ngrams(3, str.trim());
		return gram3;
	}
}
