package Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import Task1.ClientThread;

/* This class acts as the server for all the chat clients */

public class Server implements Runnable {
	private int portNumber;
	private ServerSocket socket;
	private ArrayList<Socket> clients;
	private ArrayList<ClientThread> clientThreads;
	public ObservableList<String> serverLog;
	public ObservableList<String> clientNames;
	
	public Server(int portNumber) throws IOException {
		this.portNumber = portNumber;
		serverLog = FXCollections.observableArrayList();
		clientNames = FXCollections.observableArrayList();
		clients = new ArrayList<Socket>();
		clientThreads = new ArrayList<ClientThread>();
		socket = new ServerSocket(portNumber);
		
	}

	public void startServer() {

		try {
			socket = new ServerSocket(this.portNumber); 
			
			
			serverLog = FXCollections.observableArrayList();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			/* Infinite loop to accept any incoming connection requests */
			while (true) {
				
				final Socket clientSocket = socket.accept();
				
				/* Add the incoming socket connection to the list of clients */
				clients.add(clientSocket);
				/* Add to log that a client connected */
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						serverLog.add("Connection from " +clientSocket);
					}
				});
				
				ClientThread clientThreadHolderClass = new ClientThread(clientSocket, this);
				Thread clientThread = new Thread(clientThreadHolderClass);
				clientThreads.add(clientThreadHolderClass);
				clientThread.setDaemon(true);
				clientThread.start();
				
				ServerApplication.threads.add(clientThread);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeToAllSockets(String input) {
		for (ClientThread clientThread : clientThreads) {
			clientThread.writeToServer(input);
			
		}
	}

}
