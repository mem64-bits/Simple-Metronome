package com.github.mem64bits.simple.metronome;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MetronomeApp extends ApplicationAdapter {
    // Main Control Center for Metronome Timing Logic
    MetronomeEngine engine;

    // Uses State Pattern to tell the engine what state App is in
    MetronomeState currentState;

    // Database managing multiple different sounds
    SoundRegistry soundRegistry;

    // Specialized Observer detecting when to play audio
    AudioHandler audioHandler;
    ScreenFlasher flasher;

    Viewport viewport;
    Camera camera;

    Texture codecTexture;
    SpriteBatch batch;



    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(400, 300, camera);

        codecTexture = new Texture(Gdx.files.internal("mgscodec/FACE_0754.bmp"));
        codecTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch = new SpriteBatch();

        soundRegistry = new SoundRegistry();
        MetronomeSound defaultBeat = new MetronomeSound(
            "default",
            Gdx.audio.newSound(Gdx.files.internal("sounds/default/high.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/default/low.wav"))
        );

        soundRegistry.registerSound(defaultBeat.name, defaultBeat);

        MetronomeSettings settings = new MetronomeSettings(120, new TimeSignature(4,4), 1);
        engine = new MetronomeEngine(settings);
        audioHandler = new AudioHandler(soundRegistry, "default");
        flasher = new ScreenFlasher();

        engine.addListener(audioHandler);
        engine.addListener(flasher);
        engine.start();
        System.out.println("Starting Metronome: "+settings.bpm()+"BPM"+ " Time Signature "+settings.signature());
    }


    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        engine.update(deltaTime);
        flasher.update(deltaTime);

        Color bg = flasher.getColour();
        ScreenUtils.clear(bg.r, bg.g, bg.b, bg.a);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        float x = (viewport.getWorldWidth() - codecTexture.getWidth()) / 2.0f;
        float y = (viewport.getWorldHeight() - codecTexture.getHeight()) / 2.0f;
        batch.draw(codecTexture, x, y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void dispose() {
        batch.dispose();
        codecTexture.dispose();
        soundRegistry.disposeAll();

    }
}
