## Configuration (2026-01-02_19-09+0100 -- config.json_05ef2cd4-7198-3f9c-9a38-76755f8f2850)
```json
{
  "cache_dir" : "./cache-ext-tl/ModisDataset-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/ModisDataset/answer.csv",
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
          "path" : "./datasets/ModisDataset/high"
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
          "path" : "./datasets/ModisDataset/low"
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
      "path" : "./datasets/ModisDataset/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/ModisDataset/low"
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
      "model" : "gpt-5.1-2025-11-13",
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
    "name" : "identity",
    "args" : { }
  },
  "name" : "LiSSA"
}
```

## Stats
* #TraceLinks (GS): 41
* #Source Artifacts: 19
* #Target Artifacts: 49
## Results
* True Positives: 9
* False Positives: 9
* False Negatives: 32
* Precision: 0.5
* Recall: 0.21951219512195122
* F1: 0.3050847457627119
