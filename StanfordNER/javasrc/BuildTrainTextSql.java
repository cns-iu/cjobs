/*Function name: BuildTrainTextSql
 *Author: Shutian Ma
 *Date Written: 1.2018
 *Function Description: Generate training file for stanford ner
 *ouput1 is the text after labeling
 *Input is obtained from database
 */

package stanfordner;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;

public class BuildTrainTextSql {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// output path for training data
		String output1=args[0];
		//threshold for docuemnt length			
		int threshold1= Integer.parseInt(args[1]);
		//threshold for records  number
		int threshold2= Integer.parseInt(args[2]);
		System.out.println("document length is "+threshold1);
		System.out.println("Records number is "+threshold2);
		System.out.println("Let's go to database!!");
		//connection to database
		String url = "jdbc:postgresql://#";
		//property settings for user,passwords and so on	
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		//select jobid and job title from table
		String select_sql1 ="select jobid from table";						
		//file ouputwrite 
		//osw1 is output1 
		//osw2 is ouput2			
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");		
		int num=0;
		PreparedStatement pStatement1 = conn.prepareStatement(select_sql1);
		//result set 1 of jobid and jobtitle
		ResultSet rSet1 = pStatement1.executeQuery();
		while (rSet1.next()) 
		{

			if(num>threshold2)
			{
				System.out.println("you have selected "+threshold2+" records!");
				break;
			}
			else
			{
				String jobdes="";
				BigDecimal jobid =rSet1.getBigDecimal(1);
				//select job description from table jobtext based on id
				String select_sql2 ="select jobtext based on id";
				PreparedStatement p2 = conn.prepareStatement(select_sql2);
				p2.setBigDecimal(1,jobid);
				//result set 2 of job description	
				ResultSet rSet2 = p2.executeQuery();
				while (rSet2.next()) 
				{	
					jobdes= rSet2.getString(1).toLowerCase();				
				}
				if(jobdes.length()>threshold1)
				{
					//preprocess of job description
					//delete all the punctuation except dot			
					jobdes = jobdes.replaceAll("[^\\w\\.\\s\\-]"," "); 
					//replace all dot with dot+white space
					jobdes = jobdes.replaceAll("\\."," \\. "); 
					//replace all more than 2 continuous white space with only one white space
					jobdes = jobdes.replaceAll("\\s{1,}", " "); 
			
					//hashset to store skills
					HashSet<String> skillset = new HashSet<String>();
					//select job skills  based on jobid
					String select_sql3="select skill from skills based on id";
					PreparedStatement p3 = conn.prepareStatement(select_sql3);
					//result set 3 of job skills
					p3.setBigDecimal(1,jobid);	
					ResultSet rSet3 = p3.executeQuery();
					while (rSet3.next())
					{
						String skill = rSet3.getString(1).toLowerCase();
						//split skill if there contains  white space
						//put each word in skill in to skill set
						if (skill.contains(" ")) 
						{
							String [] sss=skill.split(" ");
							for (int j = 0; j < sss.length; j++) 
							{
								skillset.add(sss[j]);
							}	
						}
						//if there contains no white space
						//put skill in to skill set
						else
						{
							skillset.add(skill);
						}
					}
					String [] des=jobdes.split(" ");
					for (int i = 0; i < des.length; i++) 
					{
						String x=des[i];
						if (skillset.contains(x)) 
						{
							osw1.write(x+"\t"+"skill"+"\r\n");
						}
						else
						{
							osw1.write(x+"\t"+"O"+"\r\n");
						}
					}	
					num++;
				}
				else
				{
					continue;
				}
			}
						
		}
		conn.close();
	        osw1.close();
	}

}
