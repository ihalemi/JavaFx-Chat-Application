package Task1;

import java.io.*;
import java.net.Socket;

import javafx.application.Platform;

/* 
 * Implementation for the Sever-side of the application 
 * Handles input from the client and relaying to all other connected clients 
 */

public class ClientThread implements Runnable {
	/* The socket of the client */
	private Socket clientSocket;
	/* Server class from which thread was called */
	private Server baseServer;
	private BufferedReader incomingMessageReader;
	private PrintWriter outgoingMessageWriter;
	/* The name of the client */
	private String clientName;
	
	public ClientThread(Socket clientSocket, Server baseServer) {
		this.setClientSocket(clientSocket);
		this.baseServer = baseServer;
		try {
			/*
			 * Reader to get all incoming messages that the client passes to the
			 * server
			 */
			incomingMessageReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			/* Writer to write outgoing messages from the server to the client */
			outgoingMessageWriter = new PrintWriter(
					clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			this.clientName = getClientNameFromNetwork();
			
			// Run the specified Runnable on the JavaFX Application Thread at some unspecified time in the future
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					baseServer.clientNames.add(clientName+ " - "
							+ clientSocket.getRemoteSocketAddress());
				}

			});
			String inputToServer;
			while (true) {
				inputToServer = incomingMessageReader.readLine();
				baseServer.writeToAllSockets(inputToServer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToServer(String input) {
		outgoingMessageWriter.println(input);
	}

	public String getClientNameFromNetwork() throws IOException {
		/*
		 * Get the name of the client
		 */
		return incomingMessageReader.readLine();
	}

	public String getClientName() {
		return this.clientName;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}
