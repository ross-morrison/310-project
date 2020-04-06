package ca.ubco.cosc310;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChatbotGUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	private JFrame jframe;
	private JTextArea conversation;
	private JTextArea userInput;
	
	public boolean newMessage;
	public String nextMessage;
	
	public ChatbotMain chatbot;

	public ChatbotGUI(ChatbotMain bot) {
		
		chatbot = bot;

		// create/set up jframe
		jframe = new JFrame();
		jframe.setSize(600, 590);
		jframe.setLayout(null);
		jframe.setResizable(false);
		jframe.setTitle("DOG BOT");
		jframe.getContentPane().setBackground(Color.darkGray);

		// create/set up conversation textArea
		conversation = new JTextArea();
		// conversation.setSize(540, 450);
		conversation.setLocation(20, 20);
		conversation.setEditable(false);
		conversation.setLineWrap(true);

		// create/set up userInput textArea
		userInput = new JTextArea();
		userInput.setSize(540, 25);
		userInput.setLocation(20, 500);

		JScrollPane scroll = new JScrollPane(conversation);
		scroll.setBounds(20, 20, 540, 450);
		
		//add scrollable textArea and user input textarea to jframe
		jframe.add(scroll);
		jframe.add(userInput);

		// make jframe and panel visible
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// have userInput text field understand the enter key
		userInput.addKeyListener(this);

	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			userInput.setEditable(false);

			// store user input in string
			String input = userInput.getText();
			// reset input
			userInput.setText("");
			addUserText(input);
			synchronized (chatbot) {
				chatbot.input = input;
				chatbot.notify();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			userInput.setEditable(true);
		}
	}

	public void addText(String text) {
		conversation.setText(conversation.getText() + text + "\n");
	}
	
	public void addUserText(String text) {
		addText("YOU---> " + text);
	}
	
	public void addBotText(String text) {
		addText("BOT---> " + text);
	}
}
