package com.yuli.netty.socket;
public class Message {
    private byte magic = 0x0079;
    private long length ;
    private String context;

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
