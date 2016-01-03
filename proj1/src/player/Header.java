package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Header {
    private  int index;
    private  String title;
    private  String key = "C";
    private  String composer = null;
    private  String noteLength = "1/8";
    private  String meter = "4/4";
    private  List<String> voices = new ArrayList<>(Arrays.asList("0"));
    private  int tempo = 90;

    public Header (int index,
                   String title,
                   String key) {
        this.index = index;
        this.title = title;
        this.key = key;
    }

    public Header (int index,
                   String title,
                   String key,
                   String noteLength,
                   String meter) {
        this.index = index;
        this.title = title;
        this.key = key;
        this.noteLength = noteLength;
        this.meter = meter;
    }

    public Header (int index,
                   String title,
                   String key,
                   String noteLength,
                   String meter,
                   int tempo,
                   List<String> voices,
                   String composer) {
        this.index = index;
        this.title = title;
        this.key = key;
        this.noteLength = noteLength;
        this.meter = meter;
        this.composer = composer;
        this.voices = voices;
        this.tempo = tempo;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public String getComposer() {
        return composer;
    }

    public double getNoteLength() {
        int numerator = Integer.valueOf(noteLength.substring(0,1));
        int denominator = Integer.valueOf(noteLength.substring(2,3));
        return (double)numerator/(double)denominator;
    }

    public String getMeter() {
        return meter;
    }

    public List<String> getVoices() { return voices;}

    public int getTempo() { return (int)(tempo*getNoteLength()/0.25);}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(key);
        sb.append(noteLength);
        sb.append(meter);
        sb.append(index);
        sb.append(composer);
        sb.append(voices.toString());
        sb.append(tempo);
        return sb.toString();
    }
}