package parser;

import parser.Lexer.Token;
import player.Header;
import player.ReadABCFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 12/7/15.
 */
public class Parser {

    Lexer lexer;
    Header header;
    // String expression;
//    private static final double PT_PER_IN = 72;

    @SuppressWarnings("serial")
    static class ParserException extends RuntimeException {
    }

    public Parser(Lexer lexer, Header header) {
        this.lexer = lexer;
        this.header = header;
    }

    // TODO write method spec

    public Segment Parse(Lexer lexer) {
        Segment segment = new Segment(header);
        Bar bar = new Bar(segment);
        while (lexer.hasNext()) {
            Token token = lexer.next();
            if (token.isDoubleBar()) { // begin subsegment
                segment.addSegment(bar);
                Segment subSegment = Parse(lexer);
                segment.addSegment(subSegment);
                bar = new Bar(segment);
            } else if (token.isNote()) {
                NoteInterface note = new Note(token.getText(), segment.getSignature(), bar.getBarSignature());
                bar.addNote(note);
            } else if (token.isRest()) {
                NoteInterface note = new Rest(token.getText(), segment.getSignature());
                bar.addNote(note);
            } else if (token.isChord()) {
                NoteInterface chord = new Chord(token.getText(), header, segment.getSignature(), bar.getBarSignature());
                bar.addNote(chord);
            } else if (token.isTuple()) {
                NoteInterface tuplet = new Tuplet(token.getText(), header, segment.getSignature(), bar.getBarSignature());
                bar.addNote(tuplet);
            } else if (token.isBar()) {
                segment.addSegment(bar);
                bar = new Bar(segment);
            } else if (token.isOpenRepeat()) {
                segment.addSegment(bar);
                Segment subSegment = Parse(lexer);
                segment.addSegment(subSegment);
            } else if (token.isCloseRepeat()) {
                segment.addSegment(bar);
                segment.addSegment(segment);
                bar = new Bar(segment);
            }
        }
        return segment;
    }

    public static void main (String[] args) {
//        String file = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/proj1/sample_abc/scale.abc";
//        ReadABCFile abc = new ReadABCFile(file);
//        List<String> abcMusic = abc.fromFile(file);
//        int n = abc.getLastHeaderLine(abcMusic);
//        Header header = abc.getHeader(abcMusic);
//        String music = abc.getMusic(abcMusic);
//        Lexer lexer = new Lexer(music);
//        Parser parser = new Parser(lexer, header);
//        Segment segment = parser.Parse(lexer);
//        System.out.print(segment.toString());
    }
}

