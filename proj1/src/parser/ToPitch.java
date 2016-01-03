package parser;

import com.sun.tools.classfile.ReferenceFinder;
import player.Play;
import sound.Pitch;
import sound.SequencePlayer;

/**
 * Created by clay on 12/16/15.
 */
public class ToPitch implements Visitor<Integer> {
    private Note note;
    private Play play;
    private SequencePlayer sequencePlayer;
    private Integer startTime;

    public ToPitch(Play play, SequencePlayer sequencePlayer, Integer startTime) {
        this.note = note;
        this.play = play;
        this.sequencePlayer = sequencePlayer;
        this.startTime = startTime;
    }

    public Integer on(Note note) {
        Pitch pitch;
        pitch = play.noteToPitch(note);
        int ticks = (int)(play.getTicksPerQuarterNote()*note.getLength()*play.getHeader().getNoteLength()/play.getQuarterNote());
        sequencePlayer.addNote(pitch.toMidiNote(), startTime, ticks);
        startTime = startTime + ticks;
        return startTime;
    }

    public Integer on(Rest rest) {
        int ticks = (int)(play.getTicksPerQuarterNote()*rest.getLength()*play.getHeader().getNoteLength()/play.getQuarterNote());
        startTime = startTime + ticks;
        return startTime;
    }

    public Integer on(Chord chord) {
        Pitch pitch;
        int maxTicks = (int)(play.getTicksPerQuarterNote()*chord.getLength()*play.getHeader().getNoteLength()/play.getQuarterNote());
        for (Note note : chord.getNotes()) {
            pitch = play.noteToPitch(note);
            int ticks = (int)(play.getTicksPerQuarterNote()*note.getLength()*play.getHeader().getNoteLength()/play.getQuarterNote());
            sequencePlayer.addNote(pitch.toMidiNote(), startTime, ticks);
        }
        startTime = startTime + maxTicks;
        return startTime;
    }

    public Integer on(Tuplet tuplet) {
        Pitch pitch;
        for (Note note : tuplet.getNotes()) {
            pitch = play.noteToPitch(note);
            int ticks = (int)(play.getTicksPerQuarterNote()*note.getLength()*play.getHeader().getNoteLength()/(play.getQuarterNote()*tuplet.getTuplet()));
            sequencePlayer.addNote(pitch.toMidiNote(), startTime, ticks);
            startTime = startTime + ticks;
        }
        return startTime;
    }
}
