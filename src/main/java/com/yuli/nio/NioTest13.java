package com.yuli.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NioTest13 {
    public static void main(String[] args) throws IOException {
        //创建两个文件
        String inputFile = "NioTest13_In.txt";
        String outputFile = "NioTest13_Out.txt";
        //创建随机文件读取类
        RandomAccessFile input = new RandomAccessFile(inputFile, "r");
        RandomAccessFile output = new RandomAccessFile(outputFile,"rw");

        long inputLength = new File(inputFile).length();
        FileChannel inputChannel = input.getChannel();
        FileChannel outputChannel = output.getChannel();

        MappedByteBuffer inputData = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);
        Charset charset = Charset.forName("utf-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        CharBuffer charBuffer = decoder.decode(inputData);
        ByteBuffer byteBuffer = encoder.encode(charBuffer);
        outputChannel.write(byteBuffer);
        input.close();
        output.close();

    }
}
