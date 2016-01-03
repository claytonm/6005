package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import parser.*;
import sound.Pitch;
import sound.SequencePlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Main entry point of your application.
 */
public class Main {

	/**
	 * Plays the input file using Java MIDI API and displays
	 * header information to the standard output stream.
	 * <p>
	 * <p>Your code <b>should not</b> exit the application abnormally using
	 * System.exit()</p>
	 *
	 * @param file the name of input abc file
	 */
	public static void play(String file) {
		ReadABCFile abc = new ReadABCFile(file);
		List<String> abcMusic = abc.fromFile(file);
		int n = abc.getLastHeaderLine(abcMusic);
		Header header = abc.getHeader(abcMusic);
		List<String> voices = header.getVoices();
		HashMap<String, String> music = abc.getMusic(abcMusic);
		List<NoteInterface> notes = new ArrayList<NoteInterface>();
		HashMap<String, List<NoteInterface>> voicesToNotes = new HashMap<String, List<NoteInterface>>();

		for (String voice : voices) {
			Lexer lexer = new Lexer(music.get(voice));
			Parser parser = new Parser(lexer, header);
			Segment segment = parser.Parse(lexer);
			voicesToNotes.put(voice, segment.getNotes());
		}


		Play play = new Play(voicesToNotes.get(voices.get(0)), header);
		SequencePlayer sequencePlayer = play.notesToSequence(voicesToNotes, header);
		System.out.print(sequencePlayer);
		try {
			sequencePlayer.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args) {
		String filename = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/proj1/sample_abc/debussy.abc";
		play(filename);
	}
}
