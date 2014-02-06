package com.android.speechapi;

public class AudioFormat {

    public static enum Encoding {
        ALAW,
        PCM_FLOAT,
        PCM_SIGNED,
        PCM_UNSIGNED,
        ULAW
    }
    
    private int sampleRateInHz = 8000;//8000 44100, 22050 and 11025
    private int channelConfig = 1;
    private int audioFormat = 1;
    
    public AudioFormat() {}
    
    
    public int getFrameSize() {
        return 2;//arbitrary
    }

    public int getSampleSizeInBits() {
        return 16;
    }

    public int getChannels() {
        return 1;
    }

    public boolean isBigEndian() {
        return false;
    }

    public Encoding getEncoding() {
        return null;
    }

    public float getSampleRate() {
        return sampleRateInHz;
    }
    
}
