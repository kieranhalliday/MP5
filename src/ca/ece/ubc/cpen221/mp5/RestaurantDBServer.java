package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Abstraction Function: This class implements a server
 * 
 * Thread Safety Argument: Using blocking with calls to things like serversocket.accept()
 * The server ensures that the socket handles clients in an orderly fashion as they arrive
 * Also, none of the methods here mutate the DB or any other mutable data types
 * To further protect from race conditions, locking occurs in RestaurantDB
 *
 */
public class RestaurantDBServer implements Runnable {

	private int port;
	private ServerSocket ssocket;
	private String restaurant;
	private String user;
	private String review;

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
		this.restaurant=filename1;
		this.user=filename2;
		this.review=filename3;
		ssocket = new ServerSocket(port);

	}

	/**
	 * When the server starts executing, run will be what
	 * it calls
	 * Main purpose of run() is to call serve()
	 * Chose this implementation so that serve() could throw an
	 * IO exception which run() could not
	 */

	public void run() {
		try {
			serve();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem 1");
		}
	}
	

	/**
	 * Main activity of the server happens here
	 * @throws IOException, if there is an I/O problem during
	 *  the reading or writing or connecting
	 */
	public void serve() throws IOException {
		RestaurantDB DB = new RestaurantDB(this.restaurant,this.user,this.review);
		
		while (true) {
			// Block until a client connects
			final Socket sock = ssocket.accept();
			// Create a new thread to handle client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(sock);
						} finally {
							sock.close();
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
						System.out.println("Problem 2");
					}
				}
			});
			handler.start();
		}
	}

	/**
	 * Does the reading from the client, calls the DB
	 * and writes back to the client
	 * @param socket, which the server is running on
	 * @throws IOException
	 */
	private void handle(Socket socket) throws IOException {

		// Wrap input in a buffered reader
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Wrap output in a print writer
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		try {

			String inputLine, outputLine;

			// Initiate conversation with client
			Protocol protocol = new Protocol();

			// Takes an input line and decides what the output should be
			while ((inputLine = in.readLine()) != null) {
				outputLine = protocol.processInput(inputLine);
				out.println(outputLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(
					"Exception caught when trying to listen on port " + port + " or listening for a connection");
			System.out.println(e.getMessage());
		} finally {
			in.close();
			out.close();
		}
	}
}
