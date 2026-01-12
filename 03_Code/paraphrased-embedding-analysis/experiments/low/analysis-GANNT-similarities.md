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
      "path" : "datasets/GANNT/low"
    }
  },
  "source_preprocessor" : {
    "name" : "artifact",
    "args" : {
      "model" : ""
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
Min similarity: 0.02477550134062767 \
Range: 0.9752244986593723 \
Average similarity: 0.32923409177779595 
![Histogram](analysis-GANNT-similarities.png)
## Identical vectors:
- d8_1.txt and d6_2.txt
- d14_2.txt and d12_6.txt
### Of which 0 are equivalent paraphrases.
