## Configuration (2026-01-02_19-22+0100 -- config.json_01f92e0d-6de5-35fc-8df0-9c04a1616d63)
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
          "path" : "./datasets/CM1-NASA/high"
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
          "path" : "./datasets/CM1-NASA/low"
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
          "path" : "./datasets/CM1-NASA/answer.csv",
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
      "model" : "gpt-5-2025-08-07",
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
* True Positives: 8
* False Positives: 8
* False Negatives: 33
* Precision: 0.5
* Recall: 0.1951219512195122
* F1: 0.28070175438596495
