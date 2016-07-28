package ui;

import model.JottoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * // TODO Write specifications for your JottoGUI class.
 */
public class JottoGUI extends JFrame implements ActionListener {

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

		JottoModel jotto = new JottoModel(Integer.toString(puzzleNumberValue));
		try {
			String response = jotto.makeGuess(guessText);
			String[] responseArgs = response.split("\\s+");
			String matchesTotal = responseArgs[1];
			String positionMathces = responseArgs[2];
			if (response == "guess 5 5") {
				// System.out.println("You win!. The secret word was " + guessText);
				guessTable.victory();
			} else {
				// System.out.println(response);
				guessTable.addRow(guessText, matchesTotal, positionMathces);
			}
			guess.setText("");
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	private void handleGuess(int puzzleNumberValue,
							 String guessText,
							 Socket socket,
							 Boolean keepRunning) throws IOException {

		guessTable.addGuessToRow(guessText);
		int rows = guessTable.getRowCount() - 1;

		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		try {
			makeGuess(puzzleNumberValue, guessText);
			} finally {
			out.close();
		}
	}



	public void actionPerformed(ActionEvent e) {
		if (newPuzzleButton.equals(e.getSource()) | newPuzzleNumber.equals(e.getSource())) {
			puzzleNumberValue = Integer.valueOf(newPuzzleNumber.getText());
			// updatePuzzleNumber(puzzleNumber);
			puzzleNumber.setText("Puzzle #" + puzzleNumberValue);
			guessTable.reset();
		} else if (guess.equals(e.getSource())) {

			makeGuess(puzzleNumberValue, guess.getText());
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

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();
				main.setVisible(true);
			}
		});
	}
}
