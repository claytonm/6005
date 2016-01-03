package parser;

import player.Header;
import player.ReadABCFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 12/17/15.
 */
public class Tuplet implements NoteInterface {
    int tupletInt;
    double tuplet;
    int numerator = 1;
    int denominator = 1;
    double singleNoteLength = 0.0;
    String tupletString;
    List<Note> notes = new ArrayList<Note>();

    public List<Note> getNotes() {return notes;}
    public String getTupletString() {return tupletString;}

    public Tuplet(String tupletString, Header header, Signature signature, BarSignature barSignature) {
        // tupletInt = Integer.valueOf(tupletString.substring(1,2));
        String tupletStringTemp = tupletString;
        Lexer tupletLexer = new Lexer(tupletStringTemp);
        Parser parser = new Parser(tupletLexer, header);
        Segment segment = parser.Parse(tupletLexer);
        tupletLexer = new Lexer(tupletString);
        Note note;
        while (tupletLexer.hasNext()) {
            Lexer.Token token = tupletLexer.next();
            note = new Note(token.getText(), segment.getSignature(), barSignature);
            notes.add(note);
            singleNoteLength = note.getLength();
        }
        if (tupletInt == 2) {
            tuplet = (double)2/(double)3;
        } else if (tupletInt == 3) {
            tuplet = (double)3/(double)2;
        } else {tuplet = (double)4/(double)3;}
    }

    public double getTuplet() {return tuplet;}

    public double getLength() {
        if (tupletInt == 2) {
            return singleNoteLength * 3;
        } else if (tupletInt == 3) {
            return singleNoteLength * 2;
        } else {
            return singleNoteLength * 3;
        }
    }

    public double getTickLength() {
        return(getLength()*tuplet);
    }

    public <R> R accept(Visitor<R> v) {return v.on(this);}

    public static void main(String[] args) {
        // test parsing of tuplet string into number and notes
        String file = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/proj1/sample_abc/scale.abc";
        ReadABCFile abc = new ReadABCFile(file);
        List<String> abcMusic = abc.fromFile(file);
        int n = abc.getLastHeaderLine(abcMusic);
        Header header = abc.getHeader(abcMusic);
        Signature signature = new Signature("C");
		BarSignature barSignature = new BarSignature();
        String tupletString = "(3ABC";
        Tuplet tuplet = new Tuplet(tupletString, header, signature, barSignature);
        System.out.print(tuplet.getNotes());


        // System.out.print("Tuplet Number: " + tupletNum + ", Tuplet string: " + tupletString);
    }
}
