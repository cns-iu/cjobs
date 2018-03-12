package cjobs;

import java.io.IOException;

public class RemovPunc {
	
	public String RemovePunc(String a) throws IOException
	{     
		a = RemoveWrong(a).trim();
		a = a.replaceAll("[\\p{Punct}]", " "); 
		a = a.replaceAll("\\s{1,}", " "); 
  		return a;
	        
	}		
	public String RemoveWrong(String input) throws IOException
	{	
    	String[] wordArray=input.split(" ");	
    	for (int i=0;i<wordArray.length;i++)
    	{     
    		String wString=wordArray[i];
    		if(wString.matches("[\\x00-\\x7F]+"))    			
    		{     			
    			continue;
    		}
    		else
    		{
    			wordArray[i]="";
    		}
    	}
    	String output="";
    	for(int i=0;i<wordArray.length;i++)
    	{
    		output=output+" "+wordArray[i];  
    	}
    	output = output.trim();
    	return output;
	}
}
