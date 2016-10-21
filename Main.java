import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
	
	static final String prefix = "!", nick = "botnick", pass = "oauth:token", channel = "#channel";
	static PrintWriter out;
	static BufferedReader in;
	
	String input, user;
	
	public class InStream extends Thread
	{
		Main act = new Main();
		public void run()
		{
			try{
				while((input = in.readLine()) != null)
				{
					System.out.println(input);
					if(input.startsWith("PING"))
					{
						out.println("PONG :tmi.twitch.tv");
						System.out.println("PONG :tmi.twitch.tv");
					}else if(input.contains("PRIVMSG"))
					{
						user = input.substring(1, input.indexOf("!"));
						input = input.substring(input.indexOf(":", input.indexOf("PRIVMSG"))+1);
						act.onMessage(user, input);
					}else if(input.contains("WHISPER"))
					{
						user = input.substring(1, input.indexOf("!"));
						input = input.substring(input.indexOf(":", input.indexOf("WHISPER"))+1);
						act.onWhisper(user, input);
					}
				}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	//TODO create a queue for outgoing messages
	public void sendMessage(String msg)
	{
		out.println("PRIVMSG " + channel + " :" + msg);
	}
	
	public void sendWhisper(String user, String msg)
	{
		out.println("PRIVMSG " + channel + " :/w " + user + " " + msg);
	}
	
	public void onMessage(String sender, String msg)
	{
		if(msg.equalsIgnoreCase(prefix + "love"))
		{
			sendMessage(sender + " sends love bleedPurple bleedPurple bleedPurple");
		}else if(msg.equalsIgnoreCase(prefix + "test"))
		{
			sendMessage("This is a test command");
		}
	}
	
	public void onWhisper(String sender, String msg)
	{
		System.out.println(sender + " whispered " + msg);
	}

	public static void main(String[] args) throws Exception
	{
		String ip = "irc.chat.twitch.tv";
		int port = 6667;
		//TODO close socket on program exit
		Socket twitch = new Socket(ip, port);
		
		out = new PrintWriter(twitch.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(twitch.getInputStream()));
		new Main().new InStream().start();
		out.println("PASS " + pass);
		out.println("NICK " + nick);
		out.println("JOIN " + channel);
		out.println("CAP REQ :twitch.tv/membership");
		out.println("CAP REQ :twitch.tv/commands");
	}
}