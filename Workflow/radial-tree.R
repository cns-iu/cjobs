library(networkD3)
library(jsonlite)
library(exportwidget)
library(htmltools)
library(webshot)
## Collect arguments
args <- commandArgs(TRUE)

filename <- args[1]
out <- args[2]
#specify the name
Flare <- jsonlite::fromJSON(filename, simplifyDataFrame = FALSE)

## Recreate Bostock example from http://bl.ocks.org/mbostock/4063550
p <- radialNetwork(List = Flare, fontSize = 10, opacity = 0.9)
html_print(tagList(
  p
  ,export_widget( )
)) %>%
  normalizePath(.,winslash="/") %>%
  gsub(x=.,pattern = ":/",replacement="://") %>%
  paste0("file:///",.) %>%
  webshot( file = out, delay = 10 )
