import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;



public class WordMasterClient extends Thread{

	private int serverPort;
	Socket socketClient;
	ArrayList<Character> currentLetters;

	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	
	WordMasterClient(Consumer<Serializable> call, int serverPort){
		this.serverPort = serverPort;
		callback = call;
	}

	public void sendMessage(WordMasterInfo message){
		try {
			out.writeObject(message);
		} catch (Exception err){
			System.out.println("Failed to send a message");
		}
	}
	
	public void run() {
		
		try {
			socketClient= new Socket("127.0.0.1",this.serverPort);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			System.out.println("Failed to connect");
		}

		// where the client reads information from the server
		while(true) {
			 
			try {
				WordMasterInfo info = (WordMasterInfo) in.readObject();
				callback.accept(info);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	
    }


}
