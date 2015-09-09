package piano;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import midi.Instrument;
import music.Pitch;
import music.NoteEvent;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.KeyEvent;

public class PianoMachine {
	
	private Midi midi;
    private midi.Instrument instr;
    private int pitchShift = 0;
    private boolean isRecording = false;
    private boolean isPlayingBack = false;
    private ArrayList<music.NoteEvent> eventHistory = new ArrayList<music.NoteEvent>();
    public long endPlayback = 0;
    
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
            instr = Instrument.PIANO;
            // int pitchShift = 0;
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }

    /**
     * Begin playing a note when key is pressed.
     * If piano is in recording mode, add note event to list
     * of recorded note events.
     * @param rawPitch
     * @return none
     */
    public void beginNote(Pitch rawPitch) {
        // question 1
        Pitch pitchTranspose = rawPitch.transpose(this.pitchShift);
        midi.beginNote(pitchTranspose.toMidiFrequency(), this.instr);

        if (this.isRecording) {
            long time = (new Date()).getTime();
            NoteEvent note = new NoteEvent(pitchTranspose, time, this.instr, NoteEvent.Kind.start);
            eventHistory.add(note);
        }
    }

    /**
     * End playing a note when key is released.
     * If piano is in recording mode, add note event to list
     * of recorded note events.
     * @param rawPitch
     */
    public void endNote(Pitch rawPitch) {
        // question 1
        Pitch pitchTranspose = rawPitch.transpose(this.pitchShift);
        midi.endNote(pitchTranspose.toMidiFrequency(), this.instr);

        if (this.isRecording) {
            long time = (new Date()).getTime();
            NoteEvent note = new NoteEvent(pitchTranspose, time, this.instr, NoteEvent.Kind.stop);
            eventHistory.add(note);
        }
    }

    /**
     * Change instrument that is being played to the next instrument in the
     * Instrument enum list.
     */
    public void changeInstrument() {
       	// question 2
        this.instr = this.instr.next();
    }

    /**
     * get current instrument
     * @return this.instrument
     */
    public midi.Instrument getInstrument() {
        return this.instr;
    }

    /**
     * Add octave to any subsequent note event.
     * @modifies this.pitchShift
     */
    public void shiftUp() {
    	// question 3
        this.pitchShift = this.pitchShift + 12;
    }

    /**
     * Subtract octave from any subsequent note event.
     * @modifies this.pitchShift
     */
    public void shiftDown() {
    	// question 3
        this.pitchShift = this.pitchShift - 12;
    }

    /**
     * Change boolean value of this.isRecording
     * @return this.isRecording
     * @modifies this.isRecording
     */
    public boolean toggleRecording() {
        // question 4
        if (!this.isRecording) {this.eventHistory = new ArrayList<music.NoteEvent>();}
        this.isRecording = !this.isRecording;
    	return this.isRecording;
    }
    /**
     * Change boolean value of this.isPlayingBack
     * @modifies this.isPlayingBack
     */
    public void togglePlayback() {
        this.isPlayingBack = !this.isPlayingBack;
    }

    /**
     * Get value of this.isPlayingBack
     * @return this.isPlayingBack
     */
    public boolean isPlayingBack() {
        return this.isPlayingBack;
    }

    /**
     * Play back recorded note events as stored in eventHistory.
     * Sequence of notes should be played with same pitch, rhythm, and
     * instrument as original recorded note sequence. During playback
     * mode, no key presses should have any effect.
     */
    protected void playback() {
        togglePlayback();
        long eventPreviousTime = eventHistory.get(0).getTime();
        for (NoteEvent noteEvent : eventHistory) {
            int waitTime = (int) Math.round((noteEvent.getTime() - eventPreviousTime)/10.0);
            midi.wait(waitTime);
            eventPreviousTime = noteEvent.getTime();
            NoteEvent.Kind kind = noteEvent.getKind();
            int pitchFrequency = noteEvent.getPitch().toMidiFrequency();
            Instrument noteEventInstrument = noteEvent.getInstr();
            if (kind == NoteEvent.Kind.start) {
                midi.beginNote(pitchFrequency, noteEventInstrument);
            }
            else {

                midi.endNote(pitchFrequency, noteEventInstrument);
            }
        }
        this.endPlayback = (new Date()).getTime();
        togglePlayback();
    }
}
