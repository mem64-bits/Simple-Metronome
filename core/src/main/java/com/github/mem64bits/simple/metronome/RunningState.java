package com.github.mem64bits.simple.metronome;

public class RunningState implements MetronomeState{
    @Override
    public void onEnter(MetronomeEngine engine){
        if(!engine.isActive())
            engine.setActive(true);
    }

    @Override
    public void onExit(MetronomeEngine engine){

    }

    @Override
    public void update(double deltaTime, MetronomeEngine engine){
        if (!engine.isActive()) return;
        engine.deltaAccumulate(deltaTime);

        final MetronomeSettings settings = engine.getSettings();
        long interval = (long) settings.getIntervalMillis();
        if (interval <= 0) return;

        engine.setCurrentTick( (long) (engine.getAccumulator() / interval));
        if (engine.getCurrentTick() > engine.getLastTick()) {
            final int totalTicksInMeasure = settings.signature().numerator() * settings.subdivision();
            final int tickMeasure = (int) (engine.getCurrentTick() % totalTicksInMeasure);

            boolean isAccent = (tickMeasure == 0);
            boolean isSub = (tickMeasure % settings.subdivision() != 0);

            engine.notifyListeners(new TickEvent(
                tickMeasure,
                engine.getCurrentTick(),
                settings.bpm(),
                isAccent,
                isSub
            ));

            engine.setLastTick(engine.getCurrentTick());
        }
    }

    @Override
    public MetronomeMode getType(){
        return MetronomeMode.RUNNING;
    }
}
