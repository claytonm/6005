package parser;

import player.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 12/11/15.
 */
public class Segment {

    Header header;
    List<Segment> segments;
    private Signature signature;
    private List<NoteInterface> notes;

    public Segment(Header header) {
        this.header = header;
        segments = new ArrayList<Segment>();
        signature = new Signature(header.getKey());
        notes = new ArrayList<NoteInterface>();
    }

    public Segment() {};

    public Signature getSignature() {
        return signature;
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
        addNotes(segment);
    }

    public void addNote(NoteInterface note) {
        this.getNotes().add(note);
    }

    public void addNotes(Segment segment) {
        notes.addAll(segment.getNotes());
    }

    public List<NoteInterface> getNotes() {
        return notes;
    }

    public String toString() {
        StringBuilder noteList = new StringBuilder();
        for (NoteInterface note : this.getNotes()) {
            noteList.append(note.toString() + "\n");
        }
        return noteList.toString();
    }
}
