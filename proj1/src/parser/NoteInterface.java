package parser;

import sound.Pitch;

/**
 * Created by clay on 12/16/15.
 */
public interface NoteInterface {
    public double getLength();
    public <R> R accept(Visitor<R> v);

}
