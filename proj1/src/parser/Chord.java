package parser;

import player.Header;
import player.ReadABCFile;
import sound.Pitch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 12/16/15.
 */
public class Chord implements NoteInterface {

    private List<Note> notes = new ArrayList<Note>();

    public Chord(String chordString, Header header, Signature signature, BarSignature barSignature) {
        chordString = chordString.replace("[", "").replace("]", "");
        Lexer chordLexer = new Lexer(chordString);
        Parser parser = new Parser(chordLexer, header);
        Segment segment = parser.Parse(chordLexer);
        Note note;
        chordString = chordString.replace("[", "").replace("]", "");
        chordLexer = new Lexer(chordString);
        while (chordLexer.hasNext()) {
            Lexer.Token token = chordLexer.next();
            note = new Note(token.getText(), segment.getSignature(), barSignature);
            notes.add(note);
        }
    }

    public double getLength() {
        double maxNoteLength = 1.0;
        double noteLength = 0.0;
        for (NoteInterface note: notes) {
            noteLength = note.getLength();
            if (noteLength > maxNoteLength) {
                maxNoteLength = noteLength;
            }
        }
        return maxNoteLength;
    }

    public List<Note> getNotes() { return this.notes;}

    public String getPitch() {return null;}

    public <R> R accept(Visitor<R> v) {return v.on(this);}

    public static void main (String[] args) {
        String file = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/proj1/sample_abc/sample2.abc";
        ReadABCFile abc = new ReadABCFile(file);
        List<String> abcMusic = abc.fromFile(file);
        int n = abc.getLastHeaderLine(abcMusic);
        Header header = abc.getHeader(abcMusic);
		BarSignature barSignature = new BarSignature();
        Signature signature = new Signature(header.getKey());
//		Chord chord = new Chord("[CEG]", header, signature, barSignature);
		// System.out.print(chord.getNotes());

        String chordString = "[CEG]";
        chordString = chordString.replace("[", "").replace("]", "");
        Lexer chordLexer = new Lexer(chordString);
        Note note;
        Chord chord = new Chord(chordString, header, signature, barSignature);
        System.out.print(chord.getNotes());
//        while (chordLexer.hasNext()) {
//            Lexer.Token token = chordLexer.next();
//            note = new Note(token.getText(), signature, barSignature);
//
//        }
    }

}
