## Configuration (2025-12-05_20-39+0100 -- config.json_5513d152-60fc-34c4-a324-09692e225a56)
```json
{
  "cache_dir" : "./cache-ext-tl/GANNT-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/GANNT/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false,
    "name" : "gold_standard"
  },
  "context_configurations" : [ {
    "name" : "embeddings",
    "context_id" : "internal_source_requirements",
    "modules" : [ {
      "ARTIFACT_PROVIDER" : {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/GANNT/high"
        }
      },
      "PREPROCESSOR" : {
        "name" : "artifact",
        "args" : { }
      },
      "EMBEDDING_CREATOR" : {
        "name" : "openai",
        "args" : {
          "model" : "text-embedding-3-large"
        }
      },
      "ELEMENT_STORE" : {
        "name" : "custom",
        "args" : {
          "max_results" : "infinity"
        }
      }
    } ]
  }, {
    "name" : "embeddings",
    "context_id" : "internal_target_requirements",
    "modules" : [ {
      "ARTIFACT_PROVIDER" : {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/GANNT/low"
        }
      },
      "PREPROCESSOR" : {
        "name" : "artifact",
        "args" : { }
      },
      "EMBEDDING_CREATOR" : {
        "name" : "openai",
        "args" : {
          "model" : "text-embedding-3-large"
        }
      },
      "ELEMENT_STORE" : {
        "name" : "custom",
        "args" : {
          "max_results" : "infinity"
        }
      }
    } ]
  }, {
    "name" : "embeddings",
    "context_id" : "external_source_requirements",
    "modules" : [ {
      "ARTIFACT_PROVIDER" : {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/dronology/high"
        }
      },
      "PREPROCESSOR" : {
        "name" : "artifact",
        "args" : { }
      },
      "EMBEDDING_CREATOR" : {
        "name" : "openai",
        "args" : {
          "model" : "text-embedding-3-large"
        }
      },
      "ELEMENT_STORE" : {
        "name" : "custom",
        "args" : {
          "max_results" : "infinity"
        }
      }
    } ]
  }, {
    "name" : "embeddings",
    "context_id" : "external_target_requirements",
    "modules" : [ {
      "ARTIFACT_PROVIDER" : {
        "name" : "text",
        "args" : {
          "artifact_type" : "requirement",
          "path" : "./datasets/dronology/low"
        }
      },
      "PREPROCESSOR" : {
        "name" : "artifact",
        "args" : { }
      },
      "EMBEDDING_CREATOR" : {
        "name" : "openai",
        "args" : {
          "model" : "text-embedding-3-large"
        }
      },
      "ELEMENT_STORE" : {
        "name" : "custom",
        "args" : {
          "max_results" : "infinity"
        }
      }
    } ]
  }, {
    "name" : "external_trace_links",
    "context_id" : "external_trace_links",
    "modules" : [ {
      "GOLD_STANDARD" : {
        "args" : {
          "path" : "./datasets/dronology/answer.csv",
          "hasHeader" : "true",
          "swap_columns" : "false"
        }
      }
    } ]
  } ],
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/GANNT/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/GANNT/low"
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
    "name" : "external-trace-links-as-context_openai",
    "args" : {
      "model" : "gpt-4o-mini-2024-07-18",
      "temperature" : "1",
      "internal_source_context_id" : "internal_source_requirements",
      "internal_target_context_id" : "internal_target_requirements",
      "external_source_context_id" : "external_source_requirements",
      "external_target_context_id" : "external_target_requirements",
      "external_trace_links_context_id" : "external_trace_links",
      "number_of_examples" : "4",
      "seed" : "133742243",
      "template" : "Below are artifacts from multiple software systems.\nConsidering the examples, is there a traceability link between the source and target requirement ({i}) ?\nAnswer with 'yes' or 'no'.\n\n{negative_examples}\n{positive_examples}\n({i}) Source {source_type}: '''{source_content}'''\nTarget {target_type}: '''{target_content}'''\nLink:\n",
      "example_template" : "({i}) Source {source_type}: '''{source_content}'''\nTarget {target_type}: '''{target_content}'''\nLink: {link}\n"
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
* #TraceLinks (GS): 68
* #Source Artifacts: 17
* #Target Artifacts: 69
## Results
* True Positives: 14
* False Positives: 8
* False Negatives: 54
* Precision: 0.6363636363636364
* Recall: 0.20588235294117646
* F1: 0.3111111111111111
