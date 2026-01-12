This directory contains the evaluation results of the thesis. \
Results for each context type are listed in their own directory.\
Results are grouped by dataset and always consist of a results-config*.md and accompanying traceLinks-config*.csv. The md file contains the configuration that was used as well as some measured metrics.
The CSV file with the same suffix contains a list of the trace links discovered by LiSSA during that run.
The caches used in the evaluation can be found in the [LiSSA](../03_Code/LiSSA/) direcrory.
To re-run any given configuraion, simply copy the configuration from the markdown file and paste it to the [config.json](../03_Code/LiSSA/config.json).
This will automatically use the same caches as were used during the creation of the thesis.