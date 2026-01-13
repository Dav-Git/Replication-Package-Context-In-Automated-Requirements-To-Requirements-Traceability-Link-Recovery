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
      "count" : "1",
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
Identical vectors: 4 \
Min similarity: 0.012120122089982033 \
Range: 0.987879877910018 \
Average similarity: 0.33181415904755884 
![Histogram](analysis-GANNTo4-mini-2025-04-16-similarities1.png)
## Identical vectors:
- d6_2.txtc and d8_1.txtc
- d12_6.txtp0 and d14_2.txtp0
- d12_6.txtc and d14_2.txtc
- d6_2.txtp0 and d8_1.txtp0
### Of which 0 are equivalent paraphrases.
0 are equivalent paraphrases.
