package cjobs;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NgramFilter {
	
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

	public static void main(String[] args) throws IOException, IOException, Exception {
		// TODO Auto-generated method stub
		RemovPunc rPunc= new RemovPunc();
		RemovStopw rStopw =new RemovStopw();
		NgramGeneration ngramGeneration = new NgramGeneration();
		NgramFilter nf = new NgramFilter();
		// TODO Auto-generated method stub
		// output path for training data
		String output1="Dicts/rightngrams.txt";
		FileOutputStream fos1= new FileOutputStream(output1);
		OutputStreamWriter osw1= new OutputStreamWriter(fos1, "utf8");
		//threshold for records number
		int threshold1= Integer.parseInt(args[0]);
		System.out.println("Records number is "+threshold1);
		System.out.println("Let's go to database!!");
		//connection to database
		String url = "jdbc:postgresql://#";
		//property settings for user,passwords and so on
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		String select_sql ="select job id";
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
		HashSet<String> gram3 = new HashSet<String>(); 
		randomSet(0,66303,50000,set);
		// random select 50000 records
		for (int j : set)
		{
			System.out.println(j);
			String select_sql1 ="select job descriptions based on id";
			PreparedStatement p1 = conn.prepareStatement(select_sql1);
			//result set 1 of jobid and jobtitle
			ResultSet rSet1 = p1.executeQuery();
			while (rSet1.next()) 
			{
				String jobdes= rSet1.getString(1).toLowerCase();
				String x1 = rPunc.RemovePunc(jobdes).replaceAll("\\s{1,}", " ");
				x1 = rStopw.RemoveStopw(x1).replaceAll("\\s{1,}", " ");
				String a=ngramGeneration.Write2List(x1);
				String [] aa=a.split("\t");
				for (int i = 0; i < aa.length; i++)
				{
					gram3.add(aa[i]);
				}
			}
		}
		conn.close();
		System.out.println(gram3.size()+" 3-grams have been generated....");
		//rightngram
		Map<String,Double> map1 = new HashMap<String,Double>();
		//matched 3grams
		Map<String,Double> map2 = new HashMap<String,Double>();
		String coursepath="Dicts/course.txt";
		InputStreamReader read1 = new InputStreamReader(new FileInputStream(coursepath),"utf8");
		BufferedReader br1 = new BufferedReader(read1);
		String line1 = null;
		int count=1;
		while ((line1=br1.readLine())!=null)
		{
			line1 = line1.toLowerCase();
			line1 = rPunc.RemovePunc(line1).replaceAll("\\s{1,}", " ");
			line1 = rStopw.RemoveStopw(line1).replaceAll("\\s{1,}", " ");
			for (String s : gram3) 
			{
				if (line1.contains(" "+s+" "))
				{
					map2.put(s, 1.0);
				}
			}
		}
		br1.close();
		System.out.println(map2.size()+" 3-grams have been matched in course....");
		System.out.println(" Generate 2-grams from 3-grams....");
		for(Entry<String, Double> entry2:map2.entrySet())
	 	{  
	 		String x=entry2.getKey();
			String [] colums = x.split(" ");
			int size = colums.length;
			if(size==3)
			{
				String a=colums[0];
				String b=colums[1];
				String c=colums[2];
				if (c.equals(b)) 
				{
					continue;
				}
				else if (a.equals(b)) 
				{
					continue;
				}
				else if (a.equals(c)) 
				{
					continue;
				}
				else 
				{
					String w1= a+" "+b;
					String w2= b+" "+c;
					if (!b.equals("big")) 
					{
						map1.put(w1, 1.0);
					}
					if (!c.equals("big")) 
					{
						map1.put(w2, 1.0);
					}
					if (!x.endsWith("big")&&!x.startsWith("algorithms")) 
					{
						map1.put(x, 1.0);
					}
				}
			}
		}
		for(Entry<String,Double> entry3:map1.entrySet())
		{
			String x=entry3.getKey();
			osw1.write(x+"\r\n");
		}
		osw1.close();
	}

}
