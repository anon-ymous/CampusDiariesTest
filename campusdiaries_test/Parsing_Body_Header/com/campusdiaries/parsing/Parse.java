package com.campusdiaries.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.campusdiaries.setup.Settings;
import com.campusdiaries.setup.logClass;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class Parse {
	static Settings settings = new Settings();
	
	public String findJSONData(List<String> line) throws IOException{
		String check = "";
		String ans = "";
		final Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
		
		for(String l: line)
			if(pattern.matcher(l).find())
				check = l;

		final Matcher matcher = pattern.matcher(check);
		while(matcher.find())
		{
			System.out.println(matcher.group(1).toString());
			ans = matcher.group(1).toString();
		}
		return ans;
	}
	
	public static String nextToken(String inp,int numberOfField){
		Scanner sc = new Scanner(inp);
		String op = "";
		String atrs[] = inp.split("\\ ");
		op = atrs[numberOfField];
		sc.close();
		return op;
	}
	
	public void parseData(File file) throws IOException{
		List<String> lines = FileUtils.readLines(file);
		
		for(int i=0;i<lines.size();i++){
			if(lines.get(i).contains("HTTP/1.1")){
				String in = nextToken(lines.get(i),1);
				if(in.contains("200")){
					settings.debugLogging("Status Code: 200 - OK", "Info");
				}
				else if(Integer.parseInt(in)>=400){
					settings.debugLogging("Status Code: "+in, "Error");
					settings.debugLogging("Error at Line "+i+": Status: "+lines.get(i), "Error");
				}
			}
			if(lines.get(i).contains("WWW-Authenticate:")){
				settings.debugLogging("Error at Line "+i+": Auth Error: "+lines.get(i), "Error");
			}
			if(lines.get(i).contains("Content-Type")){
				settings.debugLogging("Info: - Content type: "+lines.get(i), "Info");
			}
		}
		String jsonData = findJSONData(lines);
		String[] str = jsonData.split("\\,");
		if(jsonData.contains("error")){
			String ser = "";
			for(int i=0;i<str.length;i++){
				if(str[i].contains("message")){
					ser = str[i];
					break;
				}
			}
			settings.debugLogging("Error in JSON reply: "+"Auth Error: "+ser, "Error");
		}
	}
	
	public static void main(String[] args) throws IOException, JSchException, SftpException, InterruptedException {
		logClass.confFile();
		RunCommandandSCPFiles runn = new RunCommandandSCPFiles();
		String host = settings.readFromFile("SSHHost");
		String uName = settings.readFromFile("SSHUserName");
		String pwd = settings.readFromFile("SSHPwd");
		String path = settings.readFromFile("PathToStoreTempFile");
		runn.scpFile(host,uName,pwd,path);
		Parse parse = new Parse();
		File file = new File(settings.readFromFile("PathToStoreTempFile")+"\\outpOfResandBody.txt");
		parse.parseData(file);

	}

}
