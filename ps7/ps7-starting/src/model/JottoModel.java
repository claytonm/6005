package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * // TODO Write specifications for your JottoModel!
 */
public class JottoModel {

	private String puzzleNumber;
	private String baseURL;

	public JottoModel(String puzzleNumber) {
		this.puzzleNumber = puzzleNumber;
		this.baseURL = "http://127.0.0.1:8000/cgi-bin/jotto.py?";
	}

	// access puzzleNumber
	public String getPuzzleNumber() {return this.puzzleNumber;}
	// access baseURL
	public String getBaseURL() {return baseURL;}

	/**
	 *
	 * @param guess: five-letter word
	 * @return: String in format guess <Integer> <Integer>
	 * @throws IOException
     */
	public String makeGuess(String guess) throws IOException {
		String urlString = getBaseURL() + "puzzle=" + getPuzzleNumber() + "&guess=" + guess;
		URL jotto = new URL(urlString);
		URLConnection connection = jotto.openConnection();
		BufferedReader in = new BufferedReader((new InputStreamReader(connection.getInputStream())));
		String response = in.readLine();
		in.close();
		return response;
	}
}
