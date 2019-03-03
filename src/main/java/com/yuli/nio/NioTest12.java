package com.yuli.nio;

import java.io.IOException;
import java.nio.channels.Selector;

public class NioTest12 {
    public static void main(String[] args) throws IOException {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[0] = 5001;
        ports[0] = 5002;
        ports[0] = 5003;
        ports[0] = 5004;

        Selector selector = Selector.open();
    }
}
