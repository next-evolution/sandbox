package com.example.demo;

public class CommonValue {

    public static long SeqNo;

    public static Long getSeqNo(){
        SeqNo = SeqNo + 1;
        return SeqNo;
    }

    public static void init(){
        SeqNo = 0L;
    }
}
