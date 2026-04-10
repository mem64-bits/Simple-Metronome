package com.github.mem64bits.simple.metronome.internals;

public abstract class Timer {
    protected long elapsedMillis;
    private double accumulator;
    private boolean active;

    public void deltaAccumulate(double deltaTime) {
        if (!active) return;
        accumulator += deltaTime * 1000;
        this.elapsedMillis = (long) accumulator;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() { return active; }

    public abstract void update(double deltaTime);
    public abstract void start();
    public abstract void stop();
    public abstract void pause();
    public abstract void resume();


    public void reset(){
        this.active = false;
        this.elapsedMillis = 0;
        this.accumulator = 0.0;
    }

    public double getAccumulator(){
        return accumulator;
    }

    public void setAccumulator(double accumulator){
        this.accumulator = accumulator;
    }
}
