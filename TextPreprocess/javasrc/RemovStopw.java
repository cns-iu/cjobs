/*Function Name: RemovStopw
 *Author: Shutian Ma
 *Date Written: 1.2018
 *Fucntion Description: Remove stopwords from input string
 *------------------------------------------------------------
 *Static: 
 *dicpath--file path of stopwords list
 *stopWord-- hashset for storage of stopwords
 */



package TextPreprocessing;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemovStopw 
{
	// hashset to keep stopwords
	private static final  HashSet<String> stopWord=new HashSet<String>(300);
    	// dicpath where the stopwords file exsit
	private static String dicpath="Dicts/stopwords.txt"; 
    	static
	{  
        	try 
		{     
        		// read lines in stopwords list and add them one by one into hashset
			List<String> lines = Files.readAllLines(Paths.get(dicpath), Charset.forName("utf8"));  
        		for(String line : lines)
        		{  
            			line=line.trim();
            			stopWord.add(line);           
        		}
        	}
        	// if there is wrong print the error
		catch (IOException ex) 
        	{  
            		System.err.println("dict wrong"+ex.getMessage());  
        	}  
          
    	} 
	public String RemoveStopw(String input) throws IOException
	{
        	//split strings by whitespace
		String[] wordArray=input.split(" ");	
    		//go through each word
		for (int i=0;i<wordArray.length;i++)
    		{        		
    			//if this word is stopword then set this word to be none
			if(stopWord.contains(wordArray[i]))    			
    			{     			
    				wordArray[i]="";
    			}
    			else
    			{
    				continue;
    			}
    		}
    		//generate the output string after setting stopword words as none
		String output="";
    		for(int i=0;i<wordArray.length;i++)
    		{
    			output=output+" "+wordArray[i];  
    		}
    		output = output.trim().replaceAll("\\s{1,}", " ");
    		return output;
	}

}
