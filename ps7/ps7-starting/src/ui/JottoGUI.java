package ui;

import model.JottoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * // TODO Write specifications for your JottoGUI class.
 */
public class JottoGUI extends JFrame implements ActionListener {

	private final static int PORT = 4444;
	private ServerSocket serverSocket;


	private JButton newPuzzleButton;
	private JTextField newPuzzleNumber;
	private JLabel puzzleNumber;
	private int puzzleNumberValue;
	private JTextField guess;
	private JLabel guessLabel;
	private JottoTable guessTable;

	// accessor method for puzzleNumberValue
	public int getPuzzleNumber() {return puzzleNumberValue;}
	// update method for puzzleNumber label
	public void updatePuzzleNumber(JLabel label) {
		label.setText("Puzzle #" + puzzleNumberValue);
	}


	public void makeGuess(int puzzleNumberValue, String guessText) {

		// row to insert guess into
		int row = guessTable.getRowCount() - 1;

		JottoModel jotto = new JottoModel(Integer.toString(puzzleNumberValue));

		try {
			String response = jotto.makeGuess(guessText);
			String[] responseArgs = response.split("\\s+");
			String matchesTotal = responseArgs[1];
			String positionMatches = responseArgs[2];
			if (Integer.parseInt(matchesTotal) == 5 && Integer.parseInt(positionMatches) == 5) {
				// System.out.println("You win!. The secret word was " + guessText);
				guessTable.insertResults("Correct! You win.", "", row);
			} else {
				// System.out.println(response);
				guessTable.insertResults(matchesTotal, positionMatches, row);
			}
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	public class GuessThread implements Runnable {
		// given a socket, it connects each client that connects
		// to that socket in a new thread; the threads are actually
		// created in the method serve()
		// Socket socket;
		int puzzleNumberValue;
		String guessText;

		public GuessThread(int puzzleNumberValue, String guessText) {
			// this.socket = socket;
			this.puzzleNumberValue = puzzleNumberValue;
			this.guessText = guessText;
		}

		public void run() {
			makeGuess(this.puzzleNumberValue, this.guessText);
		}
	}


	public void actionPerformed(ActionEvent e) {
		if (newPuzzleButton.equals(e.getSource()) | newPuzzleNumber.equals(e.getSource())) {
			puzzleNumberValue = Integer.valueOf(newPuzzleNumber.getText());
			// updatePuzzleNumber(puzzleNumber);
			puzzleNumber.setText("Puzzle #" + puzzleNumberValue);
			guessTable.reset();
		} else if (guess.equals(e.getSource())) {
			guessTable.addGuessToRow(guess.getText());
			String guessed = guess.getText();
			guess.setText("");
			(new Thread(new GuessThread(puzzleNumberValue, guessed))).start();
			guess.setText("");
		}
	}

	public class JottoTable extends JTable {
		DefaultTableModel model;
		private JTable table;

		public JottoTable() {
			model = new DefaultTableModel();
			table = new JTable(model);
			// add columns to table
			model.addColumn("guess");
			model.addColumn("matchCount");
			model.addColumn("positionCount");
			model.addRow(new Object[]{"", "", ""});
		}

		public void addGuessToRow(String guess) {
			model.addRow(new Object[]{guess, "", ""});
		}

		public void addRow(String guess, String matchesTotal, String matchesPosition) {
			model.addRow(new Object[]{guess, matchesTotal, matchesPosition});
		}

		public void insertResults(String matchesTotal, String matchesPosition, int row) {
			model.setValueAt(matchesTotal, row, 1);
			model.setValueAt(matchesPosition, row, 2);
		}

		public int getRowCount() {return model.getRowCount();}

		public void reset() {
			int rowCount = model.getRowCount();
			for (int i = rowCount - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			model.addRow(new Object[]{"", "", ""});
		}

		public JTable getTable() {return table;}

		public void victory() {
			model.addRow(new Object[]{guess, "Correct! You win!", ""});
		}
	}



	public void setLayout(GroupLayout layout) {
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(puzzleNumber)
								.addComponent(newPuzzleButton)
								.addComponent(newPuzzleNumber))
						.addGroup(layout.createSequentialGroup()
								.addComponent(guessLabel)
								.addComponent(guess))
						.addComponent(guessTable.getTable())

		);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(puzzleNumber)
								.addComponent(newPuzzleButton)
								.addComponent(newPuzzleNumber))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(guessLabel)
								.addComponent(guess))
						.addComponent(guessTable.getTable())

		);
	}



	public JottoGUI() {
		// randomly set initial puzzle nunber
		puzzleNumberValue = (int)(Math.random() * ( 10000 - 1 ));
		newPuzzleButton = new JButton("New Puzzle");
		newPuzzleButton.setName("newPuzzleButton");
		newPuzzleNumber = new JTextField();
		newPuzzleNumber.setName("newPuzzleNumber");
		puzzleNumber = new JLabel("Puzzle #" + puzzleNumberValue);
		puzzleNumber.setName("puzzleNumber");
		guess = new JTextField();
		guess.setName("guess");
		guessLabel = new JLabel("Type a guess here:");
		guessLabel.setName("guessLabel");
		DefaultTableModel model = new DefaultTableModel();
		guessTable = new JottoTable();

		// add actionEvens
		newPuzzleButton.addActionListener(this);
		newPuzzleNumber.addActionListener(this);
		guess.addActionListener(this);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);
	}

	public static void main(final String[] args) throws IOException {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();
				main.setVisible(true);
			}
		});
	}
}
