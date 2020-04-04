package ca.ubco.cosc310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatbotTestServer {
	
	public ServerSocket server;
    public Socket client;
   
    public PrintWriter output;
    public BufferedReader input;
    
    public ChatbotTestServer(int port) throws IOException {
    	server = new ServerSocket(port);
    	client = server.accept();
    	input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	output = new PrintWriter(client.getOutputStream(), true);
    	readMessages();
    }
    
    public void readMessages() throws IOException {
		while(input.ready()) {
			String msg;
			msg = input.readLine();
			if(msg.equalsIgnoreCase("test")) {
		    	output.println("test2");
		    }
		}
    }
    
    public void close() throws IOException {
    	output.close();
    	input.close();
    	client.close();
    	server.close();
    }
}