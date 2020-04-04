package ca.ubco.cosc310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatbotSocket {
	public Socket socket;
	
	public PrintWriter output;
	public BufferedReader input;
	
	public ChatbotSocket(String ip, int port) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		output = new PrintWriter(socket.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public String send(String msg) throws IOException {
		output.println(msg);
		return input.readLine();
	}
	
	public void close() throws IOException {
		output.close();
		input.close();
		socket.close();
	}
	
	
}
