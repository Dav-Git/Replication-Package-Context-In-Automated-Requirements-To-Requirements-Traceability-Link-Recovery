## Config:
```json
{
  "cache_dir" : "./cache/GANNT",
  "gold_standard_configuration" : {
    "path" : "datasets/CM1-NASA/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "datasets/GANNT/low"
    }
  },
  "source_preprocessor" : {
    "name" : "paraphrase_openai",
    "args" : {
      "model" : "o4-mini-2025-04-16",
      "temperature" : "1",
      "count" : "0",
      "seed" : "133742243",
      "template" : "Here is the text of a software {artifact_type}:\n\n{content}\n\nParaphrase the requirement, retaining its original meaning.\nAnswer with the paraphrased text only, without any additional explanation or formatting.\n"
    }
  },
  "embedding_creator" : {
    "name" : "openai",
    "args" : {
      "model" : "text-embedding-3-large"
    }
  },
  "source_store" : {
    "name" : "custom",
    "args" : { }
  }
}
```
## Similarities:
Max similarity: 1.0 \
Identical vectors: 2 \
Min similarity: 0.02478930726647377 \
Range: 0.9752106927335262 \
Average similarity: 0.329242624001036 
![Histogram](analysis-GANNTo4-mini-2025-04-16-similarities.png)
## Identical vectors:
- d6_2.txtc and d8_1.txtc
- d12_6.txtc and d14_2.txtc
### Of which 0 are equivalent paraphrases.
 d3_3.txtp1 and d3_3.txtp2
