Kotlin Gatling bad metrics Graphite-exporter Prometheus without kafka-plugin

Gatling loads Kafka by sending messages containing an ID (36 characters), a valid name (full name), and a tax ID. Kafka reads the Spring stub and inserts these messages into the Postgres database.
Metrics are exported to the graphite exporter in Docker and read by Prometheus. The metrics are incomplete due to the lack of a graphite exporter and a Kafka plugin in the project.
Dashboard files for Spring and Gatling are in Docker files. All dependencies are nested within the files.

gatling version 3.15.1
kotlin



