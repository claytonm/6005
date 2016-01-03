package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 12/11/15.
 */

public class Bar extends Segment {

    private List<NoteInterface> notes;
    BarSignature barSignature;
    Segment segment;
    boolean repeat = false;
    boolean repeated = false;

    public Bar(Segment segment) {
        notes = new ArrayList<NoteInterface>();
        this.barSignature = new BarSignature();
        this.segment = segment;
    }

    public Bar(Segment segment, boolean repeat) {
        notes = new ArrayList<NoteInterface>();
        this.barSignature = new BarSignature();
        this.segment = segment;
        this.repeat = repeat;
    }

    public void addNote(NoteInterface note) {
        notes.add(note);
    }

    public boolean getRepeat() {
        return repeat;
    }

    public void setRepeated() {
        repeated = true;
    }

    public BarSignature getBarSignature() {
        return barSignature;
    }

    public List<NoteInterface> getNotes() {
        return notes;
    }

    public String toString() {
        StringBuilder noteList = new StringBuilder();
        for (NoteInterface note: notes) {
            noteList.append(note.toString());
        }
        return noteList.toString();
    }
}
