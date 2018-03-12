/* Function Name: ExactM
 * Author: Shutian Ma
 * Date Written:2.16.2018
 * Function description: Do exact match to job data and generate text results
 * */
package cjobs;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExactMtext {
	//add skills to hashset Skill
	private static final HashSet<String> Skill = new HashSet<String>(3000);
	static
	{
		try
		{
			System.out.println("Adding skills into dictionary...");
			String url = "jdbc:postgresql://#";
			//property settings for user,passwords and so on	
			Properties props = new Properties();
			props.setProperty("user","#");
			props.setProperty("password","#);
			props.setProperty("ssl","false");
			Connection conn = DriverManager.getConnection(url, props);
			System.out.println("Database is connected....");
			// select distinct job skills from table skills
			// String select_sql="select distinct skill from skills";
			String select_sql="--- select distinct skill from --";
			PreparedStatement pStatement = conn.prepareStatement(select_sql);
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next())
				{
					String l = rSet.getString(1);
					l=l.trim();
					Skill.add(l.toLowerCase());
				}
			conn.close();
			System.out.println("Dictionary is built!");
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		// output path for skills
		String output1=args[0];
		// output path for main skills
		String output2=args[1];
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		FileOutputStream fos2= new FileOutputStream(output2);
		OutputStreamWriter osw2= new OutputStreamWriter(fos2, "utf8");
		// link to database
		String url = "jdbc:postgresql://dbdev.cns.iu.edu:5432/cjobdev?";
		//property settings for user,passwords and so on	
		Properties props = new Properties();
		props.setProperty("user","mashut");
		props.setProperty("password","asq7E6C3");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		System.out.println("database is linked");

		//select jobid title description from skills_out
		String select_sql1 ="select jobid,title,description from table";
		PreparedStatement p1 = conn.prepareStatement(select_sql1);
		ResultSet rSet1 = p1.executeQuery();
		while (rSet1.next()) 
		{
			String t="";
			String d="";
			String jobid= rSet1.getString(1);
			String jobtitle= rSet1.getString(2).toLowerCase();
			String jobdes= rSet1.getString(3).toLowerCase();
			//
			//Exact match is done here!!
			//
			for (String s : Skill) 
			{
				//if skill s is in jobtitle
				if (jobtitle.contains(s)) 
				{
					t=t+"\t"+s;
				}
				//if skill s is in jobdes
				if (jobdes.contains(s)) 
				{
					d=d+"\t"+s;
				}
			}
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
				osw1.write(jobid+"\t"+x+"\t"+t_fre+"\t"+d_fre+"\t"+total_fre+"\r\n");
			}
			//find the main skill
			String main="";
			HashSet<String> big = new HashSet<String>(); 
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
