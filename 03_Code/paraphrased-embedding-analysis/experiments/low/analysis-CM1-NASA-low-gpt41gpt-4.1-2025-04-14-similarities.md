## Config:
```json
{
  "cache_dir" : "./cache/CM1-NASA-low-gpt41",
  "gold_standard_configuration" : {
    "path" : "datasets/CM1-NASA/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "datasets/CM1-NASA/low"
    }
  },
  "source_preprocessor" : {
    "name" : "paraphrase_openai",
    "args" : {
      "model" : "gpt-4.1-2025-04-14",
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
Identical vectors: 39 \
Min similarity: 0.0706789568066597 \
Range: 0.9293210431933403 \
Average similarity: 0.3714162205185781 
![Histogram](analysis-CM1-NASA-low-gpt41gpt-4.1-2025-04-14-similarities.png)
## Identical vectors:
- ! DPUSDS5.12.3.1p0 and DPUSDS5.12.3.1p1
- ! DPUSDS5.13.0.1p0 and DPUSDS5.13.0.1p2
- ! DPUSDS5.12.2.1p1 and DPUSDS5.12.2.1p2
- ! DPUSDS5.12.1.4.1p0 and DPUSDS5.12.1.4.1p1
- ! DPUSDS5.12.1.3.1p0 and DPUSDS5.12.1.3.1p2
- ! DPUSDS5.12.1.4.1p1 and DPUSDS5.12.1.4.1p2
- ! DPUSDS5.13.1.5.2p0 and DPUSDS5.13.1.5.2p2
- ! DPUSDS5.13.2.3p0 and DPUSDS5.13.2.3p2
- ! DPUSDS5.12.1.2.2p0 and DPUSDS5.12.1.2.2p1
- ! DPUSDS5.12.1.2.2p1 and DPUSDS5.12.1.2.2p2
- ! DPUSDS5.13.1.1.2p0 and DPUSDS5.13.1.1.2p1
- ! DPUSDS5.13.1.7.1p1 and DPUSDS5.13.1.7.1p2
- ! DPUSDS5.12.1.4.1p0 and DPUSDS5.12.1.4.1p2
- ! DPUSDS5.13.2.3p1 and DPUSDS5.13.2.3p2
- ! DPUSDS5.13.0.1p1 and DPUSDS5.13.0.1p2
- ! DPUSDS5.12.2.4p0 and DPUSDS5.12.2.4p1
- ! DPUSDS5.12.1.2.4p0 and DPUSDS5.12.1.2.4p2
- ! DPUSDS5.12.1.5.2p0 and DPUSDS5.12.1.5.2p2
- ! DPUSDS5.13.0.1p0 and DPUSDS5.13.0.1p1
- ! DPUSDS5.13.2.3p0 and DPUSDS5.13.2.3p1
- ! DPUSDS5.13.1.7.1p0 and DPUSDS5.13.1.7.1p1
- ! DPUSDS5.12.1.4.2p0 and DPUSDS5.12.1.4.2p1
- ! DPUSDS5.13.2.5p0 and DPUSDS5.13.2.5p2
- ! DPUSDS5.13.1.1.2p1 and DPUSDS5.13.1.1.2p2
- ! DPUSDS5.13.2.7p0 and DPUSDS5.13.2.7p2
- ! DPUSDS5.12.2.2p0 and DPUSDS5.12.2.2p2
- ! DPUSDS5.13.1.7.1p0 and DPUSDS5.13.1.7.1p2
- ! DPUSDS5.13.1.4.1p1 and DPUSDS5.13.1.4.1p2
- ! DPUSDS5.12.1.5.6p1 and DPUSDS5.12.1.5.6p2
- ! DPUSDS5.12.1.2.4p1 and DPUSDS5.12.1.2.4p2
- ! DPUSDS5.13.2.1p0 and DPUSDS5.13.2.1p2
- ! DPUSDS5.12.1.2.2p0 and DPUSDS5.12.1.2.2p2
- ! DPUSDS5.13.2.6p1 and DPUSDS5.13.2.6p2
- ! DPUSDS5.13.1.5.3p0 and DPUSDS5.13.1.5.3p2
- ! DPUSDS5.13.1.1.2p0 and DPUSDS5.13.1.1.2p2
- ! DPUSDS5.12.1.2.4p0 and DPUSDS5.12.1.2.4p1
- ! DPUSDS5.12.2.3p0 and DPUSDS5.12.2.3p2
- ! DPUSDS5.13.0.2p0 and DPUSDS5.13.0.2p1
- ! DPUSDS5.13.1.2.1p0 and DPUSDS5.13.1.2.1p2
### Of which 39 are equivalent paraphrases.
