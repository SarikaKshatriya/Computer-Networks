
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private Socket socket;
	//server constructor
	public Server(Socket socket)	{
		this.socket=socket;	}

	public static void main(String args[]) 
	{	try	{
		//set up the port number
		//to take input number from user or to set port number dynamically
		//int port = Integer.parseInt(args[0]);
		System.out.println("Connecting to server port");
		ServerSocket myServer=new ServerSocket(7979);
		System.out.println("This is a Server Socket");
		//Server requirement is always on as per client-server architecture
		while(true)		{
			//Listening for a TCP connection request.
			Socket socket=myServer.accept();
			System.out.println("Connection is established---"+socket);
			//getting and displaying socket details
			System.out.println("Server Socket Information:");
			System.out.println("Port Number: "+socket.getPort());
			System.out.println("Localport:"+socket.getLocalPort());
			System.out.println("Host: "+socket.getInetAddress().getHostName());
			System.out.println("Timeout-"+socket.getSoTimeout());
			//creating a new thread to process the request
			Thread thread=new Thread(new Server(socket));
			//starting the thread
			thread.start();
			}
		}
		catch(IOException e)		{
			e.printStackTrace();}
			
	}
	@Override
	public void run() {
		try
		{			
			//https://www.javaworld.com/article/2077322/core-java/core-java-sockets-programming-in-java-a-tutorial.html
			//get reference to the sockets input and output stream
			DataInputStream input=new DataInputStream(socket.getInputStream());
			DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
			//get filename
			String filename="./"+input.readUTF();
			boolean fileExist=true;
			BufferedReader br=null;
			BufferedReader br_in=new BufferedReader(new InputStreamReader(System.in));
			//reading file data
			try			{
			 br=new BufferedReader(new FileReader(new File(filename)));			}
			catch(Exception e){
				fileExist=false;			}
			if(fileExist){
			String line="",output="";
			output=output+"File exist: status 200";
			//read data until we finish the end line
			while((line=br.readLine())!=null){
				output=output+line+"\r\n";}
			dos.writeUTF(output);
			}else{
				dos.writeUTF(" 404 file not exist");}
			//message transfer between client and server
			String clientInput="";
			while(true){
				clientInput=input.readUTF();
				System.out.println("Message from client is:"+clientInput);
		    	System.out.println("To close communication, type close");
		    	System.out.println("Type message for server.To close communication, type close");
				if(clientInput.equalsIgnoreCase("close")){
					System.out.println("Closing the communication");
					break;}
				else{
					System.out.println("Enter the message to client");
					dos.writeUTF(br_in.readLine().trim());}
			}
			//closing connections
			System.out.println("closing the connections");
			input.close();
			dos.close();
			if(null !=br){
				br.close();}
			socket.close();
			}
		catch(Exception e){
			e.printStackTrace();}
		
	}
}



