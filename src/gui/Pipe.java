package gui;

import card.*;

public class Pipe {

    private Card _card;
    private Object lock;
    private boolean _enabled;

    public Pipe() {
        lock = new Object();
        _enabled  = false;
    }

    public Card Get() {
        if (!_enabled) return null;
        try {
            while (_card == null) {
                synchronized (lock) {
                    lock.wait();
                }
            }
            return _card;
        } catch (Exception e) {
            return null;
        }
    }

    public void Set(Card c) {
        if (!_enabled) return;
        synchronized (lock) {
            _card = c;
            lock.notifyAll();
        }
    }

    public void Flush() {
        _card = null;
    }

    public void Enable() {
        _enabled = true;
    }

    public void Disable() {
        _enabled = false;
    }
}
