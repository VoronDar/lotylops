package com.lya_cacoi.lotylops.activities.utilities;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SoundManager {

    private SoundPool sp;
    private int soundIdExplosion;
    private AppCompatActivity activity;
    private boolean soundExist;
    private ArrayList<Integer> ExplosionsList;


    //----------------------------------------------------------------------------------------------



    //////// LIFECYCLE /////////////////////////////////////////////////////////////////////////////
    public SoundManager(AppCompatActivity activity){
        this.activity = activity;
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundIdExplosion = 0;
    }
    public void closeSound(){
        sp.release();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean initSoundPool(String soundPath){                                                 // load sound and play it after loading
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                playSound();
            }
        });

        try {
            soundIdExplosion = sp.load(activity.getAssets().openFd(soundPath), 1);
            soundExist = true;
        } catch (IOException e) {
            e.printStackTrace();
            soundExist = false;
        }
        return soundExist;
    }
    public boolean isSoundExist() {
        return soundExist;
    }
    public void playSound(){                                                                        // play one sound
        sp.play(soundIdExplosion, 1, 1, 0,
                0, 1);
    }
    public boolean playSound(int index){                                                            // play one sound from several (return false if it's not exist)
        if (ExplosionsList.size() > index) {
            sp.play(ExplosionsList.get(index), 1, 1, 0,
                    0, 1);
            return true;
        }
        else
            return false;
    }
    public boolean loadSeveralSound(ArrayList<String> soundPaths){                                  // load several sounds and put theirs Id into Explosions List
        ExplosionsList = new ArrayList<>(soundPaths.size());
        for (String n : soundPaths) {
            try {
                ExplosionsList.add(sp.load(activity.getAssets().openFd(n + ".mp3"), 1));
                soundExist = true;
            } catch (IOException e) {
                Log.i("mainSound", n);
                e.printStackTrace();
                soundExist = false;
                ExplosionsList.clear();
                break;
            }
        }
        return soundExist;
    }


}
