
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;
//proramming Assignment 1_reference_java.pdf
public class HttpRequest implements Runnable {
	
		private final static String CRLF="\r\n";
		private Socket socket;
		//Constructor
		public HttpRequest(Socket socket) throws Exception
		{
			this.socket=socket;
		}
		//run method of the runnable interface
		public void run()
		{
			try	{
				processRequest();	}
			catch(Exception e){
				System.out.println(e);	}
			
		}
		private void processRequest() throws IOException
		{
			//https://www.javatpoint.com/URL-class
			String myurl="http://localhost:"+socket.getPort()+"/";
			URL url=new URL(myurl);
			//sockets input and output streams
			java.io.InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			DataOutputStream dos=new DataOutputStream(os);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			//Getting the request line of the HTTP request message.
			String requestLine=br.readLine();
			//Displaying the request line.
			System.out.println(br);
			System.out.println(requestLine);
			//displaying socket parameters
			System.out.println("Host Name of the Client-"+socket.getInetAddress().getHostName());
			System.out.println("Protocol-"+url.getProtocol());
			System.out.println("Port Number-"+socket.getPort());
			System.out.println("Timeout-"+socket.getSoTimeout());
			//Getting and displaying header line
			String headerLine=null;
			while((headerLine=br.readLine()).length()!=0)			{
				System.out.println(headerLine);	}
			//Extracting the filename from the request line
			StringTokenizer tokens=new StringTokenizer(requestLine);
			tokens.nextToken();
			String filename=tokens.nextToken();
			//making sure the file request is in current directory
			 filename="."+filename;
			 File file =new File(filename);
			//Opening the requested file.
			FileInputStream fis=null;
			boolean fileExists=true;
			try	{
				fis=new FileInputStream(file);	}
			catch(FileNotFoundException e){
				fileExists=false;}
			//Constructing the response message.
			String statusLine=null;
			String contentTypeLine=null;
			String entityBody=null;
			//displaying status line and content type of the file if the file is exists
			if(fileExists)	{
				statusLine="HTTP/1.1 200 OK";
				contentTypeLine = "Content Type: " +"  " + contentType(filename) + CRLF;
			}else
			//if the file doesn't exists displaying message
			{
				statusLine="HTTP/1.1 404 Not Found";
				contentTypeLine="no contents to display\n";
				entityBody="<HTML>"	+ "<HEAD><TITLE>File Not Found<?TITLE></HEAD>"+"<BODY>Not Found</BODY></HTML>";			}
			//sending the status line
			dos.writeBytes(statusLine);
			//sending content type
			dos.writeBytes(contentTypeLine);
			//sending blank line to indicate the end of the header lines
			dos.writeBytes(CRLF);
			//checking if file exists and sending the content type 
			if(fileExists){
				dos.writeBytes(CRLF);
				sendBytes(fis,os);
				fis.close();
				dos.writeBytes(CRLF);
				dos.writeBytes(statusLine);
				dos.writeBytes(CRLF);
				dos.writeBytes(contentTypeLine);}
			else{
				dos.writeBytes(CRLF);
				dos.writeBytes(statusLine);
				dos.writeBytes(CRLF);
				dos.writeBytes(contentTypeLine);
				dos.writeBytes(CRLF);
				dos.writeBytes(entityBody);	
				dos.writeBytes(CRLF);}
			//Closing connections
			is.close();
			dos.close();
			socket.close();
			}
		private static void sendBytes(FileInputStream fis, OutputStream os)
				throws IOException
		{
			//Construct a 1K buffer to hold bytes on their way to the socket.
			byte[] buffer = new byte[1024];
			int bytes = 0;
			//Copying requested file into the sockets output stream.
			while((bytes = fis.read(buffer)) != -1)			{
				os.write(buffer, 0, bytes);	}
		}
		//method to get content type
		private String contentType(String fileName) {
				if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
					return "html";		}
				if(fileName.endsWith(".txt"))
					return "text";
				if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
				return "image/jpeg";		}
				if(fileName.endsWith(".gif")) {
				return "image/gif";		}
			return "application/octet-stream";

		}

	}


