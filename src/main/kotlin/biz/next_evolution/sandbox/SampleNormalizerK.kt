package biz.next_evolution.sandbox

import java.text.Normalizer

class SampleNormalizerK {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var baseNameList: Array<String> = arrayOf("カナダ銀行 政策金利",
                                                      "全国消費者物価指数（ＣＰＩ、生鮮食料品・エネルギー除く)(前年同月比)",
                                                      "a b c d"
            )

            baseNameList.forEach { baseName ->
                println("---- Normalizer.normalize")
                println(baseName)
                println(Normalizer.normalize(baseName.replace("、", "")
                                                     .replace(" ", ""),
                                             Normalizer.Form.NFKC
                )
                )
            }
        }
    }
}