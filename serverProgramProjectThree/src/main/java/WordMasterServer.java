import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class WordMasterServer {

	int count = 1;
	private int portNumber;
	ArrayList<WordMasterClientThread> clients = new ArrayList<WordMasterClientThread>();
	private WordMasterListener server;
	private Consumer<Serializable> callback;

	WordMasterServer(Consumer<Serializable> call, int portNumber){
		callback = call;
		server = new WordMasterListener();
		this.portNumber = portNumber;
		server.start();
	}



	public class WordMasterListener extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(portNumber);){
				System.out.println("WordMasterServer is waiting for a client!");


				while(true) {
					WordMasterClientThread c = new WordMasterClientThread(mysocket.accept(), count);
					callback.accept("Client #" + count + " has connected to server");
					clients.add(c);
					c.start();
					count++;
				}
			} catch(Exception e) {
				e.printStackTrace();
				callback.accept("WordMasterServer socket did not launch");
			}
		} //end of while
	}

	class WordMasterClientThread extends Thread{

		private Socket connection;
		private int clientNumber;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private WordMasterLogic gameLogic;
		private String chosenWord = "";
		private WordMasterInfo info;

		private void parseMessage(WordMasterInfo message, int clientNumber){
			if (message.startAgain){
				callback.accept("Client #" + clientNumber + " has started playing WordMaster");
				sendMessage(new WordMasterInfo(' ', -1, null, new HashMap<>(), new ArrayList<>(), false, true, false,
						false, false));
			} else if (message.playAgain) {
				callback.accept("Client #" + clientNumber + " is playing again");
				gameLogic.fillCategory();
				gameLogic.attemptCount = 6;
				gameLogic.cat1 = false;
				gameLogic.cat2 = false;
				gameLogic.cat3 = false;

			} else if (message.chosenCategory.equals("animals")) {

				// user didn't send a letter
				if (message.letterGuessed == ' '){
					callback.accept("Client #" + clientNumber + " has chosen the animals category");

					gameLogic.attemptCount = 6;
					chosenWord = gameLogic.generateWord("animals");

					System.out.println("chosen word: " + chosenWord + " attempts: " + gameLogic.attemptCount);

					callback.accept("The chosen word from this category for client #" + clientNumber + " is: " + chosenWord);

					System.out.println("size of the current word is: " + gameLogic.currentLetters.size());

					sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "animals", gameLogic.categoryData,
							gameLogic.currentLetters,	false,
							false,
							gameLogic.cat1,
							gameLogic.cat2, gameLogic.cat3));
				}
				else {
					// user guessed a letter by clicking the guess button
					callback.accept("Client #" + clientNumber + " has tried to guess the letter: " + message.letterGuessed);

					if (gameLogic.checkGuess(message.letterGuessed)){
						System.out.println(gameLogic.currentLetters);
						callback.accept("Client #" + clientNumber + " guessed correctly!");
						callback.accept("Client #" + clientNumber + "'s current letters are now "+ gameLogic.currentLetters);
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "animals", gameLogic.categoryData,
								gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
						if(gameLogic.evaluateWord("animals")){
							System.out.println("Word is correct");
							sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "animals", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
							callback.accept("Client #" + clientNumber + " has guessed the word correctly");
						}
						else{
							sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "animals", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2,
									gameLogic.cat3));

						}

					}
					else {
						callback.accept("Client #" + clientNumber + " guessed incorrectly! " + gameLogic.attemptCount + ": attempts left");
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "animals", gameLogic.categoryData,
								gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
					}

				}

			} else if (message.chosenCategory.equals("countries")) {
				if (message.letterGuessed == ' '){
					callback.accept("Client #" + clientNumber + " has chosen the countries category");

					gameLogic.attemptCount = 6;
					chosenWord = gameLogic.generateWord("countries");

					System.out.println("chosen word: " + chosenWord + " attempts: " + gameLogic.attemptCount);

					callback.accept("The chosen word from this category for client #" + clientNumber + " is: " + chosenWord);

					System.out.println("size of the current word is: " + gameLogic.currentLetters.size());

					sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "countries", gameLogic.categoryData,
							gameLogic.currentLetters,	false,
							false,
							gameLogic.cat1,
							gameLogic.cat2, gameLogic.cat3));
				}
				else {
					// user guessed a letter by clicking the guess button
					callback.accept("Client #" + clientNumber + " has tried to guess the letter: " + message.letterGuessed);

					if (gameLogic.checkGuess(message.letterGuessed)){
						System.out.println(gameLogic.currentLetters);
						callback.accept("Client #" + clientNumber + " guessed correctly!");
						callback.accept("Client #" + clientNumber + "'s current letters are now "+ gameLogic.currentLetters);
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "countries", gameLogic.categoryData,
										gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
						if(gameLogic.evaluateWord("countries")){
							System.out.println("Word is correct");
							sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "countries", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
							callback.accept("Client #" + clientNumber + " has guessed the word correctly");
						}
						else{
							sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "countries", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));

						}
					}
					else {
						callback.accept("Client #" + clientNumber + " guessed incorrectly! " + gameLogic.attemptCount + " attempts left");
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "countries", gameLogic.categoryData,
								gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
						System.out.println(gameLogic.categoryData);
					}

				}
			} else if (message.chosenCategory.equals("food")) {
				if (message.letterGuessed == ' '){
					callback.accept("Client #" + clientNumber + " has chosen the food category");

					gameLogic.attemptCount = 6;
					chosenWord = gameLogic.generateWord("food");

					System.out.println("chosen word: " + chosenWord + " attempts: " + gameLogic.attemptCount);

					callback.accept("The chosen word from this category for client #" + clientNumber + " is: " + chosenWord);

					System.out.println("size of the current word is: " + gameLogic.currentLetters.size());

					sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "food", gameLogic.categoryData,
							gameLogic.currentLetters,	false,
							false,
							gameLogic.cat1,
							gameLogic.cat2, gameLogic.cat3));
				}
				else {
					// user guessed a letter by clicking the guess button
					callback.accept("Client #" + clientNumber + " has tried to guess the letter: " + message.letterGuessed);

					if (gameLogic.checkGuess(message.letterGuessed)){
						System.out.println(gameLogic.currentLetters);
						callback.accept("Client #" + clientNumber + " guessed correctly!");
						callback.accept("Client #" + clientNumber + "'s current letters are now "+ gameLogic.currentLetters);
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "food", gameLogic.categoryData,
								gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
						if(gameLogic.evaluateWord("food")){
							System.out.println("Word is correct");
							sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "food", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
							callback.accept("Client #" + clientNumber + " has guessed the word correctly");
						}
						else{
							sendMessage(new WordMasterInfo(' ', gameLogic.attemptCount, "food", gameLogic.categoryData,
									gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));

						}

					}
					else {
						//System.out.println("Word is wrong");
						callback.accept("Client #" + clientNumber + " guessed incorrectly! " + gameLogic.attemptCount + " attempts left");
						sendMessage(new WordMasterInfo(gameLogic.letterGuessed, gameLogic.attemptCount, "food", gameLogic.categoryData,
								gameLogic.currentLetters, false, false, gameLogic.cat1, gameLogic.cat2, gameLogic.cat3));
						System.out.println(gameLogic.categoryData);
					}

				}
			}
		}

		private void sendMessage(WordMasterInfo message){
			try {
				out.writeObject(message);
			} catch (Exception err){
				System.out.println("Failed to send a message.");
			}

		}

		WordMasterClientThread(Socket s, int count){
			this.connection = s;
			this.clientNumber = count;
			this.gameLogic = new WordMasterLogic();
			this.info = new WordMasterInfo();
		}

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			while(true) {
				try {
					WordMasterInfo info = (WordMasterInfo) in.readObject();
					parseMessage(info, clientNumber);
				}
				catch(Exception e) {
					e.printStackTrace();
					callback.accept("Client #" + clientNumber + " has disconnected");
					clients.remove(this);
					break;
				}
			}
		} //end of run


	} //end of client thread
}