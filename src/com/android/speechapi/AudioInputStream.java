package com.android.speechapi;

import java.io.ByteArrayInputStream;

public class AudioInputStream extends ByteArrayInputStream {

    private AudioFormat mFormat;
    
    public AudioInputStream(byte[] buf, AudioFormat f) {
        super(buf);
        mFormat = f;
    }

    public AudioFormat getFormat() {
        return mFormat;
    }

    public int getFrameLength() {
        return 0;
    }

}
