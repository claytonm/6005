package player;

import parser.Note;
import parser.NoteInterface;
import parser.ToPitch;
import sound.Pitch;
import sound.SequencePlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by clay on 12/13/15.
 */
public class Play {
    private List<NoteInterface> notes = new ArrayList<NoteInterface>();
    private Header header;
    final double quarterNote = (double)1/(double)4;
    private int ticksPerQuarterNote;

    public Play (List<NoteInterface> notes, Header header) {
        this.notes = notes;
        this.header = header;
        double noteLength = header.getNoteLength();
        double minNoteLength = (double)1/(double)4;
        for (NoteInterface note: notes) {
            if (note.getLength() < minNoteLength) {
                minNoteLength = note.getLength();
            }
        }
        ticksPerQuarterNote = (int)(quarterNote/(minNoteLength));
    }

    public Header getHeader() { return header;}
    public int getTicksPerQuarterNote() { return ticksPerQuarterNote;}
    public double getQuarterNote() {return quarterNote;}
    public List<NoteInterface> getNotes() {return notes;}


    public Pitch noteToPitch(Note note) {
        Pitch pitch = new Pitch(note.getPitch().charAt(0));
        pitch = pitch.accidentalTranspose(note.getAccidental());
        pitch = pitch.octaveTranspose(note.getOctave());
        return pitch;
    }

    public static int toPitch(NoteInterface note, Play play, SequencePlayer sequencePlayer, Integer startTime) {
        return note.accept(new ToPitch(play, sequencePlayer, startTime));
    }

    public SequencePlayer notesToSequence() {
        Pitch pitch;
        // get ticksPerQuarterNote
        double noteLength = header.getNoteLength();

        try {
            SequencePlayer sequencePlayer = new SequencePlayer(header.getTempo(), ticksPerQuarterNote);
            int startTime = 0;
            for (NoteInterface note : notes) {
                startTime = toPitch(note, this, sequencePlayer, startTime);
            }
            return sequencePlayer;
        }  catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SequencePlayer notesToSequence(HashMap<String, List<NoteInterface>> voicesToNotes, Header header) {
        List<String> voices = new ArrayList<String>();
        voices.addAll(voicesToNotes.keySet());
        Play play = new Play(voicesToNotes.get(voices.get(0)), header);
        int startTime = 0;
        try {
            SequencePlayer sequencePlayer = new SequencePlayer(header.getTempo(), play.getTicksPerQuarterNote());
            for (String voice : voices) {
                startTime = 0;
                List<NoteInterface> notes = voicesToNotes.get(voice);
                for (NoteInterface note : notes) {
                    startTime = toPitch(note, play, sequencePlayer, startTime);
                }
            }
            return sequencePlayer;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return null;
    }
}

