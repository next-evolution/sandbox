package biz.next_evolution.sandbox;

import java.text.Normalizer;
import java.util.Arrays;

public class SampleNormalizer {

    public static void main(String[] args) {
        String[] baseNameList = new String[]{"カナダ銀行 政策金利",
                                             "全国消費者物価指数（ＣＰＩ、生鮮食料品・エネルギー除く)(前年同月比)",
                                             "a b c d"};

        Arrays.stream(baseNameList).forEach(baseName -> {
            System.out.println("---- Normalizer.normalize");
            System.out.println(baseName);
            System.out.println(Normalizer.normalize(baseName.replaceAll("、", "")
                                                            .replaceAll(" ", ""),
                                                    Normalizer.Form.NFKC));
        });
    }

}
