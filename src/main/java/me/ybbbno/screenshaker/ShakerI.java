package me.ybbbno.screenshaker;

import java.util.Random;

public class ShakerI {
    private final Random random = new Random();

    private final float minYaw;
    private final float maxYaw;
    private final float minPitch;
    private final float maxPitch;
    private int ticks;

    public ShakerI(float minYaw, float maxYaw, float minPitch, float maxPitch, int ticks) {
        this.minYaw = minYaw;
        this.maxYaw = maxYaw;
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        this.ticks = ticks;
    }

    public float yaw() {
        float spreadMin = 0;
        float spreadMax = 0;

        if (ticks % 2 == 0) {
            spreadMin = minYaw;
        } else {
            spreadMax = maxYaw;
        }

        return random.nextFloat(spreadMin, spreadMax);
    }

    public float pitch() {
        float spreadMin = 0;
        float spreadMax = 0;

        if (ticks % 2 == 0) {
            spreadMin = minPitch;
        } else {
            spreadMax = maxPitch;
        }

        return random.nextFloat(spreadMin, spreadMax);
    }

    public int ticks() {
        return ticks;
    }

    public void add(int ticks) {
        this.ticks += ticks;
    }
}
