## LissaConfiguration (2025-11-20_14-07+0100 -- config.json_266e7560-f13b-3fe4-a64e-ff930f784d43)
```json
{
  "cache_dir" : "./cache-r2r/CM1-NASA-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/CM1-NASA/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false,
    "name" : "gold_standard"
  },
  "context_configurations" : [ {
    "name" : "embeddings",
    "context_id" : "target_requirements",
    "modules" : {
      "ARTIFACT_PROVIDER" : [ {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/CM1-NASA/low"
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
          "max_results" : "2"
        }
      } ]
    }
  }, {
    "name" : "embeddings",
    "context_id" : "source_requirements",
    "modules" : {
      "ARTIFACT_PROVIDER" : [ {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/CM1-NASA/high"
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
          "max_results" : "5"
        }
      } ]
    }
  } ],
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/CM1-NASA/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/CM1-NASA/low"
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
      "query_context" : "source_requirements",
      "search_context" : "target_requirements",
      "search_mode" : "TARGET",
      "seed" : "133742243",
      "temperature" : "0.0",
      "template" : "Below are artifacts from the same software system. Is there a traceability link between (a) and (b)?\nConsider that the numbered {search_mode} {context_type} also exist in the same software system.\nGive your reasoning, then answer with 'yes' or 'no' enclosed in <trace></trace>.\n\n(a) {source_type}: ```{source_content}```\n(b) {target_type}: ```{target_content}```\n\nContext:\n{context_content}\n"
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
    "name" : "identity",
    "args" : { }
  },
  "name" : "LiSSA"
}
```

## Stats
* #TraceLinks (GS): 45
* #Source Artifacts: 22
* #Target Artifacts: 53
## Results
* True Positives: 25
* False Positives: 30
* False Negatives: 20
* Precision: 0.45454545454545453
* Recall: 0.5555555555555556
* F1: 0.5
