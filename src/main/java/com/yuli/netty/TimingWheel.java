package com.yuli.netty;

import io.netty.util.HashedWheelTimer;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class TimingWheel {
    public static void main(String[] args) {
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS,16);
        hashedWheelTimer.newTimeout((timeout) -> {
            System.out.println(timeout);
            System.out.println(LocalTime.now());
        },3,TimeUnit.SECONDS);
    }
}
