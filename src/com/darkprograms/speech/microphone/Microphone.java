package com.darkprograms.speech.microphone;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;

import com.android.speechapi.AudioCacheFile;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/***************************************************************************
 * Microphone class that contains methods to capture audio from microphone
 *
 * @author Luke Kuza, Aaron Gokaslan, Firtecy(for Android implementation)
 ***************************************************************************/
public class Microphone implements Closeable {

    /**
     * Enum for current Microphone state
     */
    public enum CaptureState {
        PROCESSING_AUDIO, 
        STARTING_CAPTURE, 
        CLOSED
    }

    /**
     * Variable for enum
     */
    CaptureState state;

    /**
     * Variable for the audios saved file type
     */
    private AudioFormat fileType;

    /**
     * Variable that holds the saved audio CacheFile
     */
    private AudioCacheFile audioFile;

    private BufferedOutputStream mBos;
    private DataOutputStream mDos;
    private int sampleRateInHz = 8000;//8000 44100, 22050 and 11025
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private boolean isRecording;
    
    /**
     * Gets the current state of Microphone
     *
     * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
     *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
     *         CLOSED is returned if the Thread is not doing anything/not capturing audio
     */
    public CaptureState getState() {
        return state;
    }

    /**
     * Sets the current state of Microphone
     *
     * @param state State from enum
     */
    private void setState(CaptureState state) {
        this.state = state;
    }

    public AudioCacheFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(AudioCacheFile audioFile) {
        this.audioFile = audioFile;
    }

    public AudioFormat getFileType() {
        return fileType;
    }

    public void setFileType(AudioFormat fileType) {
        this.fileType = fileType;
    }
    
    //------ For Android -----------/
    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }
    
    /**
     * Constructor
     *
     * @param fileType File type to save the audio in<br>
     *                 Example, to save as WAVE use AudioFileFormat.Type.WAVE
     * @deprecated Not used for android...
     */
    public Microphone(AudioFormat fileType) {
        setState(CaptureState.CLOSED);
        setFileType(fileType);
        initTargetDataLine();
    }
    
    public Microphone() {
        setState(CaptureState.CLOSED);
        initTargetDataLine();
    }

    /**
     * Initializes the target data line.
     */
    private void initTargetDataLine(){
        mBos = new BufferedOutputStream(audioFile.getOutputStream());
        mDos = new DataOutputStream(mBos);
    }


    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The File to save the audio to
     * @throws Exception Throws an exception if something went wrong
     */
    public void captureAudioToFile(AudioCacheFile audioFile) throws Exception {
        setState(CaptureState.STARTING_CAPTURE);
        setAudioFile(audioFile);

        open();
    }

    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The fully path (String) to a file you want to save the audio in
     * @throws Exception Throws an exception if something went wrong
     * @deprecated Not implemented in Android...
     */
    public void captureAudioToFile(String audioFile) throws Exception {
        throw new Exception();
    }

	
    /**
     * The audio format to save in
     *
     * @return Returns AudioFormat to be used later when capturing audio from microphone
     * @deprecated Not implemented in Android...
     */
    public AudioFormat getAudioFormat() {
        return null;
    }

    /**
     * Opens the microphone, starting the targetDataLine.
     * If it's already open, it does nothing.
     */
    public void open() {
        if(mDos==null){
        	initTargetDataLine();
        }
        if(!isRecording){
            new Thread(new CaptureThread()).start();
        }
    }

    /**
     * Close the microphone capture, saving all processed audio to the specified file.<br>
     * If already closed, this does nothing
     */
    public void close() {
        if (getState() == CaptureState.CLOSED) {
        } else if(isRecording) {
            isRecording = false;
            setState(CaptureState.CLOSED);
        }
    }

    /**
     * Thread to capture the audio from the microphone and save it to a file
     */
    private class CaptureThread implements Runnable {

        /**
         * Run method for thread
         */
        public void run() {
            try {
                int bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz,channelConfig, audioFormat);
                short[] buffer = new short[bufferSize];
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
                        sampleRateInHz,channelConfig, audioFormat,bufferSize);

                audioRecord.startRecording();

                isRecording = true;
                while (isRecording) {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    for (int i = 0; i < bufferReadResult; i++) {
                        mDos.writeShort(buffer[i]);
                    }
                }
                mDos.close();
                mBos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mDos = null;
                mBos = null;
            }
        }
    }

}
