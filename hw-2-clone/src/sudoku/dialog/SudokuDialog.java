/*
 * CS 3331
 * Homework 4
 * @author Anthony Ayo
 * @author Matthew Montoya
 * @author Enrique Salcido
 * @author Yoonsik Cheon
 * Purpose: To practice implementing Java Graphics
 * Last Modified: 12 April 2018
 */

package sudoku.dialog;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sudoku.dialog.BoardPanel;
import sudoku.model.Board;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * A dialog for playing simple Sudoku games.
 *
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {
	
	/** Keeps track of the number chosen. */
	private int numChoosen;

	/** Default dimension of the dialog. */
	private final static Dimension DEFAULT_SIZE = new Dimension(310, 430);

	private final static String IMAGE_DIR = "/image/";

	/** Sudoku board. */
	private Board board;

	/** Special panel to display a Sudoku board. */
	private BoardPanel boardPanel;

	/** Message bar to display various messages. */
	private JLabel msgBar = new JLabel("");
	
	/** Square size of a square on the board. */
	private int squareSize;

	/** Create a new dialog. */
	public SudokuDialog() {
		this(DEFAULT_SIZE);
	}



	/** Create a new dialog of the given screen dimension. 
	 * @param dim The dimensions of the board
	 */
	public SudokuDialog(Dimension dim) {
		super("Sudoku");
		setSize(dim);
		board = new Board(9);		//default board size
		boardPanel = new BoardPanel(board, this::boardClicked);
		configureUI();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}



	/**
	 * Callback to be invoked when a square of the board is clicked.
	 * @param x 0-based row index of the clicked square.
	 * @param y 0-based column index of the clicked square.
	 */
	private void boardClicked(int x, int y) {
		
		board.x = x;
		board.y=y;
		boardPanel.repaint();

		if(numChoosen == 0) {
			board.setEntry(x, y, numChoosen);
			boardPanel.setBoard(board);
			showMessage(String.format("You've chosen "+x+" , "+y+"."));
		}

		else if(board.validEntry(x, y, numChoosen)) {
			board.setEntry(x, y, numChoosen);
			boardPanel.setBoard(board);
			boardPanel.repaint();
			showMessage(String.format("You've chosen "+x+", "+y));

			if(board.isSolved()) {
				String winningSound = "winning-sound.wav";
				try {
					playSound(winningSound);
				} catch (Exception e) {
					e.printStackTrace();
				}
				int option = JOptionPane.showConfirmDialog(null, "Congratulations! Do you want to play a new game?", "New Game", JOptionPane.YES_NO_OPTION);

				if (option == 0) {		//The ISSUE is here
					board = new Board(board.size);
					boardPanel.setBoard(board);
					repaint();
				} else {
					showMessage("No");
				}
			}
		}else {
			showMessage("Invalid Input.");			

			String inconsistantPlacementSound = "error-sound.wav";
			try {
				playSound(inconsistantPlacementSound);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * Play sounds when incorrect, open file as InputStream && convert to AudioStream
	 * @param soundPath Source file for .wav sound
	 */
	private void playSound(String soundPath) throws Exception {
		InputStream in = new FileInputStream(soundPath);
		AudioStream audioStream = new AudioStream(in);
		AudioPlayer.player.start(audioStream);
	}



	/**
	 * Callback to be invoked when a number button is clicked.
	 * @param number Clicked number (1-9), or 0 for "X".
	 */
	private void numberClicked(int number) {
        boardPanel.repaint();
		numChoosen = number;
		showMessage("You chose " + number);
	}



	/**
	 * Callback to be invoked when a new button is clicked.
	 * If the current game is over, start a new game of the given size;
	 * otherwise, prompt the user for a confirmation and then proceed
	 * accordingly.
	 * @param size Requested puzzle size, either 4 or 9.
	 */
	private void newClicked(int size) {		//changes board size
		int option = JOptionPane.showConfirmDialog(null, "Play a new game?", "New Game", JOptionPane.YES_NO_OPTION);
		if (option == 0) { 					
			board = new Board(size);
			boardPanel.setBoard(board);
			repaint();						//we can switch now the box indexes change too
			showMessage("New game of size " + size);
		} else {
			showMessage("No");
		}
		showMessage("New game of size " + size);

	}



	/**
	 * Display the given string in the message bar.
	 * @param msg Message to be displayed.
	 */
	private void showMessage(String msg) {
		msgBar.setText(msg);
	}



	/** 
	 * Configure the UI.
	 */
	private void configureUI() {
		setIconImage(createImageIcon("sudoku.png").getImage());
		setLayout(new BorderLayout());

		JPanel buttons = makeControlPanel();
		// boarder: top, left, bottom, right
		buttons.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
		add(buttons, BorderLayout.NORTH);

		JPanel board = new JPanel();
		board.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
		board.setLayout(new GridLayout(1,1));
		board.add(boardPanel);
		add(board, BorderLayout.CENTER);

		msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
		add(msgBar, BorderLayout.SOUTH);
	}



	/** 
	 * Create a control panel consisting of new and number buttons.
	 */
	private JPanel makeControlPanel() {
		JPanel newButtons = new JPanel(new FlowLayout());
		JButton new4Button = new JButton("New (4x4)");
		for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
			button.setFocusPainted(false);
			button.addActionListener(e -> {
				newClicked(e.getSource() == new4Button ? 4 : 9);
			});
			newButtons.add(button);
		}
		newButtons.setAlignmentX(LEFT_ALIGNMENT);

		// buttons labeled 1, 2, ..., 9, and X.
		JPanel numberButtons = new JPanel(new FlowLayout());
		int maxNumber = board.size() + 1;
		for (int i = 1; i <= maxNumber; i++) {
			int number = i % maxNumber;
			JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
			button.setFocusPainted(false);
			button.setMargin(new Insets(0,2,0,2));
			button.addActionListener(e -> numberClicked(number));
			numberButtons.add(button);
		}
		numberButtons.setAlignmentX(LEFT_ALIGNMENT);

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.add(newButtons);
		content.add(numberButtons);
		return content;
	}



	/** 
	 * Create an image icon from the given image file. 
	 * @param filename Directory of the image 
	 */
	private ImageIcon createImageIcon(String filename) {
		URL imageUrl = getClass().getResource(IMAGE_DIR + filename);
		if (imageUrl != null) {
			return new ImageIcon(imageUrl);
		}
		return null;
	}


	/**
	 * Run the program
	 * @param args
	 */
	public static void main(String[] args) {
		new SudokuDialog();
	}
}