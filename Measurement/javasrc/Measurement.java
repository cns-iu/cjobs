/*Function Name:Measurement
 *Author: Shutian Ma
 *Date Writte: 1.30.2018
 *Function Title: F1 value
 *Invoking Function: PR
 *There are two invoking function in PR: precision and recall
 */
package F1Measure;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.function.DoubleToLongFunction;

import javax.jws.soap.SOAPBinding.ParameterStyle;

public class Measurement {

	public static void main(String[] args) throws Exception {
		PRmeasure prm = new PRmeasure();
		// output file for marco f1
		String output=args[0];
		String alg=args[1];
		FileWriter fWriter= new FileWriter(output,true);
		//link to database
		//basic setting here
		String url = "jdbc:postgresql://dbdev.cns.iu.edu:5432/cjobdev?";
		Properties props = new Properties();
		props.setProperty("user","#");
		props.setProperty("password","#");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);
		//select huaman labeled skills and other skills obtained by other algorithms
		//skills is not null here
		String select_sql1 ="select human labeled results and algorithm results";				
		PreparedStatement pStatement1 = conn.prepareStatement(select_sql1);	
		ResultSet rSet1 = pStatement1.executeQuery();
 		// count for how many records we select
		double fa=0.0;
		double pa=0.0;
		double ra=0.0;
 		double  c=0.0;
 		while (rSet1.next()) 
 		{
 			c++;
			String human ="";
 			String skills="";
 			human= rSet1.getString(1).toLowerCase();
 			int count= rSet1.getInt(3);

 			if(count!=0)
 			{
 				skills= rSet1.getString(2).toLowerCase();
 			}
			//calculate precision recall for each piece of record
			//
			//human data and max1 algorithm 
			double p = prm.precision(human, skills);
			double r = prm.recall(human, skills);
			double f = 0.0;
			if (p+r==0) 
			{
				continue;
			}
			else
			{
				f = 2*p*r/(p+r);
			}
			fa=fa+f;
			pa=pa+p;
			ra=ra+r;
			
 		}	

 		fWriter.write(alg+"\t"+pa/c+"\t"+ra/c+"\t"+fa/c+"\r\n");
 		fWriter.close();
		conn.close();
	}

}
	
		
		
		
		
		
	 
