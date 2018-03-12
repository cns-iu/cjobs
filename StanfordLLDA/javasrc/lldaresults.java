package cjobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class lldaresults {
	private static final Map<String,String> tterms=new HashMap<String,String>(); 
	private static final Map<String,String> dterms=new HashMap<String,String>(); 
    //path for top-terms.csv file
    private static String tpath="llda/lda-393afefa-30-45e19c88/title-top-terms.csv"; 
	private static String dpath="llda/lda-393afefa-30-45e19c88/des-top-terms.csv";
    static{  
        try {     
        		InputStreamReader reader1 = new InputStreamReader(new FileInputStream(tpath),"utf8");
        		BufferedReader bu1= new BufferedReader(reader1);
        		String line1=null;
        		int count1=0;
        		while((line1=bu1.readLine())!=null)
        		{
        			line1=line1.trim();
        			int ind1=line1.indexOf(",");
        			String xString1=line1.substring(ind1+1);
        			tterm.put(Integer.toString(count1), xString1);
        			count1++;
        		}
        		bu1.close();
				
				InputStreamReader reader2 = new InputStreamReader(new FileInputStream(dpath),"utf8");
        		BufferedReader bu2= new BufferedReader(reader2);
        		String line2=null;
        		int count2=0;
        		while((line2=bu2.readLine())!=null)
        		{
        			line2=line2.trim();
        			int ind2=line2.indexOf(",");
        			String xString2=line2.substring(ind2+1);
        			dterm.put(Integer.toString(count2), xString2);
        			count2++;
        		}
        		bu2.close();
				
        	} 
        catch (IOException ex) 
        	{  
            System.err.println("dict wrong: "+ex.getMessage());  
        	}  
          
    		}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		int threshold= Integer.parseInt(args[0]);
		// output path for skills
		String output1=args[1];
		// output path for main skills
		String output2=args[2];
		
		// link to database
		String url = "jdbc:postgresql://dbdev.cns.iu.edu:5432/cjobdev?";
		//property settings for user,passwords and so on	
		Properties props = new Properties();
		props.setProperty("user","mashut");
		props.setProperty("password","asq7E6C3");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		System.out.println("database is linked");
		
		
		Map<String,String> title=new HashMap<String,String>();
		Map<String,String> des=new HashMap<String,String>();

		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		
		FileOutputStream fos2= new FileOutputStream(output2);
		OutputStreamWriter osw2= new OutputStreamWriter(fos2, "utf8");		
		
		
 		InputStreamReader reader1 = new InputStreamReader(new FileInputStream("llda/lda-393afefa-30-45e19c88/title-document-topic-distributuions.csv"),"utf8");
 		BufferedReader bu1= new BufferedReader(reader1);
 		String line1;		 
 		while((line1=bu1.readLine())!=null)
 		{
 			String skills="";
 			String [] lStrings=line1.split(",");
 			String jobid=lStrings[0];
 			if (line1.contains("NaN")) 
 			{
				System.out.println("none topics");
			}
 			else 
 			{
 				HashSet<Double> pro = new HashSet<Double>();				
 				//System.out.println(lStrings.length);
 				for (int i = 1; i < lStrings.length; i++) 
 				{
 					pro.add( Double.parseDouble(lStrings[i]));
 				}
 				Object obj = Collections.max(pro);
 				int i=0;
 				for (Double s : pro) 
 				{
 					if (s.equals(obj)) 
 					{
 						break;
 					}
 					i++;
 				}
 				String terms=tterms.get(Integer.toString(i));
 				String [] tt=terms.split(",");
 				if (tt.length>threshold) 
 				{
					for (int j = 0; j < threshold; j++) 
					{
						skills=skills+"\t"+tt[j];
					}
				}
 			}
 			title.put(jobid, skills);

 			
 		}
 		bu1.close();
 		
 		
 		InputStreamReader reader2 = new InputStreamReader(new FileInputStream("llda/lda-393afefa-30-45e19c88/des-document-topic-distributuions.csv"),"utf8");
 		BufferedReader bu2= new BufferedReader(reader2);
 		String line2;		 
 		while((line2=bu2.readLine())!=null)
 		{
 			String skills="";
 			String [] lStrings=line2.split(",");
 			String jobid=lStrings[0];
 			if (line2.contains("NaN")) 
 			{
				System.out.println("none topics");
			}
 			else 
 			{
 				HashSet<Double> pro = new HashSet<Double>();				
 				//System.out.println(lStrings.length);
 				for (int i = 1; i < lStrings.length; i++) 
 				{
 					pro.add( Double.parseDouble(lStrings[i]));
 				}
 				Object obj = Collections.max(pro);
 				int i=0;
 				for (Double s : pro) 
 				{
 					if (s.equals(obj)) 
 					{
 						break;
 					}
 					i++;
 				}
 				String terms=dterms.get(Integer.toString(i));
 				String [] tt=terms.split(",");
 				if (tt.length>threshold) 
 				{
					for (int j = 0; j < threshold; j++) 
					{
						skills=skills+"\t"+tt[j];
					}
				}
 			}
 			des.put(jobid, skills); 			
 		}
 		bu2.close();
 		
 		for(Entry<String, String> entry1:title.entrySet())
		{
 			String id=entry1.getKey();
 			String t=entry1.getValue();
 			String d=des.get(id);
 			//map skill to store all skills
			Map<String,Double> skill=new HashMap<String,Double>();		
			t=t.trim();
			d=d.trim();
		 	String all=t+"\t"+d;
		 	all=all.trim();
			String [] al=all.split("\t");
			for (int i = 0; i < al.length; i++) 
			{
				if(skill.containsKey(al[i])) 
				{
					double c=skill.get(al[i]);
					skill.put(al[i], c+1.0);
				}
				else
				{
					skill.put(al[i], 1.0);
				}
			}
			//count skill frequency for title and description part
			//get max frequency
			// max frequency
			double max=1.0;
			//get all unique skills string
			String skillsmm="";
			for(Entry<String, Double> entry:skill.entrySet())
			{
				double t_fre=0.0;
				double d_fre=0.0;
				String x=entry.getKey();
				if (t.contains(x)) 
				{
					String [] tt=t.split("\t");
					for (int i = 0; i < tt.length; i++) 
					{
						if (tt[i].equals(x)) 
						{
							t_fre++;
						}
					}
				}
				if (d.contains(x)) 
				{
					String [] dd=d.split("\t");
					for (int i = 0; i < dd.length; i++) 
					{
						if (dd[i].equals(x)) 
						{
							d_fre++;
						}
					}
				}
				double total_fre=entry.getValue();
				if(total_fre>max)
				{
					max=total_fre;
				}
				osw1.write(id+"\t"+x+"\t"+t_fre+"\t"+d_fre+"\t"+total_fre+"\r\n");
				skillsmm=skillsmm+x+"\t";
			}
			skillsmm=skillsmm.trim();
			String update_sql="update skills_out set skills_llda"+threshold+"=?, count_llda"+threshold+"=? where bgtjobid="+id;
			PreparedStatement p2 = conn.prepareStatement(update_sql);
			p2.setString(1,skillsmm);
			p2.setInt(2,skill.size());
			p2.executeUpdate();
			//find the main skill
			String main="";
			HashSet<String> big = new HashSet<String>(20); 
			for(Entry<String, Double> entry:skill.entrySet())
			{
				String x=entry.getKey();
				double total_fre=entry.getValue();
				if (total_fre==max) 
				{
					big.add(x);
				}
			}
			if (big.size()==1) 
			{
				for (String s : big) 
				{
					main=s;
					break;
				}
			}
			else
			{
				String [] aStrings=all.split("\t");
				for (int i = 0; i < aStrings.length; i++) 
				{
					if (big.contains(aStrings[i])) 
					{
						main=aStrings[i];
						break;
					}
				}
			}
			osw2.write(id+"\t"+main+"\r\n");
		}

		osw1.close();
		osw2.close();
		conn.close();	
 		
	}

}
