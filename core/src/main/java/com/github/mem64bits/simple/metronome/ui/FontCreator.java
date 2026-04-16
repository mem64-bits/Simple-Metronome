package com.github.mem64bits.simple.metronome.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;

public class FontCreator{
    // Main Big config object we modify
    private final FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

    public FontCreator() {
        params.color = Color.WHITE;
        params.mono = true; // Letters are equally spaced by default
    }


    /*Returning this allows for function chaining, like in C++*/
    public FontCreator withSize(int size) {
        params.size = size;
        return this;
    }

    public FontCreator withColor(Color color) {
        params.color = color;
        return this;
    }

    public FontCreator withShadow(int xOffset, int yOffset, Color shadowColor) {
        params.shadowOffsetX = xOffset;
        params.shadowOffsetY = yOffset;
        params.shadowColor = shadowColor;
        return this;
    }

    public BitmapFont createFont(String path) {
        FileHandle fontFile = Gdx.files.internal(path);
        if (!fontFile.exists()) {
            throw new IllegalArgumentException("Font file not found: " + path);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        BitmapFont font = generator.generateFont(params);
        generator.dispose();
        return font;
    }
}
