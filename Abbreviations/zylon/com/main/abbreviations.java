package zylon.com.main;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SFDC2IDP.BASE.COMMON.Helper;
import edu.emory.mathcs.backport.java.util.Arrays;


public class abbreviations {
	private static String path = "http://www.abbreviations.com/abbreviation/";
	private static Pattern pattern =Pattern.compile("term/[^>]+>(\\w+)<");
	private static HashMap<String,HashSet<String>> words = new  HashMap<String,HashSet<String>> ();
public static void main(String[] args) throws Exception {
	System.out.println(Arrays.toString(getAbbrWords("name").toArray()));
}
private static  int i = 0;
public static HashSet<String> getAbbrWords(String word) {
	////term/1449919">LVL</a>
	i++;
	if(i%10==0){
		System.err.println("invoke time = "+i+" restore words = "+words.size());

	}

	word =word.trim().toLowerCase();
	HashSet<String> results = new HashSet<String>();
	  if(!hasSpecialHandle(results,word)){
		  if(words.containsKey(word)){
			   return  words.get(word);
		   }else{
			   words.put(word, results);
		   }
		   if(word.length()<3){
			   results.add(word);
		   }else{
				try {
					String html =Helper.getRespond(path+word);
				    Matcher matcher	= pattern.matcher(html);
				 
				    while (matcher.find()) {
				    	String value = matcher.group(1).trim().toLowerCase();
				    	if(!value.equalsIgnoreCase("null")&&!value.equalsIgnoreCase("")&&value.length()>1){
				    		results.add(value);
				    	}
				    		
					}
				} catch (Exception e) {
				   e.printStackTrace();
				}
		   }
	  }
	  
  if(results.size()==0){
	  results.add(word);
  }else if(results.size()==1){
	  String value = results.iterator().next();
	  if(value.equalsIgnoreCase("#null#")){
		  results.clear();
	  }
  }
  System.err.println(word + "=" + Arrays.toString(results.toArray()));
	return results;
}
private static boolean hasSpecialHandle(HashSet<String> results, String word) {
	

	String returnSpace = "the and or".trim().toLowerCase();
	returnSpace= returnSpace.trim().toLowerCase();
	for (String source : returnSpace.split("\\s+")) {
		if(word.trim().equalsIgnoreCase(source)){
			results.add("#null#");
			return true;
		}
	}
	
	String returnSource = "code rate type sales key geo Vendor Bill city price region line office group time "
			+ "person plant price unit trace qty Base Material Route Partner Name Town street Date "
			+ "class notify";
	returnSource= returnSource.trim().toLowerCase();
	for (String source : returnSource.split("\\s+")) {
		if(word.trim().equalsIgnoreCase(source)){
			results.add(source);
			return true;
		}
	}
	
	String returnEqual = "user,usr terms,term address,addr functio,func integrat,integ "
			+ "acknow,ack purchas,pur character,char exchang,xchg document,doc "
			+ "numbers,num Serial,ser information,info System,sys "
			+ "response,resp quantity,qty value,val Default,def Previous,prev "
			+ "message,msg Language,lang text,txt segment,seg "
			+ "house,hse number,no payment,pay to,2 level,lvl telephone,tel "
			+ "for,4 group,grp count,cnt Currency,curcy Priority,pri control,ctrl count,cnt "
			+ "weight,wt total,tot transaction,trans  Customer,CX Contract,contr "
			+ "Action,actn category,cat order,ord require,req required,reqd "
			+ "Length,lgth totals,tot requested,req";
	for (String matchEqual : returnEqual.split("\\s+")) {
		String []kAV = matchEqual.split(",");
		if(word.trim().equalsIgnoreCase(kAV[0])){
			results.add(kAV[1]);
			return true;
		}
	}

	String returnMatches = "organiz,org condition,cond address,addr functio,func integrat,integ "
			+ "acknow,ack purchas,pur character,char exchang,xchg Identifica,id deliver,deliv Distribut,distr "
			+ "";
	returnMatches= returnMatches.trim().toLowerCase();
	for (String matchSource : returnMatches.split("\\s+")) {
		String []kAV = matchSource.split(",");
		if(word.trim().matches(kAV[0]+"\\w+")){
			results.add(kAV[1]);
			return true;
		}
	}
	
	
	
	return false;
}
}
