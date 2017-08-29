package com.skymxc.demo.down;

import java.util.Observable;

/**
 * Created by mxc on 2017/8/29.
 * description:
 */

public class CustomObservable extends Observable {

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
