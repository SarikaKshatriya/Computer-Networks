
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//proramming Assignment 1_reference_java.pdf
//webServer class to set connection with client
public final class WebServer {
		public static void main(String[] args) throws Exception
		{
			try
			{
			//set port number as 7777
			ServerSocket serverSocket=new ServerSocket(7777);
			System.out.println("The Server Socket is:"+serverSocket);
			while(true){
				//creating socket to establish TCP connection between client and server
				Socket socket=serverSocket.accept();
				//connection is established
				System.out.println("Connection establised with:"+socket);
				//Creating HttpRequest object to process request
				HttpRequest httpRequest=new HttpRequest(socket);
				//Creating thread to handle multiple request 
				Thread thread=new Thread(httpRequest);
				//Starting the thread
				thread.start();
				}
			}
			catch(IOException e){
				System.out.println(e);	}
		}
	}


