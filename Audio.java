import java.io.File;
 
 import javax.sound.sampled.AudioFormat;
 import javax.sound.sampled.AudioInputStream;
 import javax.sound.sampled.AudioSystem;
 import javax.sound.sampled.Clip;
 import javax.sound.sampled.DataLine;

public class Audio {


    private Clip clip = null;
    private AudioInputStream audioStream = null;

    public Audio(File f) throws Exception{
        // recuperation d'un stream de type audio sur le fichier
        audioStream = AudioSystem.getAudioInputStream(f);
        // recuperation du format de son
        AudioFormat audioFormat = audioStream.getFormat();
        // recuperation du son que l'on va stoquer dans un oblet de type clip
        DataLine.Info info = new DataLine.Info(
            Clip.class, audioStream.getFormat(),
            ((int) audioStream.getFrameLength() * audioFormat.getFrameSize()));
        // recuperation d'une instance de type Clip
        clip = (Clip) AudioSystem.getLine(info);

    }

    public boolean open(){
        if(clip != null && !clip.isOpen())
            try {
                clip.open(audioStream);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }

    public void close(){
        if(clip != null && clip.isOpen())
            clip.close();
    }

    public void play(){
        if(clip != null && clip.isOpen())
            clip.start();
    }

    public void stop(){
        if(clip != null && clip.isOpen())
            clip.stop();
    }
}
