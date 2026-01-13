## Configuration (2025-11-05_14-52+0100 -- config.json_2e60d7b3-d99b-3812-82ec-2336a23cc14b)
```json
{
  "cache_dir" : "./cache-r2r/WARC-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/WARC/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/WARC/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/WARC/low"
    }
  },
  "source_preprocessor" : {
    "name" : "artifact",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "artifact",
    "args" : { }
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
  },
  "target_store" : {
    "name" : "cosine_similarity",
    "args" : {
      "max_results" : "4"
    }
  },
  "classifier" : {
    "name" : "reasoning_openai",
    "args" : {
      "model" : "gpt-4o-2024-08-06",
      "seed" : "133742243",
      "temperature" : "0.0",
      "prompt" : "Below are two artifacts from the same software system. Is there a traceability link between (1) and (2)? Give your reasoning and then answer with 'yes' or 'no' enclosed in <trace> </trace>.\n (1) {source_type}: '''{source_content}''' \n (2) {target_type}: '''{target_content}''' ",
      "use_original_artifacts" : "false",
      "use_system_message" : "true"
    }
  },
  "result_aggregator" : {
    "name" : "any_connection",
    "args" : {
      "source_granularity" : "0",
      "target_granularity" : "0"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "req2req",
    "args" : { }
  }
}
```

## Stats
* #TraceLinks (GS): 136
* #Source Artifacts: 63
* #Target Artifacts: 89
## Results
* True Positives: 87
* False Positives: 79
* False Negatives: 49
* Precision: 0.5240963855421686
* Recall: 0.6397058823529411
* F1: 0.576158940397351
