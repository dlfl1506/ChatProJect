package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class ChatClient extends JFrame {

	private ChatClient chatclient = this;
	public final static String TAG = "ChatClinet";
	private static final int PORT = 10000;

	public String id;
	private JButton btnConnect, btnSend;
	public JTextField tfHost, tfChat, tfprotocol;
	public JTextArea taChatList;
	private ScrollPane scrollPane;
	private JPanel topPanel, bottomPanel;

	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;
	
	public ChatClient() {
		init();
		setting();
		batch();
		listener();

		setVisible(true);
	}

	private void init() {
		btnConnect = new JButton("Connect");
		btnSend = new JButton("send");
		tfHost = new JTextField("127.0.0.1", 20);
		tfChat = new JTextField(20);
	
		taChatList = new JTextArea(10, 30); // row ,column
		scrollPane = new ScrollPane();
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		
	}

	private void setting() {
		setTitle("ä�� �ٴ�� Ŭ���̾�Ʈ");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocationRelativeTo(null); // �������� �߾ӹ�ġ
		taChatList.setBackground(Color.orange);
		taChatList.setForeground(Color.blue);
		
	}

	private void batch() {
		topPanel.add(tfHost);
		topPanel.add(btnConnect);
		
		bottomPanel.add(tfChat);
		bottomPanel.add(btnSend);

		scrollPane.add(taChatList);

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void listener() {
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				routing();

			}
		});

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String gubun[] = tfChat.getText().split(":");
				if (id ==null &&gubun[0].equals("ID")) {
					id = gubun[1];
					taChatList.append("����� ���̵�� " + id + " �Դϴ�." + "\n");
					tfChat.setText("");
					connect();
				} else {	
					send();
					
				}

			}
		});
		tfChat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String gubun[] = tfChat.getText().split(":");
					if (id ==null && gubun[0].equals("ID")) {
						id = gubun[1];
						taChatList.append("����� ���̵�� " + id + " �Դϴ�." + "\n");
						tfChat.setText("");
						connect();
					} else {	
						send();
						
					}
				}
			}
		});
	}

	

	private void send() {

		String chat = tfChat.getText();
		// 1�� taChatList �Ѹ���
		// 2�� ������ ����
	
		// 3�� tfChat ����
		taChatList.append("���޽���:" + chat + "\n");
		
		writer.println(id+"-->"+chat);
		writer.flush();
		tfChat.setText("");

		try {
	         OutputStream output = new FileOutputStream("D:\\workspace\\JavaWork\\smsProject/Output.txt");
	         String str = taChatList.getText();
	         byte[] by = str.getBytes();
	 
	         output.write(by);
	         
	      } catch (Exception e) {

	      }
		
		
		
	}

	private void routing() {
		String host = tfHost.getText();
		try {
			if (id == null) {
				taChatList.append("���� �Ϸ��� ���̵� �Է��ϼ���" + "\n" + "ex)ID:���̵�" + "\n");
				tfChat.setText("ID:");
			} else {
				socket = new Socket(host, PORT);
				writer = new PrintWriter(socket.getOutputStream());

				ReaderThread rt = new ReaderThread();
				rt.start();
			}
		} catch (Exception e1) {
			System.out.println(TAG + "���� ���� ����" + e1.getMessage());
		}

	}
	
	private void connect() {
		
		String host = tfHost.getText();
		try {
			socket = new Socket(host, PORT);
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReaderThread rt = new ReaderThread();
		rt.start();
	}

	// while �� ���鼭 �����κ��� �޽����� �޾Ƽ� taChatList�� �Ѹ���
	class ReaderThread extends Thread {
		@Override
		public void run() {
			try {
				String input = null;
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while ((input = reader.readLine()) != null) {
					taChatList.append(input + "\n");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		new ChatClient();
	}
}