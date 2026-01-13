## Config:
```json
{
  "cache_dir" : "./cache/CM1-NASA",
  "gold_standard_configuration" : {
    "path" : "datasets/CM1-NASA/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "datasets/CM1-NASA/high"
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
Identical vectors: 8 \
Min similarity: 0.29307809472084045 \
Range: 0.7069219052791595 \
Average similarity: 0.5269483417693096 
![Histogram](analysis-CM1-NASAo4-mini-2025-04-16-similarities.png)
## Identical vectors:
- ! SRS5.13.1.1p3 and SRS5.13.1.1p4
- ! SRS5.12.2.1p0 and SRS5.12.2.1p3
- ! SRS5.13.3.2p1 and SRS5.13.3.2p4
- ! SRS5.12.3.2p0 and SRS5.12.3.2p4
- ! SRS5.13.1.1p0 and SRS5.13.1.1p1
- ! SRS5.13.3.2p2 and SRS5.13.3.2p3
- ! SRS5.12.4.1p0 and SRS5.12.4.1p3
- ! SRS5.13.1.4p0 and SRS5.13.1.4p4
### Of which 8 are equivalent paraphrases.
