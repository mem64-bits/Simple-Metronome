package com.github.mem64bits.simple.metronome.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.mem64bits.simple.metronome.commands.*;
import com.github.mem64bits.simple.metronome.input.*;
import com.github.mem64bits.simple.metronome.internals.*;
import com.github.mem64bits.simple.metronome.states.MetronomeState;
import com.github.mem64bits.simple.metronome.ui.FontCreator;
import com.github.mem64bits.simple.metronome.listeners.AudioHandler;
import com.github.mem64bits.simple.metronome.sound.*;

import java.io.IOException;
import java.util.Optional;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MetronomeApp extends ApplicationAdapter {
    // Main Control Center for Metronome Timing Logic
    MetronomeEngine engine;

    // Uses State Pattern to tell the engine what state App is in
    MetronomeState currentState;

    // Database managing multiple different sounds, loaded from json or manually
    SoundRegistry soundRegistry;

    // Specialized Observer detecting when to play audio
    AudioHandler audioHandler;
    InputBinder inputBinder;

    Viewport viewport;
    Camera camera;

    Texture codecTexture;
    SpriteBatch batch;
    BitmapFont font;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(400, 300, camera);

        codecTexture = new Texture(Gdx.files.internal("mgscodec/FACE_0754.bmp"));
        codecTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch = new SpriteBatch();

        font = new FontCreator()
            .withSize(17)
            .withColor(Color.LIGHT_GRAY)
            .withShadow(2, 2, Color.BLACK)
            .createFont("fonts/MGS1 Codec.ttf");

        font.getRegion().getTexture().setFilter(
            Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest
        );

        soundRegistry = new SoundRegistry();
        try{
            soundRegistry.loadFromJson(Gdx.files.internal(
                "config/soundpacks.json").file().toPath()
            );

        } catch(IOException e){
            System.err.println("Failed to load sound config from JSON: "+e.getMessage());

            // Fallback to hardcoded default soundpack if config fails
            MetronomeSound defaultBeat = MetronomeSound.load(
                "default",
                "sounds/default/high.wav",
                "sounds/default/low.wav",
                Optional.of(0) // Default baseBpm
            );
            soundRegistry.registerSound(defaultBeat.getSoundID(), defaultBeat);
        }
        // loads sound from JSON and plays sound onMetrotomeTick()
        audioHandler = new AudioHandler(soundRegistry, "reece");

        MetronomeSettings settings = new MetronomeSettings(120, new TimeSignature(4,4), 1);
        engine = new MetronomeEngine(settings);
        engine.addListener(audioHandler);
        CommandManager cmdMgr = engine.getCmdMgr();

        try{
            cmdMgr.loadFromJson(Gdx.files.internal("config/app_commands.json").file().toPath());
        } catch(IOException e){
            System.err.println("Could not load App Commands from json config: "
            +e.getMessage()+"\n");
            System.err.println("Fallback to default command hashkey binds");

            // Binds Command Patterns to a Key stored in a hashmap registry
            cmdMgr.registerCommand("TOGGLE_PAUSE", new PauseCommand());
            cmdMgr.registerCommand("BPM_UP", new IncreaseBpm(1));
            cmdMgr.registerCommand("BPM_DOWN", new DecreaseBpm(1));
            cmdMgr.registerCommand("QUIT_APP", new QuitApp());
        }


        // Binds input to a specific command pattern referenced to by a given hashkey
        inputBinder = new InputBinder();

        try{
            inputBinder.loadFromJson(Gdx.files.internal("config/keybinds.json").file().toPath());
        } catch(IOException e){
            System.err.println("Could not load keybinds from json config: "
                +e.getMessage()+"\n");
            System.err.println("Fallback to default keybinds");

            inputBinder.bind(Input.Keys.SPACE, "TOGGLE_PAUSE");
            inputBinder.bind(Input.Keys.UP, "BPM_UP");
            inputBinder.bind(Input.Keys.DOWN, "BPM_DOWN");
            inputBinder.bind(Input.Keys.ESCAPE, "QUIT_APP");
        }
        Gdx.input.setInputProcessor(new InputHandler(inputBinder, engine));

        engine.start();
        System.out.println("Starting Metronome: "+settings.bpm()+"BPM"+ " Time Signature "+settings.signature());
    }
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Updates engine logic every frame
        engine.update(deltaTime);
        System.out.print("\r" + "Current BPM: " + engine.getSettings().bpm());
        ScreenUtils.clear(0, 0, 0, 1);

        // Apply Viewport to scale everything inside to 400x300
        viewport.apply();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float x = (viewport.getWorldWidth() - codecTexture.getWidth()) / 2.0f;
        float y = (viewport.getWorldHeight() - codecTexture.getHeight()) / 2.0f;

        batch.draw(codecTexture, x, y);
        font.draw(batch, "SNAKE", x + 5, y + codecTexture.getHeight() - 5);

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
        font.dispose();
    }
}
