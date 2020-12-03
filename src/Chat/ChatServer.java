package Chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;





public class ChatServer {

	private ChatClient chatclient;
	private static final String TAG = "ChatServer:";
	private ServerSocket serversocket;
	private Vector<ClientInfo> vc; // ����� Ŭ���̾�Ʈ ������ ��� �÷���

	public ChatServer(ChatClient chatclient) {
		this.chatclient = chatclient;
	}

	public ChatServer() {
		vc = new Vector<>();

		try {

			serversocket = new ServerSocket(10000);
			System.out.println(TAG + "Ŭ���̾�Ʈ ���� �����");
			
			// ���� �������� ����
			while (true) {
				Socket socket = serversocket.accept();
				System.out.println("Ŭ������");
				ClientInfo clientinfo = new ClientInfo(socket);
				clientinfo.start();
				vc.add(clientinfo);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class ClientInfo extends Thread {

		Socket socket;
		BufferedReader reader;
		PrintWriter writer; // BufferedWrite�� �ٸ����� �������� �Լ��Ѥ� ����

		public ClientInfo(Socket socket) {
			this.socket = socket;

		}

		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				String input = null;

					while ((input = reader.readLine()) != null) {
		
						// ��ü�޼��� ������
					
						for (int i = 0; i < vc.size(); i++) {
							if (vc.get(i) != this) {
								vc.get(i).writer.println(input);
							}
						}
						
					}
			} catch (Exception e) {
				System.out.println("�������� ����" + e.getMessage());
			}

		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
