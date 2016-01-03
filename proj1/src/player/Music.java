package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clay on 12/20/15.
 */
public class Music {
    Map<String, List<String>> music = new HashMap<String, List<String>>();

    Music(List<String> voices) {
        for (String voice : voices) {
            music.put(voice, new ArrayList<String>());
        }
    }

    public List<String> getVoice(String voice) {
        return music.get(voice);
    }

    public String toString() {return music.toString();}
}
