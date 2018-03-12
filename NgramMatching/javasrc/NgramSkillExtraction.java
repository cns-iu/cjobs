/*Function name: SkillEntityNgramMatching
 *Author: Shutian Ma
 *Date Written: 1.2018
 *Function Description: String matching based on Ngram dictionary
 *----------------------------------------------------------
 *Static
 *List -- nragmdic for ngram words storage
 *dicpath -- file path of rightngrams.txt
 */


package cjobs;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.math.BigDecimal;

public class NgramSkillExtraction
{
	// storage of ngramwords
	private static final HashSet<String> ngramdic = new HashSet<String>();
	private static String dicpath="Dicts/rightngrams.txt"; 
	static{
		try
		{
			List<String> lines = Files.readAllLines(Paths.get(dicpath), Charset.forName("utf8"));  
			for(String line : lines)
			{
				line=line.trim();
				ngramdic.add(line.toLowerCase());
			}
			System.out.println("Adding right ngrams into dictionary...");
		}
		catch (IOException ex) 
		{
			System.err.println("dict wrong"+ex.getMessage());  
		}
	}


	public static void main(String[] args) throws Exception 
	{
		NgramSkillExtraction skillenm = new NgramSkillExtraction();
		RemovPunc rPunc = new RemovPunc();
		RemovStopw rStopw = new RemovStopw();
		// output path for skills
		String output1=args[0];
		// output path for main skills
		String output2=args[1];
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		FileOutputStream fos2= new FileOutputStream(output2);
		OutputStreamWriter osw2= new OutputStreamWriter(fos2, "utf8");
		// link to database
		String url = "jdbc:postgresql://#";
		//property settings for user,passwords and so on	
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		System.out.println("database is linked");

		//select jobid title description from skills_out
		String select_sql1 ="sql select sentence to select job id title and description from table";
		PreparedStatement p1 = conn.prepareStatement(select_sql1);
		ResultSet rSet1 = p1.executeQuery();
		while (rSet1.next()) 
		{
			//text preprocess on title and description
			String jobid= rSet1.getString(1);
			String jobtitle= rSet1.getString(2).toLowerCase();
			String jobdes= rSet1.getString(3).toLowerCase();
			String jobtitle1 =rPunc.RemovePunc(jobtitle).replaceAll("\\s{1,}", " ");
			String jobtitle2 = rStopw.RemoveStopw(jobtitle1).replaceAll("\\s{1,}", " ");
			String jobdes1 = rPunc.RemovePunc(jobdes).replaceAll("\\s{1,}", " ");
			String jobdes2 = rStopw.RemoveStopw(jobdes1).replaceAll("\\s{1,}", " ");
			//map skill to store all skills
			Map<String,Double> skill=new HashMap<String,Double>();
			String t="";
			String d="";
			for (String s : ngramdic) 
			{
					if(jobtitle2.contains(s))
					{
						t=t+"\t"+s;
					}
					if(jobdes2.contains(s))
					{
						d=d+"\t"+s;
					}
			}
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
				osw1.write(jobid+"\t"+x+"\t"+t_fre+"\t"+d_fre+"\t"+total_fre+"\r\n");
				skillsmm=skillsmm+x+"\t";
			}
			skillsmm=skillsmm.trim();
			//update the ngram matching results
			String update_sql="update sentence based on job id";
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
			osw2.write(jobid+"\t"+main+"\r\n");
		}
		osw1.close();
		osw2.close();
		conn.close();
	}

}
