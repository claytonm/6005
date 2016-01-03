package parser;

import player.Play;
import sound.SequencePlayer;

/**
 * Created by clay on 12/16/15.
 */
public interface Visitor<R> {
    public R on(Note note);
    public R on(Rest rest);
    public R on(Chord chord);
    public R on(Tuplet Tuplet);
}
