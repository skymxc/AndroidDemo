package com.skymxc.demo.down;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mxc on 2017/8/28.
 * description:
 */

public abstract class CustomObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EventStart){
            updateStart((EventStart) arg);
        }else if(arg instanceof EventComplete){
            updateComplete((EventComplete) arg);
        }else if(arg instanceof EventProgress){
            updateProgress((EventProgress) arg);
        }else if( arg instanceof EventError){
            updateError((EventError) arg);
        }else{
            update(arg);
        }
    }

    public abstract void updateStart(EventStart event);
    public abstract void updateComplete(EventComplete event);
    public abstract void updateError(EventError event);
    public abstract void updateProgress(EventProgress event);
    public  void update(Object event){

    }
}
