package cjobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ling.CoreLabel;


public class TestData {

	public static void main(String[] args) throws Exception {
		ResultGeneration rGeneration= new ResultGeneration();		
		//input for trained model
		String serializedClassifier = args[0];
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		// output path for skills
		String output1=args[1];
		// output path for main skills
		String output2=args[2];
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

		//select jobid title description table
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
			jobtitle = jobtitle.replaceAll("[^\\w\\.\\s\\-]"," "); 
			jobtitle = jobtitle.replaceAll("\\s{1,}", " "); 
			jobdes = jobdes.replaceAll("[^\\w\\.\\s\\-]"," "); 
			jobdes = jobdes.replaceAll("\\s{1,}", " "); 		
			String ouput1=classifier.classifyToString(jobtitle,"slashTags",false);
			ouput1=ouput1.replaceAll("\n"," ");
			t=rGeneration.result(ouput1).trim();	
			String ouput2=classifier.classifyToString(jobdes,"slashTags",false);
			ouput1=ouput2.replaceAll("\n"," ");
			d=rGeneration.result(ouput2).trim();	
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
				skillsmm=skillsmm+x+"\t";
			}
			skillsmm=skillsmm.trim();
			String update_sql="update ner results based on job id";
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
