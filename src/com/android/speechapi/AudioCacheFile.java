package com.android.speechapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioCacheFile {
    
    private ByteArrayInputStream mInputStream;
    private ByteArrayOutputStream mOutputStream;
    private AudioFormat mFormat;
    private byte[] mArray;
    
    public AudioCacheFile() {
        mOutputStream = new ByteArrayOutputStream();
    }
    
    public OutputStream getOutputStream() {
        return mOutputStream;
    }
    
    public InputStream getInputStream() {
        mArray = mOutputStream.toByteArray();
        mInputStream = new ByteArrayInputStream(mArray);
        return mInputStream;
    }
   
    public AudioInputStream getAudioInputStream() {
        mArray = mOutputStream.toByteArray();
        return new AudioInputStream(mArray, mFormat);
    }
    
    public AudioFormat getAudioFormat() {
        return mFormat;
    }

    public void setAudioFormat(AudioFormat mFormat) {
        this.mFormat = mFormat;
    }
}
