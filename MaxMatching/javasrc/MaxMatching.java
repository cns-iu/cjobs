/*Function Name:
 * Author:
 * Date Written:
 * 
 * */


package cjobs;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class MaxMatching 
{
	private static final HashSet<String> DIC = new HashSet<String>(94000); 
	private static final Map<String,String> WrongDic=new HashMap<String,String>(1700); 
    	private static int MAX_LENGTH; 
    	//path for softwarelist.txt file
    	private static String dicpath="Dicts/softwarelist.txt";
    	static
    	{
    		try
    		{
    		System.out.println("Adding software into dictionary...");
    		System.out.println("One-word terms will not be added...");
        	MaxMatching mm= new MaxMatching();
        	int max=1;   
        	List<String> lines = Files.readAllLines(Paths.get(dicpath), Charset.forName("utf8"));  
        	for(String line : lines)
        	{
        		if (line.contains(" ")) 
        		{
            		line=line.trim();
            		DIC.add(line.toLowerCase());  
                	int wordnum = mm.wl(line);
                	if(wordnum>max)
                	{  
                		max=wordnum;  
                	}
				}
        	}  
        	System.out.println("Adding skills into dictionary...");
        	
        	String url = "jdbc:postgresql://#";
        	//property settings for user,passwords and so on	
        	Properties props = new Properties();
        	props.setProperty("user","#");
        	props.setProperty("password","#");
        	props.setProperty("ssl","false");
        	Connection conn = DriverManager.getConnection(url, props);
        	//select distinct job skills from table skills
    		String select_sql3="sql sentence to select skills";
    		PreparedStatement p1 = conn.prepareStatement(select_sql3);
    		ResultSet rSet3 = p1.executeQuery();
    		while (rSet3.next())
    		{
    			String l = rSet3.getString(1);
    			l=l.trim();
        		DIC.add(l.toLowerCase());  
            	int wordnum = mm.wl(l);
            	if(wordnum>max)
            	{  
            		max=wordnum;  
            	}
    		}	
    		conn.close();	        		
        	MAX_LENGTH = max; 
        	System.out.println("Dictionary is built!");	 
        		
        } 
        catch (Exception ex) 
        	{  
            System.err.println("Building dictionary is wrong  "+ex.getMessage());  
        	} 
    }  
	//return the word number if string text, which can be seen as word length 
	//wl is short for that
    public  int wl(String text)
    {
    	int wordnum=0;
    	String [] words = text.split(" ");
    	wordnum = words.length;
    	return wordnum;
    }
    
    
   	//get substring of string string word by word from forward direction
   	//n is the word length of string
    private  String getStr(String str, double n) 
    {
        int i = 0;
        int s = 0;
        while (i++ < n) {
            s = str.indexOf(" ", s + 1);
            if (s == -1) 
            {
                return str;
            }
        }
        return str.substring(0, s).trim();
    }

    //forward maximum string matching
    public String seg(String text) throws IOException
    {  
    		MaxMatching mms = new MaxMatching(); 	 	    	 	    	 	
    		Map<String,String> result=new HashMap<String,String>();
    	 	int len = MAX_LENGTH;
	 		int textlen = mms.wl(text);
	 		if (textlen<len) 
	 		{
	 			len = textlen;	
	 		}
	 		
    	 	loop:while(text.length()>0)
    	 	{
    	 		//System.out.println(text.length());
    	 		String tryW = mms.getStr(text.trim(), len); 
    	 		//System.out.println("now try: "+tryW);    	 		
    	 		if (DIC.contains(tryW)) 
    	 		{
    	 			//System.out.println("right:"+tryW);
    	 			result.put(tryW, "1");
    	 			text = text.substring(tryW.length()).trim();
    	 			//System.out.println("new text:"+text);
    	 			len = MAX_LENGTH;
    	 			//System.out.println("len: "+len);
    	 			continue loop;
				}
    	 		else
    	 		{
    	 			if (len==1) 
    	 			{
    	 				if (mms.wl(text)==1) 
    	 				{
							break;
						}
    	 				else
    	 				{
    	 				int x = text.indexOf(" ");
    	 				text = text.substring(x).trim();
    	 				//System.out.println("after cut:"+text);
    	 				len = MAX_LENGTH;
    	 				continue loop;
    	 				}
					}
    	 			else
    	 			{
    	 				len=len-1;
    	 			}
    	 		}
 
    	 	}
    	 	
    	 	String r="";
    	 	for(Entry<String, String> entry:result.entrySet())
    	 	{  
    	 		String x=entry.getKey();
    	 		r=r+"\t"+x;
    	 	}
    	 	return r; 
    	 	
    }  	
	
	
}
