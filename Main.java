import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
	static final String PREFIX = "!", NICK = "botnick", PASS = "oauth:token", CHANNEL = "#channel", IP = "irc.chat.twitch.tv";
	static final int PORT = 6667;
	
	static PrintWriter out;
	static BufferedReader in;
	static Socket twitch;
	ArrayList<String> messages = new ArrayList<String>();
	
	public class MessageQueue extends Thread {
		public void run() {
			while (!messages.isEmpty()) {
				out.println(messages.get(0));
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				messages.remove(0);
			}
		}
	}

	public void sendMessage(String msg) {
		if (messages.isEmpty()) {
			messages.add("PRIVMSG " + CHANNEL + " :" + msg);
			new MessageQueue().start();

		} else {
			messages.add("PRIVMSG " + CHANNEL + " :" + msg);
		}
	}
	
	//TODO implement separate limit for whispers
	public void sendWhisper(String user, String msg) {
		out.println("PRIVMSG " + CHANNEL + " :/w " + user + " " + msg);
	}

	public void onMessage(String sender, String msg) {
		if (msg.equalsIgnoreCase(PREFIX + "test")) {
			sendMessage("This is a test command");
		}
	}

	public void onWhisper(String sender, String msg) {
		if (msg.equalsIgnoreCase(PREFIX + "test")) {
			sendWhisper(sender, "This is a test command");
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO close socket on program exit
		twitch = new Socket(IP, PORT);
		out = new PrintWriter(twitch.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(twitch.getInputStream()));
		new InStream().start();
		out.println("PASS " + PASS);
		out.println("NICK " + NICK);
		out.println("JOIN " + CHANNEL);
		out.println("CAP REQ :twitch.tv/membership");
		out.println("CAP REQ :twitch.tv/commands");
	}
}