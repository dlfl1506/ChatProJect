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
	private Vector<ClientInfo> vc; // 연결된 클라이언트 소켓을 담는 컬렉션

	public ChatServer(ChatClient chatclient) {
		this.chatclient = chatclient;
	}

	public ChatServer() {
		vc = new Vector<>();

		try {

			serversocket = new ServerSocket(10000);
			System.out.println(TAG + "클라이언트 연결 대기중");
			
			// 메인 쓰레드의 역할
			while (true) {
				Socket socket = serversocket.accept();
				System.out.println("클라접속");
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
		PrintWriter writer; // BufferedWrite와 다른점은 내려쓰기 함수ㅡㄹ 지원

		public ClientInfo(Socket socket) {
			this.socket = socket;

		}

		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				String input = null;

					while ((input = reader.readLine()) != null) {
		
						// 전체메세지 보내기
					
						for (int i = 0; i < vc.size(); i++) {
							if (vc.get(i) != this) {
								vc.get(i).writer.println(input);
							}
						}
						
					}
			} catch (Exception e) {
				System.out.println("서버연결 실패" + e.getMessage());
			}

		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
