package com.github.mem64bits.simple.metronome;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MetronomeEngineTest {
    private TestListener listener;

    @BeforeEach
    void setUp() {
        listener = new TestListener();
    }

    /**
     * Helper to create an engine with a specific BPM
     */
    private MetronomeEngine createEngine(int bpm) {
        MetronomeSettings settings = new MetronomeSettings(
            bpm,
            new TimeSignature(4, 4),
            1
        );
        MetronomeEngine engine = new MetronomeEngine(settings);
        engine.addListener(listener);
        return engine;
    }

    @Test
    void testSlowBpm_60() {
        // 60 BPM = 1000ms per beat
        MetronomeEngine engine = createEngine(60);
        engine.start();

        engine.update(0); // T=0: Tick 0
        engine.update(0.5); // T=500ms: No tick
        engine.update(0.499); // T=999ms: No tick
        assertEquals(1, listener.tickCount, "At 999ms, only the start-up tick should have fired");

        engine.update(0.002); // T=1001ms: Tick 1
        assertEquals(2, listener.tickCount, "At 1001ms, the second tick (index 1) should fire");
    }

    @Test
    void testStandardBpm_120() {
        // 120 BPM = 500ms per beat
        MetronomeEngine engine = createEngine(120);
        engine.start();

        engine.update(0); // T=0ms: Tick 0
        engine.update(0.49); // T=490ms: No tick
        assertEquals(1, listener.tickCount);

        engine.update(0.02); // T=510ms: Tick 1
        assertEquals(2, listener.tickCount);

        engine.update(0.5); // T=1010ms: Tick 2
        assertEquals(3, listener.tickCount);
    }

    @Test
    void testFastBpm_240() {
        // 240 BPM = 250ms per beat
        MetronomeEngine engine = createEngine(240);
        engine.start();

        // Simulate 1 full second (should be 5 ticks: 0, 250, 500, 750, 1000)
        engine.update(0);
        for(int i=0; i<4; i++) {
            engine.update(0.25);
        }

        assertEquals(5, listener.tickCount, "240 BPM should produce 5 ticks in 1 second (including T=0)");
        assertEquals(4, listener.lastEvent.currentTick(), "Last global tick index should be 4");
    }

    @Test
    void testHighPrecisionBpm_144() {
        // 144 BPM = 416.666...ms per beat
        // This tests if 'double' accumulator prevents rounding drift
        MetronomeEngine engine = createEngine(144);
        engine.start();
        engine.update(0); // Tick 0

        // Advance 10 beats
        // Total time should be 4166.66... ms
        for(int i=0; i<10; i++) {
            engine.update(0.416666);
        }

        // We expect 11 ticks total (Start + 10 intervals)
        assertEquals(11, listener.tickCount, "Drift check: Should handle fractional intervals without missing ticks");
    }

    @Test
    void testBpmChangeMidStream() {
        // Start at 60 BPM (1000ms)
        MetronomeSettings settings = new MetronomeSettings(60, new TimeSignature(4, 4), 1);
        MetronomeEngine engine = new MetronomeEngine(settings);
        engine.addListener(listener);

        engine.start();
        engine.update(0); // Tick 0 (at 0ms)

        engine.update(0.5); // T=500ms
        assertEquals(1, listener.tickCount);

        // SUDDEN CHANGE: Double the speed to 120 BPM (500ms)
        // In a real app, you'd use a setter that updates the Record
        MetronomeSettings fastSettings = new MetronomeSettings(120, new TimeSignature(4, 4), 1);
        engine.setSettings(fastSettings);

        // At T=500ms, with the new 500ms interval, currentTick = 500 / 500 = 1.
        // Since 1 > lastTick(0), it should fire IMMEDIATELY.
        engine.update(0);

        assertEquals(2, listener.tickCount, "Engine should react immediately to BPM increase if the new threshold is met");
        assertEquals(1, listener.lastEvent.currentTick());
    }

    static class TestListener implements MetronomeListener {
        int tickCount = 0;
        TickEvent lastEvent;

        @Override
        public void onMetronomeTick(TickEvent tick) {
            tickCount++;
            lastEvent = tick;
        }
    }
}
