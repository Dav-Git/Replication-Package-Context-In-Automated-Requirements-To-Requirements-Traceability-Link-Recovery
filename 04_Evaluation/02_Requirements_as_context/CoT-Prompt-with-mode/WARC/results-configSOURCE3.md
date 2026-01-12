## LissaConfiguration (2025-11-16_18-27+0100 -- config.json_96e4f878-5782-3415-8e44-f89ede4a6f7d)
```json
{
  "cache_dir" : "./cache-r2r/WARC-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/WARC/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false,
    "name" : "gold_standard"
  },
  "context_configurations" : [ {
    "name" : "embeddings",
    "context_id" : "requirements",
    "modules" : {
      "ARTIFACT_PROVIDER" : [ {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/WARC/high"
        }
      } ],
      "PREPROCESSOR" : [ {
        "name" : "artifact",
        "args" : { }
      } ],
      "EMBEDDING_CREATOR" : [ {
        "name" : "openai",
        "args" : {
          "model" : "text-embedding-3-large"
        }
      } ],
      "ELEMENT_STORE" : [ {
        "name" : "custom",
        "args" : {
          "max_results" : "3"
        }
      } ]
    }
  } ],
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
    "name" : "embeddings-as-context_openai",
    "args" : {
      "model" : "gpt-4o-2024-08-06",
      "context_id" : "requirements",
      "search_similar_to" : "SOURCE",
      "template" : "Below are artifacts from the same software system. Is there a traceability link between (a) and (b)?\nConsider that the numbered {similar_to} {context_type} also exist in the same software system.\nGive your reasoning, then answer with 'yes' or 'no' enclosed in <trace></trace>.\n\n(a) {source_type}: ```{source_content}```\n(b) {target_type}: ```{target_content}```\nContext:\n{context_content}",
      "seed" : "133742243",
      "temperature" : "0.0"
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
  },
  "name" : "LiSSA"
}
```

## Stats
* #TraceLinks (GS): 136
* #Source Artifacts: 63
* #Target Artifacts: 89
## Results
* True Positives: 82
* False Positives: 53
* False Negatives: 54
* Precision: 0.6074074074074074
* Recall: 0.6029411764705882
* F1: 0.6051660516605166
