## Overview

Sample repo to provide an example of spark based job to read and write deeply nested datetime partitioned files dynamically (i.e without really knowing the partitioned folder names completely).

## Dependencies

1) Scala 2.12+
2) Sbt
3) Spark

## Compile and Run

```bash
$ sbt clean package 

$ spark-submit --class main.scala.blog.PartitionedReaderWriter target/scala-2.12/spark_partitioned_reader_2.12-1.0.jar
```