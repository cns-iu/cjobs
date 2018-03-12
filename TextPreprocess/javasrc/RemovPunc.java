/*Function Name: RemovPunc
 *Author: Shutian Ma
 *Date Writte: 1.2018
 *Function Description: remove puncutation and invalid string characters of input string
 */




package TextPreprocessing;

import java.io.IOException;

public class RemovPunc {
	// this function is to replace punctuations
	public String RemovePunc(String a) throws IOException
	{     
		a = RemoveWrong(a).trim();
		a = a.replaceAll("[\\pP<91><92><93><94>]", " "); 
		a = a.replaceAll("\\|", " "); 
		a = a.replaceAll("\\s{1,}", " "); 
  		return a;
	}
	// this function is to remove invalid characters		
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
