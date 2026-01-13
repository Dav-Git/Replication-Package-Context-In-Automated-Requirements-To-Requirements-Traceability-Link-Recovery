## Config:
```json
{
  "cache_dir" : "./cache/GANNT-low-gpt4o",
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
      "model" : "gpt-4o-2024-11-20",
      "temperature" : "1",
      "count" : "3",
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
Identical vectors: 147 \
Min similarity: 0.02478930726647377 \
Range: 0.9752106927335262 \
Average similarity: 0.34206279042043153 
![Histogram](analysis-GANNT-low-gpt4ogpt-4o-2024-11-20-similarities.png)
## Identical vectors:
- ! d3_2.txtp0 and d3_2.txtp2
- d12_6.txtp1 and d14_2.txtp2
- d12_6.txtp0 and d14_2.txtp2
- ! d1_3.txtp1 and d1_3.txtp2
- ! d7_2.txtp0 and d7_2.txtp2
- ! d1_4.txtp1 and d1_4.txtp2
- ! d7_1.txtp0 and d7_1.txtp2
- ! d12_1.txtp0 and d12_1.txtp1
- ! d14_1.txtp0 and d14_1.txtp1
- ! d3_4.txtp0 and d3_4.txtp1
- ! d5_1.txtp0 and d5_1.txtp2
- ! d5_1.txtp0 and d5_1.txtp1
- ! d12_6.txtp0 and d12_6.txtp1
- ! d7_2.txtp1 and d7_2.txtp2
- ! d4_2.txtp0 and d4_2.txtp2
- ! d11_1.txtp1 and d11_1.txtp2
- ! d14_2.txtp1 and d14_2.txtp2
- d12_6.txtp1 and d14_2.txtp0
- ! d11_1.txtp0 and d11_1.txtp2
- ! d10_6.txtp0 and d10_6.txtp2
- d12_6.txtp2 and d14_2.txtp2
- d6_2.txtp0 and d8_1.txtp2
- d12_6.txtp1 and d14_2.txtp1
- ! d17_2.txtp0 and d17_2.txtp1
- ! d3_3.txtp1 and d3_3.txtp2
- ! d5_4.txtp0 and d5_4.txtp2
- ! d13_4.txtp1 and d13_4.txtp2
- ! d4_1.txtp1 and d4_1.txtp2
- ! d17_3.txtp1 and d17_3.txtp2
- ! d10_3.txtp0 and d10_3.txtp1
- ! d17_2.txtp1 and d17_2.txtp2
- ! d1_4.txtp0 and d1_4.txtp2
- ! d9_3.txtp1 and d9_3.txtp2
- ! d9_4.txtp1 and d9_4.txtp2
- ! d14_3.txtp1 and d14_3.txtp2
- ! d17_1.txtp1 and d17_1.txtp2
- ! d3_1.txtp0 and d3_1.txtp2
- d6_2.txtc and d8_1.txtc
- ! d6_6.txtp0 and d6_6.txtp2
- d12_6.txtp0 and d14_2.txtp0
- ! d7_3.txtp0 and d7_3.txtp1
- ! d14_3.txtp0 and d14_3.txtp2
- ! d8_2.txtp0 and d8_2.txtp1
- d12_6.txtp2 and d14_2.txtp0
- ! d10_7.txtp0 and d10_7.txtp2
- ! d2_3.txtp0 and d2_3.txtp2
- ! d13_3.txtp0 and d13_3.txtp1
- d6_2.txtp0 and d8_1.txtp0
- ! d15_2.txtp0 and d15_2.txtp1
- d12_6.txtp2 and d14_2.txtp1
- ! d12_3.txtp0 and d12_3.txtp1
- ! d5_2.txtp0 and d5_2.txtp2
- ! d12_3.txtp1 and d12_3.txtp2
- ! d12_6.txtp0 and d12_6.txtp2
- ! d3_4.txtp1 and d3_4.txtp2
- ! d7_3.txtp1 and d7_3.txtp2
- ! d17_4.txtp1 and d17_4.txtp2
- ! d1_4.txtp0 and d1_4.txtp1
- ! d1_1.txtp0 and d1_1.txtp2
- ! d2_2.txtp1 and d2_2.txtp2
- ! d10_3.txtp1 and d10_3.txtp2
- ! d6_3.txtp1 and d6_3.txtp2
- ! d8_1.txtp0 and d8_1.txtp2
- ! d5_4.txtp0 and d5_4.txtp1
- d10_3.txtp0 and d6_6.txtp1
- d12_6.txtp0 and d14_2.txtp1
- ! d3_1.txtp1 and d3_1.txtp2
- ! d5_2.txtp1 and d5_2.txtp2
- ! d10_1.txtp1 and d10_1.txtp2
- ! d3_1.txtp0 and d3_1.txtp1
- ! d11_1.txtp0 and d11_1.txtp1
- ! d10_2.txtp0 and d10_2.txtp1
- ! d3_2.txtp0 and d3_2.txtp1
- ! d6_3.txtp0 and d6_3.txtp1
- ! d3_3.txtp0 and d3_3.txtp2
- ! d13_1.txtp0 and d13_1.txtp2
- ! d7_1.txtp0 and d7_1.txtp1
- ! d3_4.txtp0 and d3_4.txtp2
- ! d14_2.txtp0 and d14_2.txtp1
- ! d10_3.txtp0 and d10_3.txtp2
- ! d12_6.txtp1 and d12_6.txtp2
- ! d3_3.txtp0 and d3_3.txtp1
- ! d2_1.txtp0 and d2_1.txtp2
- ! d10_6.txtp1 and d10_6.txtp2
- ! d16_3.txtp1 and d16_3.txtp2
- ! d12_1.txtp1 and d12_1.txtp2
- ! d4_2.txtp1 and d4_2.txtp2
- ! d6_2.txtp0 and d6_2.txtp2
- ! d10_6.txtp0 and d10_6.txtp1
- ! d15_1.txtp1 and d15_1.txtp2
- ! d12_1.txtp0 and d12_1.txtp2
- ! d7_2.txtp0 and d7_2.txtp1
- d6_2.txtp2 and d8_1.txtp2
- ! d6_5.txtp1 and d6_5.txtp2
- ! d6_3.txtp0 and d6_3.txtp2
- d10_3.txtp2 and d6_6.txtp1
- ! d13_2.txtp0 and d13_2.txtp1
- ! d3_2.txtp1 and d3_2.txtp2
- d10_3.txtp1 and d6_6.txtp1
- ! d9_4.txtp0 and d9_4.txtp1
- ! d7_1.txtp1 and d7_1.txtp2
- ! d13_2.txtp1 and d13_2.txtp2
- ! d12_3.txtp0 and d12_3.txtp2
- ! d17_1.txtp0 and d17_1.txtp1
- ! d17_1.txtp0 and d17_1.txtp2
- ! d17_3.txtp0 and d17_3.txtp1
- ! d13_1.txtp0 and d13_1.txtp1
- ! d2_3.txtp1 and d2_3.txtp2
- ! d12_2.txtp0 and d12_2.txtp2
- ! d5_2.txtp0 and d5_2.txtp1
- ! d4_4.txtp0 and d4_4.txtp2
- ! d5_3.txtp1 and d5_3.txtp2
- ! d6_5.txtp0 and d6_5.txtp1
- ! d5_4.txtp1 and d5_4.txtp2
- ! d13_1.txtp1 and d13_1.txtp2
- ! d10_7.txtp1 and d10_7.txtp2
- ! d9_4.txtp0 and d9_4.txtp2
- ! d7_3.txtp0 and d7_3.txtp2
- ! d11_2.txtp1 and d11_2.txtp2
- ! d5_1.txtp1 and d5_1.txtp2
- ! d10_4.txtp0 and d10_4.txtp2
- ! d8_2.txtp1 and d8_2.txtp2
- ! d13_2.txtp0 and d13_2.txtp2
- ! d6_5.txtp0 and d6_5.txtp2
- ! d9_2.txtp1 and d9_2.txtp2
- ! d12_5.txtp0 and d12_5.txtp2
- ! d16_3.txtp0 and d16_3.txtp1
- ! d6_1.txtp0 and d6_1.txtp2
- d6_2.txtp2 and d8_1.txtp0
- ! d10_5.txtp1 and d10_5.txtp2
- ! d6_1.txtp0 and d6_1.txtp1
- ! d15_2.txtp0 and d15_2.txtp2
- ! d14_3.txtp0 and d14_3.txtp1
- ! d2_2.txtp0 and d2_2.txtp1
- ! d8_2.txtp0 and d8_2.txtp2
- ! d6_1.txtp1 and d6_1.txtp2
- ! d10_7.txtp0 and d10_7.txtp1
- ! d15_2.txtp1 and d15_2.txtp2
- ! d8_3.txtp0 and d8_3.txtp2
- ! d2_2.txtp0 and d2_2.txtp2
- ! d16_3.txtp0 and d16_3.txtp2
- ! d4_2.txtp0 and d4_2.txtp1
- d12_6.txtc and d14_2.txtc
- ! d14_2.txtp0 and d14_2.txtp2
- ! d17_2.txtp0 and d17_2.txtp2
- ! d17_3.txtp0 and d17_3.txtp2
- ! d2_3.txtp0 and d2_3.txtp1
### Of which 129 are equivalent paraphrases.
