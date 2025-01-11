package biz.next_evolution.sandbox;

import java.util.ArrayList;
import java.util.List;

/**
 * performance check
 * for vs stream api
 */
public class SamplePerformance {

    public static void main(String[] args) {

        System.out.println("==============================");

        final int maxLoopCount = 1000000;
        final int divisionValue = 4;

        System.out.println("maxLoopCount = " + String.format("%,d", maxLoopCount));

        final List<Integer> sampleDataList = new ArrayList<>();
        for (int i = 1; i <= maxLoopCount; i++) {
            sampleDataList.add(i);
        }

        System.out.println("----- for");

        long startTime = System.currentTimeMillis();
        long sumResult = 0;
        for (Integer data : sampleDataList) {
            if (data % divisionValue == 0) {
                sumResult += data;
            }
        }
        System.out.println(sumResult);
        long endTime = System.currentTimeMillis();
        System.out.println("処理時間:" + (endTime - startTime) + "ms");

        System.out.println("----- stream");
        startTime = System.currentTimeMillis();

        final long sumStream = sampleDataList.stream()
                                      .filter(entry -> entry % divisionValue == 0)
                                      .mapToLong(entry -> entry)
                                      .sum();

        System.out.println(sumStream);
        endTime = System.currentTimeMillis();
        System.out.println("処理時間:" + (endTime - startTime) + "ms");

        System.out.println("----- parallelStream");


        startTime = System.currentTimeMillis();
        final long parallelStream = sampleDataList.parallelStream()
                                      .filter(entry -> entry % divisionValue == 0)
                                      .mapToLong(entry -> entry)
                                      .sum();
        System.out.println(parallelStream);
        endTime = System.currentTimeMillis();
        System.out.println("処理時間:" + (endTime - startTime) + "ms");

        System.out.println("==============================");
    }

}
