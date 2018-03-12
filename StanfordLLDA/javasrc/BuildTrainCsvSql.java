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

public class BuildTrainCsvSql {

	public static void randomSet(int min, int max, int n, HashSet<Integer> set)
	{
		if (n > (max - min + 1) || max < min)
		{
			return;
		}
		for (int i = 0; i < n; i++)
		{
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);
		}
		int setSize = set.size();  
		if (setSize < n) 
		{
			randomSet(min, max, n - setSize, set);
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RemovPunc rPunc = new RemovPunc();
		RemovStopw rStopw = new RemovStopw();
		String output1="llda/train.csv";
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		System.out.println("Let's go to database!!");
		//connection to database
		String url = "jdbc:postgresql://#";
		//property settings for user,passwords and so on
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		//select job id
		String select_sql ="select jobid from whole data";
		PreparedStatement p = conn.prepareStatement(select_sql);
		Map<String,String> ids=new HashMap<String,String>(70000); 
		int idid=0; 
		ResultSet idr = p.executeQuery();
		while(idr.next())
		{
			String idstring= idr.getString(1);
			ids.put(Integer.toString(idid),idstring);
			idid++;
		}
		HashSet<Integer> set = new HashSet<Integer>();
		randomSet(0,66303,30000,set);
		// random select 30000 records for training
		for (int j : set)
		{
			System.out.println(j);
			String select_sql1 ="select sentence,select title and description based on job id="+ids.get(Integer.toString(j));
			PreparedStatement p1 = conn.prepareStatement(select_sql1);
			ResultSet rSet1 = p1.executeQuery();
			while (rSet1.next()) 
			{
				String jobtitle= rSet1.getString(1).toLowerCase();
				String jobdes= rSet1.getString(2).toLowerCase();
				String jobtitle1 =rPunc.RemovePunc(jobtitle).replaceAll("\\s{1,}", " ");
				String jobtitle2 = rStopw.RemoveStopw(jobtitle1).replaceAll("\\s{1,}", " ");
				String jobdes1 = rPunc.RemovePunc(jobdes).replaceAll("\\s{1,}", " ");
				String jobdes2 = rStopw.RemoveStopw(jobdes1).replaceAll("\\s{1,}", " ");
				osw1.write("\""+jobtitle2+"\",\""+jobdes2+"\"\r\n");
			}
		}
		conn.close();
		osw1.close();
		
	}

}
