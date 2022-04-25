package uqac.dim.gamersguess.persistance;

public class VolumeSingleton {
    private int savedVolume;
    private boolean muted;

    private static VolumeSingleton instance;

    public static VolumeSingleton getInstance() {
        if (instance == null)
            instance = new VolumeSingleton();
        return instance;
    }

    public int getSavedVolume() {
        return savedVolume;
    }

    public void setSavedVolume(int volume) {
        savedVolume = volume;
    }

    public boolean getMute() {
        return muted;
    }

    public void setMute(boolean mute) {
        muted = mute;
    }
}