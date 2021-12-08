package application;

import javafx.application.Application;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {

	@SuppressWarnings("resource")
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Text area for displaying contents
		TextArea ta = new TextArea();
		// Create a scene and place it in the stage
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));

				// Listen for a connection request
				Socket socket = serverSocket.accept();

				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					// Receive Number from the client
					int userNum = inputFromClient.readInt();

					// Check if Prime
					boolean isPrime = isPrime(userNum);

					// Send area back to the client
					outputToClient.writeBoolean(isPrime);

					Platform.runLater(() -> {
						ta.appendText("Number received from client: " + userNum + '\n');
						ta.appendText("Prime Number Check: " + isPrime + '\n');
					});
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
		
		
		
	}

	static boolean isPrime(int n) {
		// Corner cases
		if (n <= 1)
			return false;
		if (n <= 3)
			return true;

		// This is checked so that we can skip
		// middle five numbers in below loop
		if (n % 2 == 0 || n % 3 == 0)
			return false;

		for (int i = 5; i * i <= n; i = i + 6)
			if (n % i == 0 || n % (i + 2) == 0)
				return false;

		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
