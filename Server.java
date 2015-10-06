import java.net.*;
import java.util.*;
import java.io.*;

public class Server extends Thread
{
	private Map<String,String> map;

	public Server(Map<String,String> map)
	{
		this.map = map;
	}

	public void run()
	{
		ServerSocket server = null;
		try
		{
			server = new ServerSocket(1337);
			System.out.println("listening");
			while(true)
			{
				Socket client = null;
				BufferedReader in = null;
				PrintWriter out = null;
				try
				{
					client = server.accept();
					System.out.println("new connection");
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
					String str;
					while((str = in.readLine()) != null && !"quit".equals(str))
					{
						System.out.println(String.format("request (%s)",str));
						String response = this.map.get(str);
						if(response == null)
						{
							response = "Bezirk nicht gefunden";
						}
						System.out.println(String.format("response (%s)",response));
						out.println(response);
						out.flush();
					}
					System.out.println("quit");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					try
					{
						out.close();
						in.close();
						client.close();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				server.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void main(String... args)
	{
		RandomAccessFile file = null;
		try
		{
			List<String> list = new LinkedList<String>();
			file = new RandomAccessFile("input.csv","r");

			String s;
			while((s = file.readLine()) != null)
			{
				list.add(s);
			}

			Map<String,String> map = new HashMap<String,String>();
			for(String str:list)
			{
				String[] strs = str.split(";");
				map.put(strs[1],str);
			}
			new Server(map).start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				file.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}