- ! d1_2.txtp3 and d1_2.txtp7
- ! d1_3.txtp4 and d1_3.txtp7
- ! d7_1.txtp6 and d7_1.txtp7
- d12_6.txtp6 and d14_2.txtp4
- ! d17_2.txtp1 and d17_2.txtp5
- ! d12_6.txtp4 and d12_6.txtp7
- d6_3.txtp2 and d8_3.txtp3
- d12_6.txtp4 and d14_2.txtp1
- ! d1_4.txtp0 and d1_4.txtp2
- ! d14_2.txtp4 and d14_2.txtp6
- ! d12_6.txtp1 and d12_6.txtp2
- d12_6.txtp7 and d14_2.txtp6
- ! d15_2.txtp3 and d15_2.txtp7
- ! d13_4.txtp2 and d13_4.txtp3
- ! d1_1.txtp0 and d1_1.txtp6
- d12_6.txtp4 and d14_2.txtp2
- ! d2_2.txtp2 and d2_2.txtp3
- ! d3_3.txtp5 and d3_3.txtp6
- d6_2.txtp0 and d8_1.txtp0
- ! d7_2.txtp2 and d7_2.txtp5
- ! d1_4.txtp1 and d1_4.txtp4
- ! d3_3.txtp4 and d3_3.txtp6
- d6_3.txtp5 and d8_3.txtp2
- ! d7_2.txtp4 and d7_2.txtp7
- ! d13_1.txtp6 and d13_1.txtp7
- ! d17_2.txtp0 and d17_2.txtp5
- ! d7_2.txtp3 and d7_2.txtp6
- d12_6.txtp0 and d14_2.txtp3
- ! d3_1.txtp0 and d3_1.txtp3
- ! d12_6.txtp1 and d12_6.txtp7
- ! d6_6.txtp2 and d6_6.txtp7
- ! d10_7.txtp0 and d10_7.txtp6
- ! d7_2.txtp0 and d7_2.txtp6
- ! d9_1.txtp2 and d9_1.txtp5
- ! d10_7.txtp1 and d10_7.txtp3
- ! d9_1.txtp4 and d9_1.txtp7
- ! d17_3.txtp2 and d17_3.txtp6
- ! d11_2.txtp0 and d11_2.txtp5
- ! d10_7.txtp1 and d10_7.txtp5
- ! d3_3.txtp3 and d3_3.txtp6
- ! d6_6.txtp1 and d6_6.txtp6
- d12_6.txtp0 and d14_2.txtp0
- ! d4_2.txtp3 and d4_2.txtp5
- ! d12_6.txtp6 and d12_6.txtp7
- ! d17_4.txtp2 and d17_4.txtp6
- ! d3_2.txtp3 and d3_2.txtp4
- ! d6_6.txtp2 and d6_6.txtp5
- ! d8_2.txtp2 and d8_2.txtp7
- ! d17_1.txtp1 and d17_1.txtp3
- ! d1_4.txtp0 and d1_4.txtp5
- ! d3_3.txtp2 and d3_3.txtp7
- ! d10_7.txtp1 and d10_7.txtp7
- d12_6.txtp1 and d14_2.txtp4
- ! d12_6.txtp0 and d12_6.txtp3
- ! d14_2.txtp1 and d14_2.txtp7
- ! d7_2.txtp2 and d7_2.txtp6
- ! d3_2.txtp2 and d3_2.txtp6
- ! d9_1.txtp1 and d9_1.txtp6
- d12_6.txtp5 and d14_2.txtp5
- ! d2_2.txtp1 and d2_2.txtp2
- d12_6.txtp1 and d14_2.txtp6
- ! d6_6.txtp1 and d6_6.txtp2
- ! d7_2.txtp5 and d7_2.txtp7
- ! d1_4.txtp3 and d1_4.txtp4
- ! d3_3.txtp6 and d3_3.txtp7
- ! d6_6.txtp5 and d6_6.txtp7
- d6_2.txtp7 and d8_1.txtp7
- ! d17_4.txtp3 and d17_4.txtp5
- ! d17_3.txtp4 and d17_3.txtp5
- ! d9_1.txtp0 and d9_1.txtp7
- d10_3.txtp0 and d6_6.txtp0
- ! d14_2.txtp2 and d14_2.txtp7
- ! d12_1.txtp1 and d12_1.txtp4
- ! d3_3.txtp2 and d3_3.txtp6
- d10_3.txtp2 and d6_6.txtp3
- d6_2.txtp3 and d8_1.txtp3
- d12_6.txtp4 and d14_2.txtp4
- ! d14_2.txtp1 and d14_2.txtp2
- ! d17_2.txtp0 and d17_2.txtp7
- ! d13_4.txtp2 and d13_4.txtp4
- ! d13_1.txtp0 and d13_1.txtp7
- ! d12_6.txtp1 and d12_6.txtp6
- ! d1_4.txtp1 and d1_4.txtp3
- ! d6_1.txtp4 and d6_1.txtp7
- ! d11_2.txtp1 and d11_2.txtp5
- ! d3_2.txtp0 and d3_2.txtp6
- ! d9_2.txtp5 and d9_2.txtp7
- ! d5_2.txtp5 and d5_2.txtp7
- d12_6.txtp7 and d14_2.txtp7
- ! d4_2.txtp4 and d4_2.txtp5
- ! d5_3.txtp3 and d5_3.txtp4
- ! d5_3.txtp2 and d5_3.txtp4
- ! d3_2.txtp6 and d3_2.txtp7
- ! d10_3.txtp0 and d10_3.txtp2
- ! d12_6.txtp2 and d12_6.txtp6
- ! d11_3.txtp5 and d11_3.txtp7
- d12_6.txtp7 and d14_2.txtp1
- ! d9_1.txtp0 and d9_1.txtp2
- ! d11_1.txtp3 and d11_1.txtp7
- ! d5_2.txtp2 and d5_2.txtp7
- ! d14_2.txtp3 and d14_2.txtp5
- ! d4_2.txtp6 and d4_2.txtp7
- ! d12_6.txtp2 and d12_6.txtp4
- ! d7_2.txtp0 and d7_2.txtp2
- ! d7_2.txtp2 and d7_2.txtp3
- d12_6.txtc and d14_2.txtc
- ! d17_2.txtp1 and d17_2.txtp7
- ! d5_3.txtp2 and d5_3.txtp3
- ! d1_2.txtp2 and d1_2.txtp3
- d12_6.txtp2 and d14_2.txtp6
- ! d9_1.txtp5 and d9_1.txtp7
- ! d13_4.txtp1 and d13_4.txtp2
- ! d4_2.txtp3 and d4_2.txtp4
- ! d10_7.txtp5 and d10_7.txtp7
- ! d3_3.txtp1 and d3_3.txtp6
- ! d10_7.txtp0 and d10_7.txtp1
- ! d7_2.txtp4 and d7_2.txtp5
- ! d3_2.txtp0 and d3_2.txtp7
- ! d6_6.txtp0 and d6_6.txtp3
- ! d9_1.txtp4 and d9_1.txtp5
- ! d5_2.txtp2 and d5_2.txtp5
- d12_6.txtp4 and d14_2.txtp6
- ! d10_7.txtp0 and d10_7.txtp3
- ! d7_2.txtp3 and d7_2.txtp7
- d6_2.txtc and d8_1.txtc
- ! d6_6.txtp1 and d6_6.txtp7
- ! d17_1.txtp0 and d17_1.txtp4
- ! d5_2.txtp1 and d5_2.txtp7
- ! d7_2.txtp0 and d7_2.txtp7
- d6_2.txtp6 and d8_1.txtp6
- ! d11_2.txtp6 and d11_2.txtp7
- ! d3_3.txtp4 and d3_3.txtp5
- ! d14_2.txtp2 and d14_2.txtp4
- ! d14_2.txtp0 and d14_2.txtp5
- ! d6_1.txtp5 and d6_1.txtp6
- ! d7_2.txtp3 and d7_2.txtp5
- d6_2.txtp5 and d8_1.txtp5
- ! d14_2.txtp4 and d14_2.txtp7
- ! d9_1.txtp0 and d9_1.txtp4
- ! d1_4.txtp2 and d1_4.txtp5
- ! d13_1.txtp0 and d13_1.txtp6
- ! d17_3.txtp1 and d17_3.txtp6
- ! d7_2.txtp6 and d7_2.txtp7
- ! d6_6.txtp2 and d6_6.txtp6
- ! d10_7.txtp3 and d10_7.txtp5
- ! d3_2.txtp1 and d3_2.txtp7
- ! d7_2.txtp0 and d7_2.txtp5
- ! d7_3.txtp1 and d7_3.txtp6
- ! d3_2.txtp0 and d3_2.txtp2
- ! d3_2.txtp1 and d3_2.txtp2
- d12_6.txtp3 and d14_2.txtp3
- ! d3_3.txtp3 and d3_3.txtp5
- ! d7_2.txtp3 and d7_2.txtp4
- d12_6.txtp7 and d14_2.txtp4
- ! d10_7.txtp3 and d10_7.txtp7
- ! d3_1.txtp2 and d3_1.txtp7
- ! d11_2.txtp0 and d11_2.txtp1
- ! d7_2.txtp0 and d7_2.txtp4
- ! d14_2.txtp1 and d14_2.txtp4
- ! d6_6.txtp1 and d6_6.txtp5
- ! d6_6.txtp5 and d6_6.txtp6
- ! d3_2.txtp2 and d3_2.txtp7
- ! d12_6.txtp1 and d12_6.txtp4
- ! d9_2.txtp1 and d9_2.txtp6
- ! d3_3.txtp3 and d3_3.txtp4
- d12_6.txtp7 and d14_2.txtp2
- ! d1_2.txtp2 and d1_2.txtp7
- ! d7_1.txtp4 and d7_1.txtp6
- ! d17_3.txtp0 and d17_3.txtp7
- d12_6.txtp6 and d14_2.txtp6
- ! d3_3.txtp4 and d3_3.txtp7
- d12_6.txtp3 and d14_2.txtp5
- d12_6.txtp2 and d14_2.txtp2
- ! d17_2.txtp5 and d17_2.txtp7
- ! d10_6.txtp1 and d10_6.txtp4
- d12_6.txtp5 and d14_2.txtp3
- d12_6.txtp5 and d14_2.txtp0
- ! d13_4.txtp0 and d13_4.txtp4
- ! d17_2.txtp3 and d17_2.txtp6
- ! d10_7.txtp1 and d10_7.txtp6
- ! d5_2.txtp1 and d5_2.txtp5
- ! d7_2.txtp4 and d7_2.txtp6
- ! d12_6.txtp4 and d12_6.txtp6
- ! d9_2.txtp1 and d9_2.txtp3
- ! d3_2.txtp1 and d3_2.txtp6
- ! d13_4.txtp0 and d13_4.txtp3
- ! d13_4.txtp0 and d13_4.txtp1
- ! d10_7.txtp0 and d10_7.txtp7
- ! d3_2.txtp0 and d3_2.txtp1
- ! d12_6.txtp3 and d12_6.txtp5
- d12_6.txtp2 and d14_2.txtp1
- ! d10_7.txtp0 and d10_7.txtp5
- ! d12_3.txtp4 and d12_3.txtp5
- ! d3_2.txtp5 and d3_2.txtp6
- ! d5_3.txtp6 and d5_3.txtp7
- d12_6.txtp2 and d14_2.txtp7
- ! d3_4.txtp2 and d3_4.txtp6
- d12_6.txtp6 and d14_2.txtp7
- ! d3_3.txtp1 and d3_3.txtp4
- ! d17_2.txtp0 and d17_2.txtp1
- ! d3_2.txtp0 and d3_2.txtp5
- ! d13_4.txtp0 and d13_4.txtp2
- ! d12_6.txtp0 and d12_6.txtp5
- ! d9_1.txtp2 and d9_1.txtp7
- d12_6.txtp3 and d14_2.txtp0
- d12_6.txtp1 and d14_2.txtp1
- ! d13_4.txtp3 and d13_4.txtp4
- ! d3_3.txtp2 and d3_3.txtp5
- ! d3_3.txtp1 and d3_3.txtp5
- ! d9_3.txtp1 and d9_3.txtp3
- d6_2.txtp4 and d8_1.txtp4
- d12_6.txtp6 and d14_2.txtp2
- ! d7_3.txtp2 and d7_3.txtp4
- ! d10_7.txtp5 and d10_7.txtp6
- ! d14_2.txtp0 and d14_2.txtp3
- ! d8_3.txtp6 and d8_3.txtp7
- ! d14_2.txtp6 and d14_2.txtp7
- ! d3_3.txtp3 and d3_3.txtp7
- ! d7_2.txtp2 and d7_2.txtp7
- ! d7_2.txtp2 and d7_2.txtp4
- d12_6.txtp0 and d14_2.txtp5
- ! d3_2.txtp1 and d3_2.txtp5
- ! d15_2.txtp6 and d15_2.txtp7
- ! d9_2.txtp3 and d9_2.txtp6
- ! d2_2.txtp1 and d2_2.txtp3
- ! d14_2.txtp2 and d14_2.txtp6
- ! d10_7.txtp3 and d10_7.txtp6
- ! d7_1.txtp0 and d7_1.txtp1
- ! d3_3.txtp1 and d3_3.txtp7
- ! d3_3.txtp2 and d3_3.txtp3
- ! d7_1.txtp4 and d7_1.txtp7
- ! d3_1.txtp0 and d3_1.txtp6
- ! d3_2.txtp2 and d3_2.txtp5
- d12_6.txtp4 and d14_2.txtp7
- ! d5_2.txtp1 and d5_2.txtp2
- ! d17_3.txtp1 and d17_3.txtp2
- ! d12_4.txtp2 and d12_4.txtp5
- d12_6.txtp2 and d14_2.txtp4
- ! d14_2.txtp1 and d14_2.txtp6
- ! d6_6.txtp6 and d6_6.txtp7
- ! d9_1.txtp0 and d9_1.txtp5
- ! d7_2.txtp5 and d7_2.txtp6
- ! d10_7.txtp6 and d10_7.txtp7
- ! d15_2.txtp3 and d15_2.txtp6
- ! d10_5.txtp0 and d10_5.txtp4
- d12_6.txtp1 and d14_2.txtp7
- ! d3_3.txtp2 and d3_3.txtp4
- ! d7_1.txtp1 and d7_1.txtp3
- ! d15_2.txtp1 and d15_2.txtp2
- ! d7_2.txtp0 and d7_2.txtp3
- d10_3.txtp2 and d6_6.txtp0
- d10_3.txtp0 and d6_6.txtp3
- ! d12_6.txtp2 and d12_6.txtp7
- ! d3_2.txtp5 and d3_2.txtp7
- ! d13_4.txtp1 and d13_4.txtp3
- d6_2.txtp2 and d8_1.txtp2
- d12_6.txtp1 and d14_2.txtp2
- ! d7_1.txtp0 and d7_1.txtp3
- d6_2.txtp1 and d8_1.txtp1
- ! d3_1.txtp3 and d3_1.txtp6
- ! d3_3.txtp1 and d3_3.txtp3
- d12_6.txtp6 and d14_2.txtp1
- ! d13_4.txtp1 and d13_4.txtp4
- ! d3_3.txtp5 and d3_3.txtp7
### Of which 218 are equivalent paraphrases.
