package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	// accessor method for puzzleNumberValue
	public int getPuzzleNumber() {return puzzleNumberValue;}
	// update method for puzzleNumber label
	public void updatePuzzleNumber(JLabel label) {
		label.setText("Puzzle #" + puzzleNumberValue);
	}

	public void setLayout(GroupLayout layout) {
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(puzzleNumber)
				.addComponent(newPuzzleButton)
				.addComponent(newPuzzleNumber)
		);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(puzzleNumber)
								.addComponent(newPuzzleButton)
								.addComponent(newPuzzleNumber))
		);
	}

	public void actionPerformed(ActionEvent e) {
		if (newPuzzleButton.equals(e.getSource()) | newPuzzleNumber.equals(e.getSource())) {
			puzzleNumberValue = Integer.valueOf(newPuzzleNumber.getText());
			// updatePuzzleNumber(puzzleNumber);
			puzzleNumber.setText("Puzzle #" + puzzleNumberValue);
		}
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

		// add actionEvens
		newPuzzleButton.addActionListener(this);
		newPuzzleNumber.addActionListener(this);

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
