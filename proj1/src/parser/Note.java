package parser;

import player.Play;
import sound.SequencePlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clay on 12/8/15.
 */
public class Note implements NoteInterface {

    private String noteString;
    private int accidental = 0;
    private int octave = 0;
    private int numerator = 1;
    private int denominator = 1;
    private String pitch;

    public int getOctave() {
        return this.octave;
    }
    public int getAccidental() {
        return this.accidental;
    }
    public String getPitch() {return this.pitch;}
    public double getLength(){return (double)numerator/(double)denominator;}

    public Note() {};

    public Note(String noteString, Signature signature, BarSignature barSignature) {

        this.noteString = noteString;

        // set octave
        Pattern pUp = Pattern.compile("\\'+");
        Pattern pDown = Pattern.compile("\\,+");
        Matcher mUp = pUp.matcher(noteString);
        Matcher mDown = pDown.matcher(noteString);
        if (mUp.find()) {
            this.octave = mUp.end() - mUp.start();
        } else if (mDown.find()) {
            this.octave = -1*(mDown.end() - mDown.start());
        } else {this.octave =  0;}

        // get pitch
        Pattern pPitch = Pattern.compile("[a-g[A-G]]");
        Matcher mPitch = pPitch.matcher(noteString);
        mPitch.find();
        mPitch.group();
        int start = mPitch.start();
        int end = mPitch.end();
        String pitch = noteString.substring(start, end);
        if (pitch == pitch.toLowerCase()) {
            pitch = pitch.toUpperCase();
            this.octave++;
        }
        this.pitch = pitch;

        // set accidental
        pUp = Pattern.compile("\\^+");
        pDown = Pattern.compile("_+");
        mUp = pUp.matcher(noteString);
        mDown = pDown.matcher(noteString);
        Pattern pNeutral = Pattern.compile("\\=");
        Matcher mNeutral = pNeutral.matcher(noteString);

        // if note has neutral mark, set accidental to 0
        if (mNeutral.find()) {
            this.accidental = 0;
            barSignature.addNeutral(this.pitch);

        } else {
            if (mUp.find()) { // if note has sharps, count them and add to accidental
                this.accidental = mUp.end() - mUp.start();
                barSignature.addAccidental(this.pitch, this.accidental);
            } else if (mDown.find()) { // if note has flats, count them and subtract from accidental
                this.accidental = mDown.start() - mDown.end();
                barSignature.addAccidental(this.pitch, this.accidental);
            } else {this.accidental =  0;}

            if (this.accidental == 0) { // if note has no accidental marks, check signature and bar signature, and modify accidental accordingly
                this.accidental = barSignature.getAccidental(this.pitch);
                if (signature.isFlat(this.pitch) && barSignature.getAccidental(this.pitch) == 0) {this.accidental--;}
                if (signature.isSharp(this.pitch) && barSignature.getAccidental(this.pitch) == 0) {this.accidental++;}
            }
        }

        // get length
        Pattern justNumber = Pattern.compile("([a-gA-G]|'*|,*)[0-9](?!/)");
        Matcher mJustNumber = justNumber.matcher(noteString);
        Boolean mJustNumberFound = mJustNumber.find();
        Pattern pLength = Pattern.compile("[0-9]*/[0-9]*|/");
        Matcher m = pLength.matcher(noteString);
        Boolean found = m.find();
        if (mJustNumberFound) {
            mJustNumber.group();
            end = mJustNumber.end();
            numerator = Integer.parseInt(noteString.substring(end-1, end));
            denominator = 1;
        }
        if (found) {
            m.group();
            start = m.start();
            end = m.end();
            if (end - start == 1) {
                this.numerator = 1;
                this.denominator = 2;
            } else if (noteString.substring(start, start+1).equals("/")) {
                this.numerator = 1;
                this.denominator = Integer.parseInt(noteString.substring(end-1, end));
            } else if (noteString.substring(end-1, end).equals("/")) {
                this.numerator = Integer.parseInt(noteString.substring(start, start+1));
                this.denominator = 2;
            } else {
                this.numerator = Integer.parseInt(noteString.substring(start, start+1));
                this.denominator = Integer.parseInt(noteString.substring(end-1, end));
            }
        }
    }

    public String toString() {
        String stringRep = "Accidental: " + accidental + ", octave: " + octave + ", pitch: " +  pitch + ", length: " + numerator + "/" + denominator;
        // String stringRep = pitch + " ";
        return stringRep;
    }

    public static void main(String[] args) {
        String noteString = "E2";
		Signature signature = new Signature("C");
		BarSignature barSignature = new BarSignature();
		Note note = new Note(noteString, signature, barSignature);
		System.out.print(note.toString());
    }

    public <R> R accept(Visitor<R> v) {return v.on(this);}
}
