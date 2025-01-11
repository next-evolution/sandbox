package biz.next_evolution.sandbox;

import java.util.ArrayList;
import java.util.List;

/**
 * performance check
 * for vs stream api
 */
public class SamplePerformance {

    public static void main(String[] args) {

        final int maxLoopCount = 1000000;

        final List<Integer> dataSet = new ArrayList<>();
        for (int i = 1; i <= maxLoopCount; i++) {
            dataSet.add(i);
        }

        System.out.println("===============");

        System.out.println("開始:for");
        long startTime = System.currentTimeMillis();
        long sumFor = 0;
        for (Integer data : dataSet) {
            if (data % 3 == 0) {
                sumFor += data;
            }
        }
        System.out.println(sumFor);
        long endTime = System.currentTimeMillis();
        System.out.println("終了:for");
        System.out.println("処理時間:" + (endTime - startTime) + "ms");


        System.out.println("===============");
        System.out.println("開始:stream");
        startTime = System.currentTimeMillis();

        final long sumStream = dataSet.parallelStream()
                                      .filter(entry -> entry % 3 == 0)
                                      .mapToLong(entry -> entry)
                                      .sum();

        System.out.println(sumStream);
        endTime = System.currentTimeMillis();
        System.out.println("終了:stream");
        System.out.println("処理時間:" + (endTime - startTime) + "ms");

        System.out.println("===============");

        System.out.println("開始:parallelStream");
        startTime = System.currentTimeMillis();
        final long parallelStream = dataSet.parallelStream()
                                      .filter(entry -> entry % 3 == 0)
                                      .mapToLong(entry -> entry)
                                      .sum();
        System.out.println(parallelStream);
        endTime = System.currentTimeMillis();
        System.out.println("終了:stream");
        System.out.println("処理時間:" + (endTime - startTime) + "ms");

        System.out.println("===============");
    }

}
