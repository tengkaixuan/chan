
package com.dobi.jiecon;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {
    int playID = 0;
    protected SoundPool soundPool;

    private HashMap<Integer, Integer> soundPoolMap;

    int streamVolume;
    int streamID;
    public Sound(Context paramContext) {
        initSound(paramContext);
    }

    private void initSound(Context paramContext) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
//        int i = ((AudioManager) paramContext.getSystemService("audio"))
//                .getStreamVolume(AudioManager.STREAM_MUSIC);
//        this.streamVolume = i;
        streamVolume  = 1;
        soundPoolMap.put(0, soundPool.load(paramContext, R.raw.tink, 1));
        soundPoolMap.put(1, soundPool.load(paramContext, R.raw.lose, 1));
        soundPoolMap.put(2, soundPool.load(paramContext, R.raw.lottery, 1));
    }

    public int play() {
        streamID = soundPool.play(soundPoolMap.get(0), streamVolume, streamVolume, 1,  
                -1, 1f);
        return streamID;
    }
    
    public int win() {
    	streamID = soundPool.play(soundPoolMap.get(2), streamVolume, streamVolume, 1,  
                0, 1f);
        return streamID;
    }
    
    public int lose() {
    	streamID = soundPool.play(soundPoolMap.get(1), streamVolume, streamVolume, 1,  
                0, 1f);
        return streamID;
    }
    public void stop() {
    	soundPool.stop(streamID);
    }
}
