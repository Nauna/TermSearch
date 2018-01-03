/*2017
 * Author: Annahita Doulatshahi
 * 
 * Class Description: 
 * Class reads names of .txt files in a hardcoded location folder, 
 * then reads each term in this folder (given the set up of a simple txt file of terms seperated by a newline)
 * then searches each term definition in google
 * then parses the google results into the descriptors of the top 10 web urls
 * then writes the results neatly into an output text file that has a predefined location
 * 
 * Hard Coded:
 * folderPath = location of folder with potentially multiple txt files with definitions to look up
 * path = location of folder to store results of search
 * 
 * " define" added to each google search
 * 
 * Referenced: https://stackoverflow.com/questions/39592009/parsing-google-search-result-error
 * 
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Tester {
	
	private static String delim = ":";
	private static String folderPath  = "C:\\Users\\annad\\Documents\\TermSearch";
	private static String path  = "C:\\Users\\annad\\Documents\\TermSearch\\outputs";
	
	public static void main(String[] args){	
		//Variables
		BufferedReader reader ;
		BufferedWriter outputFile;
		List<String> termFiles;
    	List<String> text = null;
    	String file = "";
    	String parsedResults ="";
    	int digit = 1; //for naming each output file
    	
    	//get all file names in folder which should be files with definitions for look-up
    	termFiles = getFiles(folderPath);
    	
    	//for each of these files, open, read, store each term into text(list), search google, save first 10 results to output file
    	for(String termDoc : termFiles) {
    		 try{
    			 //open
    			 outputFile   = new BufferedWriter( new FileWriter(path  + "\\outputTermsLEC"+digit+".txt" ) ) ;
    			 reader = new BufferedReader(new FileReader(folderPath + "\\" + termDoc)); 
    			 //read
    			 file = getText(reader);
    			 //store each term
    			 text = getTerm(file);
    			 System.out.println("Terms Searched: \n"+file);
    			 //search each term
    			 for(String txt:text){
    				 parsedResults = doASearch(txt + " define");
    				 System.out.println(txt);
    				 outputFile.write("TERM: " + txt);
    				 outputFile.write(parsedResults);
    				 outputFile.newLine();
    			 }
    			 reader.close();
    			 outputFile.close();
    			 digit++;
    		 }catch(IOException e){e.printStackTrace();}
    	 }
    	System.out.println("\n\nFinished");
	}
	
	
	private static String getText(BufferedReader reader){
    	
    	String text = "";
        String line = "";
        
        try {
			while (  ( line = reader.readLine() ) != null  ){
				text += (line+delim);
			}
		} catch (IOException e) {	e.printStackTrace();	}
        
        text = text.replace("\n", " ");
        
        return text;
    }
	private static List<String> getTerm(String txt){
		List<String> result = new ArrayList<String>();
		String end = delim;
		int endIndex= txt.indexOf(end);
		
		while(endIndex != -1){
			result.add(txt.substring(0,endIndex));
			txt = txt.substring(endIndex+end.length());
			endIndex = txt.indexOf(end);
		}
		return result;
	}
	
	private static List<String> getFiles(String folderPath) {	
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(folderPath);
        
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".txt"))) {
                textFiles.add(file.getName());
            }
        }
        return textFiles;
    }
	private static String doASearch(String search){
		String google = "http://www.google.com/search?q=";
		String charset = "UTF-8";
		String body = "";
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; /* (FULLY NOT ANNA's code, from above mentioned source)
																		Change this to your company's name and bot homepage!
																		tried without userAgent, and it does not work*/

		try{
			Document links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get();
			body = links.toString();
			body = body.replaceAll("\n","");
			return breaksubtext(body);
		}
		catch(IOException e){}
		return null;
	}
	
	private static String breaksubtext(String txt){
		String result = "";
		String start ="<span class=\"st\">";
		String end = "</span>";
		
		int indexS= txt.indexOf(start);
		int indexE= 0;
		
		while(indexS!= -1 && indexE != -1){
			txt = txt.substring(indexS+start.length());
			indexE= txt.indexOf(end);
			
			result += "\n\t" + process(txt.substring(0, indexE)) + "\n\n";
			indexS = txt.indexOf(start);
		}
		return result;
		
	}
	private static String process(String txt){
		
		String start ="<";
		String end = ">";
		
		int beginIndex= txt.indexOf(start);
		int endIndex= txt.indexOf(end);
		
		while(beginIndex!= -1 && endIndex != -1){
			if(beginIndex ==0){txt = txt.substring(endIndex+end.length());}
			else {txt = txt.substring(0,beginIndex)+txt.substring(endIndex+end.length());}
			beginIndex= txt.indexOf(start);
			endIndex= txt.indexOf(end);
			
		}
		return txt;
	}
}
	
