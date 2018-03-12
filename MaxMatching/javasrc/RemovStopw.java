package cjobs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemovStopw {
	
	private static final  Set<String> stopWord=new HashSet<String>();
    private static String dicpath="Dicts/stopwords.txt"; 
    static{  
        try {     
        		List<String> lines = Files.readAllLines(Paths.get(dicpath), Charset.forName("utf8"));  
        		for(String line : lines)
        		{  
            		line=line.trim();
            		stopWord.add(line);           
        		}
        }
        catch (IOException ex) 
        	{  
            System.err.println("dict wrong"+ex.getMessage());  
        	}  
          
    } 
	public String RemoveStopw(String input) throws IOException
	{
        
    	String[] wordArray=input.split(" ");	
    	for (int i=0;i<wordArray.length;i++)
    	{        		
    		if(stopWord.contains(wordArray[i]))    			
    		{     			
    			wordArray[i]="";
    		}
    		else
    		{
    			continue;
    		}
    	}
    	String output="";
    	for(int i=0;i<wordArray.length;i++)
    	{
    		output=output+" "+wordArray[i];  
    	}
    	output = output.trim().replaceAll("\\s{1,}", " ");
    	return output;
	}

}
