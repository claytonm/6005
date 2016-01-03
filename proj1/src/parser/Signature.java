package parser;

import java.util.*;

/**
 * Created by clay on 12/9/15.
 */
public class Signature {
    Map <String, List<String>> signature = new HashMap<String, List<String>>();

    public Signature(String key) {
        switch (key) {
            case "C" :  signature.put("sharps", new ArrayList<String>());
                signature.put("flats", new ArrayList<String>());
                break;
            case "Am" :  signature.put("sharps", new ArrayList<String>());
                signature.put("flats", new ArrayList<String>());
                break;
            case "D" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "F")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "Bm" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "F")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "E" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "D", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "C#m" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "D", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "F" :  signature.put("sharps", new ArrayList<String>());
                signature.put("flats", new ArrayList<>(Arrays.asList("B")));
                break;
            case "Dm" :  signature.put("sharps", new ArrayList<String>());
                signature.put("sharps", new ArrayList<>(Arrays.asList("B")));
                break;
            case "G" :  signature.put("sharps", new ArrayList<>(Arrays.asList("F")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "Em" :  signature.put("sharps", new ArrayList<>(Arrays.asList("F")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "A" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "Fm" :  signature.put("sharps", new ArrayList<>(Arrays.asList("C", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "B" :  signature.put("sharps", new ArrayList<>(Arrays.asList("A", "C", "D", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "G#" :  signature.put("sharps", new ArrayList<>(Arrays.asList("A", "C", "D", "F", "G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "F#" :  signature.put("sharps", new ArrayList<>(Arrays.asList("A", "C", "D", "E", "F","G")));
                signature.put("flats", new ArrayList<String>());
                break;
            case "D#" :  signature.put("sharps", new ArrayList<>(Arrays.asList("A", "C", "D", "E", "F","G")));
                signature.put("flats", new ArrayList<String>());
                break;
        }
    }

    public Boolean isSharp(String key) {
        return signature.get("sharps").contains(key);
    }

    public Boolean isFlat(String key) {
        return signature.get("flats").contains(key);
    }

    public List<String> getSharps() { return signature.get("sharps");}
}
