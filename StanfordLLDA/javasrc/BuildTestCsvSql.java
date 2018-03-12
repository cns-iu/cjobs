package cjobs;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

public class BuildTestCsvSql {


	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RemovPunc rPunc = new RemovPunc();
		RemovStopw rStopw = new RemovStopw();
		String output1="llda/title.csv";
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		String output2="llda/des.csv";
		FileOutputStream fos2= new FileOutputStream(output2);
		OutputStreamWriter osw2= new OutputStreamWriter(fos2, "utf8");
		System.out.println("Let's go to database!!");
		//connection to database
		String url = "jdbc:postgresql://#";
		//property settings for user,passwords and so on
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		String select_sql1 ="select job id,title, description from table";
		PreparedStatement p1 = conn.prepareStatement(select_sql1);
		ResultSet rSet1 = p1.executeQuery();
		while (rSet1.next()) 
		{
				String jobid = rSet1.getString(1).toLowerCase();
				String jobtitle= rSet1.getString(2).toLowerCase();
				String jobdes= rSet1.getString(3).toLowerCase();
				String jobtitle1 =rPunc.RemovePunc(jobtitle).replaceAll("\\s{1,}", " ");
				String jobtitle2 = rStopw.RemoveStopw(jobtitle1).replaceAll("\\s{1,}", " ");
				String jobdes1 = rPunc.RemovePunc(jobdes).replaceAll("\\s{1,}", " ");
				String jobdes2 = rStopw.RemoveStopw(jobdes1).replaceAll("\\s{1,}", " ");
				osw1.write("\""+jobid+"\",\""+jobtitle2+"\"\r\n");
				osw2.write("\""+jobid+"\",\""+jobdes2+"\"\r\n");
		}
		conn.close();
		osw1.close();
		osw2.close();
	}

}
