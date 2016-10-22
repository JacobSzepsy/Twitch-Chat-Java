import java.io.BufferedReader;
import java.io.PrintWriter;

public class InStream extends Thread {
	Main act = new Main();
	PrintWriter out = Main.out;
	BufferedReader in = Main.in;
	String input, user;

	public void run() {
		try {
			while ((input = in.readLine()) != null) {
				System.out.println(input);
				if (input.startsWith("PING")) {
					out.println("PONG :tmi.twitch.tv");
					System.out.println("PONG :tmi.twitch.tv");
				} else if (input.contains("PRIVMSG")) {
					user = input.substring(1, input.indexOf("!"));
					input = input.substring(input.indexOf(":", input.indexOf("PRIVMSG")) + 1);
					act.onMessage(user, input);
				} else if (input.contains("WHISPER")) {
					user = input.substring(1, input.indexOf("!"));
					input = input.substring(input.indexOf(":", input.indexOf("WHISPER")) + 1);
					act.onWhisper(user, input);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}