## LissaConfiguration (2025-11-14_11-14+0100 -- config.json_df154666-e5b9-3433-8294-fe91e7fd42d7)
```json
{
  "cache_dir" : "./cache-r2r/GANNT-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/GANNT/answer.csv",
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
          "path" : "./datasets/GANNT/high"
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
    "name" : "embeddings-as-context_openai",
    "args" : {
      "model" : "gpt-4o-2024-08-06",
      "context_id" : "requirements",
      "search_similar_to" : "SOURCE",
      "template" : "Question: We have two software development artifacts that may or may not be related in terms of their purpose, functionality, or requirements.\n\nSource Artifact ({source_type}): '''{source_content}'''\n\nTarget Artifact ({target_type}): '''{target_content}'''\n\nPlease analyze the content of both artifacts and determine if they are related. For the purpose of this analysis, consider the following criteria:\n1. **Purpose**: Do both artifacts serve a similar goal or objective within the software development process?\n2. **Functionality**: Do both artifacts provide similar features or capabilities?\n3. **Requirements**: Do both artifacts address the same requirements or constraints?\n4. **Context**: Additionally, the following {similar_to} {context_type} exist in the project:\n{context_content}\n\nBased on these criteria, answer with 'yes' if they are related, or 'no' if they are not related. Enclose your answer in '<trace>' and '</trace>'.",
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
* #TraceLinks (GS): 68
* #Source Artifacts: 17
* #Target Artifacts: 69
## Results
* True Positives: 37
* False Positives: 28
* False Negatives: 31
* Precision: 0.5692307692307692
* Recall: 0.5441176470588235
* F1: 0.5563909774436089
