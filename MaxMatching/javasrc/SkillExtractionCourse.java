/*Function Name: SkillEaxtraction
 *Author: Shutian Ma
 *Date Written: 1.2018
 *Function Title: Skill Entity Extraction based on Forward Maximum Matching Algorithm 
 *Function Description: This is the main function for extracting skills from course which is the same with SkillExtraction
 *from texts based on forward maximum string matching
 *Invoking function: RemovPunc,RemovStopw and MaxMatching
 *RemovPunc: remove punctuation of text
 *RemovStopw: remove stop words of text
 *MaxMatching: string matching based on software list and skills list
 */

package cjobs;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.math.BigDecimal;



public class SkillExtractionCourse {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RemovPunc rPunc= new RemovPunc();
		RemovStopw rStopw =new RemovStopw();
		MaxMatching MM = new MaxMatching();
		// output path for skills
		String output1=args[0];
		// output path for main skills
		String output2=args[1];
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		FileOutputStream fos2= new FileOutputStream(output2);
		OutputStreamWriter osw2= new OutputStreamWriter(fos2, "utf8");
		//link to database
		String url = "jdbc:postgresql://dbdev.cns.iu.edu:5432/cjobdev?";
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		System.out.println("Linked to database now !");

		//select id title description from course table
		String select_sql1 ="select sentence for course data";
		PreparedStatement p1 = conn.prepareStatement(select_sql1);
		ResultSet rSet1 = p1.executeQuery();
		while (rSet1.next()) 
		{
			String des="";
			String id= rSet1.getString(1);
			String title= rSet1.getString(2).toLowerCase();
			if(rSet1.getString(3)!=null)
			{
				des= rSet1.getString(3).toLowerCase();
			}
			//text preprocess on title and descriptions
			String title1 =rPunc.RemovePunc(title).replaceAll("\\s{1,}", " ");
			String title2 = rStopw.RemoveStopw(title1).replaceAll("\\s{1,}", " ");
			String des1 = rPunc.RemovePunc(des).replaceAll("\\s{1,}", " ");
			String des2 = rStopw.RemoveStopw(des1).replaceAll("\\s{1,}", " ");
			//map skill to store all skills
			Map<String,Double> skill=new HashMap<String,Double>();
			//skill results for title
			String t=MM.seg(jobtitle2.trim()).trim();
			//skill results for description
			String d=MM.seg(jobdes2.trim()).trim();
		 	//put all skills into map skill
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
			// update course table
			String update_sql="update sentence based on course id";
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

