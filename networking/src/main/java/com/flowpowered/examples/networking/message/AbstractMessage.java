package com.flowpowered.examples.networking.message;

import com.flowpowered.networking.Message;

public class AbstractMessage implements Message {
    public boolean isAsync() {
        return false;
    }
}
