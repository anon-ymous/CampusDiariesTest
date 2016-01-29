package com.campusdiaries.parsing;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.testng.annotations.BeforeClass;

import com.campusdiaries.setup.Settings;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
public class RunCommandandSCPFiles {
	static Settings settings = new Settings();
	@SuppressWarnings("unused")
	public void scpFile(String host,String username,String pass,String tempPath) throws JSchException, IOException, SftpException, InterruptedException{
	
		String strToscp = "";
		String command = " curl -i https://graph.facebook.com/me -H 'Authorization: Bearer "+settings.readFromFile("OAuthToken")+"' > outpOfResandBody.txt";

		JSch jsch = null;
		Session session = null;
		Session sessionE = null;
		Channel channelold = null;
		Channel forSFTP = null;
		ChannelSftp c = null;

		jsch = new JSch();
		session = jsch.getSession(username, host, 22);
		session.setPassword(pass);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		channelold = session.openChannel("exec");
		((ChannelExec)channelold).setCommand(command);
		channelold.setInputStream(null);
		((ChannelExec)channelold).setErrStream(System.err);

		InputStream input = channelold.getInputStream();
		channelold.connect();

		System.out.println("Channel Connected to machine " + host + " server with command: " + command ); 

		try{
			InputStreamReader inputReader = new InputStreamReader(input);
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			String line = null;

			while((line = bufferedReader.readLine()) != null){
				//strToscp = line;
				//System.out.println(line);
			}
			bufferedReader.close();
			inputReader.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}

		Thread.sleep(5000);
		forSFTP = session.openChannel("sftp");
		forSFTP.connect();
		c = (ChannelSftp) forSFTP;
		strToscp = "/home/"+username+"/outpOfResandBody.txt";
		c.get(strToscp,tempPath);
		settings.debugLogging("Download completed "+strToscp+" to: "+tempPath,"Info");

		session.disconnect();
	}

	public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
	
		RunCommandandSCPFiles s = new RunCommandandSCPFiles();
		String host = settings.readFromFile("SSHHost");
		String uName = settings.readFromFile("SSHUserName");
		String pwd = settings.readFromFile("SSHPwd");
		String path = settings.readFromFile("PathToStoreTempFile");
		s.scpFile(host,uName,pwd,path);

	}

}
