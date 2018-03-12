/* Function Name: ExactM
 * Author: Shutian Ma
 * Date Written:2.16.2018
 * Function description: Do exact match to job data with skills and insert back to database
 * */
package Matching;

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

public class ExactM {

	private static final HashSet<String> Skill = new HashSet<String>(3000); 
    static
    {
    	try
    	{
		// add skill into hashset Skill, here we use databese to keep these data
		// you can also read from text file
        	System.out.println("Adding skills into dictionary...");       	
        	String url = "jdbc:postgresql://#";
        	//property settings for user, passwords and so on	
        	Properties props = new Properties();
        	props.setProperty("user","#");
        	props.setProperty("password","#");
        	props.setProperty("ssl","false");
        	Connection conn = DriverManager.getConnection(url, props);
        	System.out.println("Database is connected...");
        	//select skill here if you use databese to keep skills
    		String select_sql3="sql sentence to select skills";
    		PreparedStatement p1 = conn.prepareStatement(select_sql3);
    		ResultSet rSet3 = p1.executeQuery();
    		while (rSet3.next())
    		{
    			String l = rSet3.getString(1);
    			l=l.trim();
    			Skill.add(l.toLowerCase());  
    		}	
    		conn.close();	        		
        	System.out.println("Dictionary is built!");	 
        		
        } 
        catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
	public static void main(String[] args) throws Exception {
		
		// output path for no matched jobs
		String output1=args[0];
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		
		// connect to database
		String url = "jdbc:postgresql://#";
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);		
		System.out.println("Database is connected...");		
		
		//select job id and title, here we use databese to keep these data
		String select_sql1 ="select jobid,title from table";	
		PreparedStatement pStatement1 = conn.prepareStatement(select_sql1);		
		ResultSet rSet1 = pStatement1.executeQuery();
		//count for no matched jobs
		int noncount=0;
		while (rSet1.next()) 
		{
			String jobtitle="";
			String jobdes="";
			//hashset to store title skills
			HashSet<String> title = new HashSet<String>(3000); 
			//hashset to store des skills
			HashSet<String> des = new HashSet<String>(3000); 
			
			BigDecimal jobid =rSet1.getBigDecimal(1);
			String id=rSet1.getString(1);
			
			jobtitle= rSet1.getString(2).toLowerCase();
			//select jobtext based on job id
			String select_sql2 ="sql sentence to select job description based on job id";
			PreparedStatement pStatement2 = conn.prepareStatement(select_sql2);
			ResultSet rSet2 = pStatement2.executeQuery();
			while (rSet2.next()) 
			{
				jobdes= rSet2.getString(1).toLowerCase();				
			}
			for (String s : Skill) 
			{
				//if skill s is in jobtitle
			    if (jobtitle.contains(s)) 
			    {
			    	title.add(s);
				}
			    //if skill s is in jobdes
			    if (jobdes.contains(s)) 
			    {
					des.add(s);
				}
			}
			String tskill="";
			String dskill="";
			if (title.size()>0) 
			{
				for (String ts : title) 
				{
					tskill=tskill+ts+"\t";
				}
			}
			if (des.size()>0) 
			{
				for (String ds : des) 
				{
					dskill=dskill+ds+"\t";
				}
			}
			tskill=tskill.trim();
			dskill=dskill.trim();
			int tcount=title.size();
			int dcount=des.size();
			// check if there is already results for the job id
			String se_sql="sql sentence to check jobid records";
			PreparedStatement pp = conn.prepareStatement(se_sql);
			pp.setBigDecimal(1,jobid);
			ResultSet rs = pp.executeQuery();
			if(rs.next())
			{
				//Attention for column names
				String update_sql="update sql sentence based on job id";
				PreparedStatement p2 = conn.prepareStatement(update_sql);
				p2.setString(#,##);
				p2.setInt(#,##);
				p2.execute();
			}
			else
			{

				String insert_sql="insert sql sentence";		
				PreparedStatement p1 = conn.prepareStatement(insert_sql);
				p1.setString(#,##);
				p1.setInt(#,##);
				p1.execute();
		
			}
			
			int total=tcount+dcount;
			// keep records if there is no exact matched skills
			if (total==0) 
			{
				noncount++;
				osw1.write(id+"\t"+jobtitle+"\t"+jobdes+"\r\n");
			}
			
		}
		System.out.println("There are "+noncount+" records not having exact matched skills!");
		conn.close();
		osw1.close();
	}

}
