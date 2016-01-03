package player;

import player.Header;

import javax.print.DocFlavor;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadABCFile {
    private String filename;

    private boolean endOfHeader(String s) {
        Pattern p = Pattern.compile("^K:.+");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    class LineID {
        private boolean isIndex(String s) {
            Pattern p = Pattern.compile("^X:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        private boolean isTitle(String s) {
            Pattern p = Pattern.compile("^T:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        private boolean isComposer(String s) {
            Pattern p = Pattern.compile("^C:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        private boolean isMeter(String s) {
            Pattern p = Pattern.compile("^M:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        private boolean isTempo(String s) {
            Pattern p = Pattern.compile("^Q:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        private boolean isNoteLength(String s) {
            Pattern p = Pattern.compile("^L:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        public boolean isKey(String s) {
            Pattern p = Pattern.compile("^K:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        public boolean isVoice(String s) {
            Pattern p = Pattern.compile("^V:.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        public String getValue(String s) {
            Pattern p = Pattern.compile("^[a-zA-Z]:\\s*");
            String[] components = p.split(s);
            return components[1];
        }

        public boolean isComment(String s) {
            Pattern p = Pattern.compile("^\\s*%.*");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        public boolean containsComment(String s) {
            Pattern p = Pattern.compile(".+%.+");
            Matcher m = p.matcher(s);
            return m.matches();
        }

        public String removeComment(String s) {
            Pattern p = Pattern.compile("%");
            String[] components = p.split(s);
            return components[0];
        }
    }

    public ReadABCFile (String file) {
        this.filename = filename;
    }


    public  List<String> fromFile(String filename) {
        Path path = Paths.get(filename);
        List<String> abcMusicAndHeader = null;
        // read in music file as List<String> abcMusicAndHeader
        try {
            abcMusicAndHeader = Files.readAllLines(path);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return abcMusicAndHeader;
    }

    public int getLastHeaderLine(List<String> abc) {
        int lastHeaderIndex = 0;
        LineID lid = new LineID();
        String line = abc.get(lastHeaderIndex);
        while (!(lid.isKey(line))) {
            lastHeaderIndex++;
            line = abc.get(lastHeaderIndex);
        }
        return lastHeaderIndex;
    }

    public  Header getHeader(List<String> abc) {
        int lastHeaderIndex = getLastHeaderLine(abc);
        int lineIndex = 0;
        int index = 0;
        String title = "";
        String key = "";
        String composer = "";
        String meter = "";
        String noteLength = "";
        String line;
        int tempo = 90;
        List<String> voices = new ArrayList<String>();
        LineID lid = new LineID();
        while (lineIndex < lastHeaderIndex) {
            line = abc.get(lineIndex);
            if (lid.isIndex(line)) {
                index = Integer.parseInt(lid.getValue(line));
            } else if (lid.isTitle(line)) {
                title = lid.getValue(line);
            } else if (lid.isComposer(line)) {
                composer = lid.getValue(line);
            } else if (lid.isMeter(line)) {
                meter = lid.getValue(line);
            } else if (lid.isKey(line)) {
                key = lid.getValue(line);
            } else if (lid.isVoice(line)) {
                voices.add(lid.getValue(line));
            } else if (lid.isTempo(line)){
                tempo = Integer.parseInt(lid.getValue(line));
            } else {
                noteLength = lid.getValue(line);
            }
            lineIndex++;
        }
        key = lid.getValue(abc.get(lineIndex));
        if (noteLength.isEmpty()) {
            return new Header(index, title, key);
        } else if (composer.isEmpty()) {
            return new Header(index, title, key, noteLength, meter);
        } else {
            return new Header(index, title, key, noteLength, meter, tempo, voices, composer);
        }
    }

    public HashMap<String, String> getMusic(List<String> abc) {
        Header header = getHeader(abc);
        List<String> voices = header.getVoices();
        List<String> abcMusic = abc.subList(getLastHeaderLine(abc) + 1, abc.size());
        Music music = new Music(voices);
        LineID lid = new LineID();
        String voice = "1";
        if (!(voices.size() == 0)) {
            voice = voices.get(0);
        }

        for (String line : abcMusic) {
            if (lid.isVoice(line)) {
                voice = lid.getValue(line);
            } else if (!(lid.isComment(line)) && !line.isEmpty()) {
                if (lid.containsComment(line)) {
                    music.getVoice(voice).add(lid.removeComment(line));
                }
                music.getVoice(voice).add(line);
            }
        }

        HashMap<String, String> musicWithVoices = new HashMap<String, String>();
        for (String voiceString : voices) {
            musicWithVoices.put(voiceString, new String());
        }

        for (String voiceString : voices) {
            StringBuilder musicString = new StringBuilder();
            List<String> musicVoice = music.getVoice(voiceString);
            for (String line : musicVoice) {
                musicString.append(line);
            }
            musicWithVoices.put(voiceString, musicString.toString().replaceAll("\\s+",""));
        }

        // convert StringBuilder to String and remove all blank space before returning
        return musicWithVoices;
        //  return music;
    }

    public static void main (String[] args) {
        String file = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/proj1/sample_abc/scale.abc";
        ReadABCFile abc = new ReadABCFile(file);
        List<String> abcMusic = abc.fromFile(file);
        HashMap<String, String> music = abc.getMusic(abcMusic);
        System.out.print(music.toString());
    }
}