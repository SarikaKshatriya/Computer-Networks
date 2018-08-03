
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
//http://dumbitdude.com/socket-programming-in-java/
public class Client {
	
	public static void main(String args[]) 
	{
		try{
		//set up the port number
		//to take input number from user or to set port number dynamically
		//int port = Integer.parseInt(args[0]);
		//https://www.javatpoint.com/URL-class
		URL url=new URL("http://localhost");
		//connecting to the server
		Socket myClient=new Socket("localhost",7979);
		System.out.println("Connected to the Server from client");
		//displaying connection parameters
		System.out.println("Connection Parameters:");
		//displaying the host name
		System.out.println("Host Name of the Server-"+myClient.getInetAddress().getHostName());
		System.out.println("Protocol-"+url.getProtocol());
		System.out.println("Port Number-"+myClient.getPort());
		//displaying the timeout
		System.out.println("Timeout-"+myClient.getSoTimeout());
		//getting reference to the sockets input and output stream
		DataInputStream input=new DataInputStream(myClient.getInputStream());
		DataOutputStream output=new DataOutputStream(myClient.getOutputStream());
		//reading data from end user
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the file name");
		String filename=br.readLine().trim();
		//calculating RTT
			long startTime,endTime,totalTime;
			startTime = System.currentTimeMillis();
			output.writeUTF(filename);
		    String response=input.readUTF();
		    System.out.println("Server:"+response);
			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("RTT(in milliseconds) for request is:"+totalTime);
			//client server communication
			
			String serverInput="";
			//https://www.javaworld.com/article/2853780/core-java/socket-programming-for-scalable-systems.html
		    while(true) {
				
				System.out.println("Message from server is:"+serverInput);
		    	System.out.println("To close communication, type close");
		    	System.out.println("Type message for server.To close communication, type close");
		    	serverInput=br.readLine().trim();
		     	if(serverInput.equalsIgnoreCase("close")){
		     		output.writeUTF(serverInput);
		    		break;
		    	}else {
		    		output.writeUTF(serverInput);
		    		System.out.println("Server Message:"+input.readUTF());}
		      }//while
		    //closing connections
		    System.out.println("closing the connections");
		    input.close();
		    output.close();
		    br.close();
		    myClient.close();
		    
		} catch (IOException e) {
			e.printStackTrace();}
		
	}
}
