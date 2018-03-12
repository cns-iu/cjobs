/*Function Name: PRmeasure
 *Author:Shutian Ma
 *Date Written: 1.30.2018
 *Function description: calculate precision and recall between two string
 * human is golden data from human, alg is obtained data from algorithms
 */
package F1Measure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class PRmeasure
{	
	
	// calculate precision
	 public  double precision(String human, String alg)
	 {
		 	HashSet<String> gset = new HashSet<String>();
			HashSet<String> rset = new HashSet<String>();		 			 	
		 	if (human.contains(";")) 
		 	{
		 		String [] gstring= human.split(";");
		 		for(int i1=0;i1<gstring.length;i1++)
				{
					gset.add(gstring[i1]);				
				}	
			}
		 	else
		 	{
		 		gset.add(human);
		 	}	 	
		 	
		 	if (alg.contains(";")) 
		 	{
		 		String [] rstring= alg.split(";");
		 		for(int i1=0;i1<rstring.length;i1++)
				{
					rset.add(rstring[i1]);
				}
			}
		 	else
		 	{
		 		rset.add(alg);
		 	}

									
			int a=0;
			for (String s : gset) 
			{
			    if (rset.contains(s)) 
			    {
					a++;
				}
			}
				
			//if rmap has nothing then precision is 0
			double precision = 0.0;
			if (rset.size()==0) 
			{
				precision = 0.0;
			}
			else
			{						
				precision = (double)a/(double)rset.size();
			}
			return precision;
	 }
	 // calculate recall
	 public  double recall(String human, String alg)
	 {
			// gstring1 is to store human skills
			// rstring1 is to store algorithm skills
		 	HashSet<String> gset1 = new HashSet<String>();
			HashSet<String> rset1 = new HashSet<String>();
		 			 	
		 	if (human.contains(";")) 
		 	{
		 		String [] gstring1= human.split(";");
		 		for(int i1=0;i1<gstring1.length;i1++)
				{
					gset1.add(gstring1[i1]);				
				}	
			}
		 	else
		 	{
		 		gset1.add(human);
		 	}	 	
		 	
		 	if (alg.contains(";")) 
		 	{
		 		String [] rstring1= alg.split(";");
		 		for(int i1=0;i1<rstring1.length;i1++)
				{
					rset1.add(rstring1[i1]);
				}
			}
		 	else
		 	{
		 		rset1.add(alg);
		 	}
			
			
			
			
			
			
			int a1=0;
			for (String s1 : gset1) 
			{
			    if (rset1.contains(s1)) 
			    {
					a1++;
				}
			}
			//if gset has nothing
			//then recall is 0
			double recall = 0.0;
			if (gset1.size()==0) 
			{
				recall = 0.0;
			}
			else
			{					
				recall = (double)a1/(double)gset1.size();
			}
			return recall;
			
	 }
}
