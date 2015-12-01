package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// TODO: Implement a server that will instantiate a database, 
// process queries concurrently, etc.
// Copy threaded server into here, it will solve the commented problem I had

public class RestaurantDBServer implements Runnable {

	private int port;
	@SuppressWarnings("unused")
	private String filename1;
	@SuppressWarnings("unused")
	private String filename2;
	@SuppressWarnings("unused")
	private String filename3;

	/**
	 * Constructor
	 * 
	 * @param port
	 * @param filename1
	 * @param filename2
	 * @param filename3
	 * @throws IOException
	 */
	public RestaurantDBServer(int port, String filename1, String filename2, String filename3) throws IOException {
		this.port = port;
		this.filename1 = filename1;
		this.filename2 = filename2;
		this.filename3 = filename3;

	}

	/**
	 * This method starts the socket and server It is what runs while the server
	 * is running
	 */

	public void run() {

		ServerSocket ssocket;
		try {
			ssocket = new ServerSocket(port);

			while (true) {
				Socket sock = ssocket.accept();
				new Thread(new RestaurantDBServer(port, "restaurant.json", "reviews.json", "users.json")).start();

				// Prints output stream
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				// Reads new input
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				String inputLine, outputLine;

				// Initiate conversation with client
				Protocol protocol = new Protocol();

				// Takes an input line and decides what the output should be
				while ((inputLine = in.readLine()) != null) {
					outputLine = protocol.processInput(inputLine);
					out.println(outputLine);
				}
				ssocket.close();

			}
		}

		catch (IOException e) {
			e.printStackTrace();
			System.out.println(
					"Exception caught when trying to listen on port " + port + " or listening for a connection");
			System.out.println(e.getMessage());
		}

	}

}
