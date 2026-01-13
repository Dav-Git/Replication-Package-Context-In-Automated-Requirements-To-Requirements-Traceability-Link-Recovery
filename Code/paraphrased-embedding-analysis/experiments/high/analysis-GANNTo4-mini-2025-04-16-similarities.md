## Config:
```json
{
  "cache_dir" : "./cache/GANNT",
  "gold_standard_configuration" : {
    "path" : "datasets/GANNT/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "datasets/GANNT/high"
    }
  },
  "source_preprocessor" : {
    "name" : "paraphrase_openai",
    "args" : {
      "model" : "o4-mini-2025-04-16",
      "temperature" : "1.0",
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
Min similarity: 0.0538291335105896 \
Range: 0.9461708664894104 \
Average similarity: 0.38463786031115726 
![Histogram](analysis-GANNTo4-mini-2025-04-16-similarities.png)
## Identical vectors:
- ! r7.txtp4 and r7.txtp2
- ! r7.txtp3 and r7.txtp2
- ! r7.txtp4 and r7.txtp3
- ! r11.txtp2 and r11.txtp1
### Of which 4 are equivalent paraphrases.
