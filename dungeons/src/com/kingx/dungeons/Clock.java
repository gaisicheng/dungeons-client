package com.kingx.dungeons;

import java.util.LinkedList;
import java.util.List;

public class Clock implements Runnable {

    private volatile boolean init = true;
    private final boolean quit = false;
    private final List<Updateable> services = new LinkedList<Updateable>();

    public Clock(String name) {
        if (init) {
            new Thread(this, name).start();
            init = false;
        }
    }

    private final long BILLION = 1000000000;
    private long currentTime = System.nanoTime();

    private final float delta = .0166f;
    private final float maxStep = .25f;
    private float accumulator = 0.0f;
    private long clocks;
    private long initTime;

    @Override
    public void run() {
        initTime = System.currentTimeMillis();
        while (!quit) {
            long newTime = System.nanoTime();
            double frameTime = (double) (newTime - currentTime) / BILLION;
            currentTime = newTime;

            frameTime = Math.min(frameTime, maxStep); // note: max frame time to avoid spiral of death

            accumulator += frameTime;

            if (accumulator < delta) {
                if (App.NOSLEEP == null) {
                    try {
                        // Takes a nap for the remaining time.
                        Thread.sleep((long) ((delta - accumulator) * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                while (accumulator >= delta) {
                    for (Updateable service : services) {
                        service.update(delta);
                    }
                    accumulator -= delta;
                    clocks++;
                }

            }
        }
    }

    public void addService(Updateable service) {
        services.add(service);
    }

    public void removeService(Updateable service) {
        services.remove(service);
    }

    public long getClocks() {
        return clocks;
    }

    public float getFPS() {
        return clocks / ((System.currentTimeMillis() - initTime) / 1000f);
    }

}
