package piano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();
    	midi.clearHistory();

    	// Problem 1: test beginNote and endNote
        pm.beginNote(new Pitch(1));
        // pm.beginNote(new Pitch(4));
		Midi.wait(100);
		pm.endNote(new Pitch(1));
        // pm.endNote(new Pitch(4));

        System.out.println(midi.history());
        assertEquals(expected0, midi.history());

        // Problem 2: test switchInstruments
        midi.clearHistory();
        String expected1 = "on(61,PIANO) wait(100) off(61,PIANO) wait(0) on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO)";

        pm.beginNote(new Pitch(1));
        // pm.beginNote(new Pitch(4));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        pm.changeInstrument();
        pm.beginNote(new Pitch(1));
        // pm.beginNote(new Pitch(4));
        Midi.wait(100);
        pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected1, midi.history());

        // Problem 3: test switchUp and switchDown
        midi.clearHistory();
        String expected3 = "on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO) wait(0) on(73,BRIGHT_PIANO) wait(100) off(73,BRIGHT_PIANO) wait(100) on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO)";

        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        pm.shiftUp();
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        pm.shiftDown();
        Midi.wait(100);
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected3, midi.history());

        // Problem 4: test switchUp and switchDown
        midi.clearHistory();
        String expected4 = "on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO) wait(0) on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO)";

        pm.toggleRecording();
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        pm.playback();

        System.out.println(midi.history());
        assertEquals(expected4, midi.history());


    }

}
