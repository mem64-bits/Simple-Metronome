package com.github.mem64bits.simple.metronome.commands;

import com.badlogic.gdx.Gdx;

public class QuitApp implements Command{
    @Override
    public void execute(){
        Gdx.app.exit();
    }
}
