package main.scala.blog

/**
*  Spark stream example code to read and write from a partitioned folder
*  to a partitioned folder without explicitly known datetime.
*/

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.functions.{udf, input_file_name, col, lit, regexp_extract}

object PartitionedReaderWriter {

    def main(args: Array[String]) : Unit = {
        
        val spark = SparkSession
                    .builder
                    .appName("PartitionedReaderWriterApp")
                    .getOrCreate()

        val sourceBasePath = "data/partitioned_files_source/user"
        val sourceDf = spark.read
                            .format("csv")
                            .schema("State STRING, Color STRING, Count INT")
                            .option("header", "true")
                            .option("pathGlobFilter", "*.csv")
                            .option("recursiveFileLookup", "true")
                            .load(sourceBasePath)

        val destinationBasePath = "data/partitioned_files_destination/user"
        val writeDf = sourceDf
                        .withColumn("year", regexp_extract(input_file_name(), "year=(\\d{4})", 1))
                        .withColumn("month", regexp_extract(input_file_name(), "month=(\\d{2})", 1))
                        .withColumn("day", regexp_extract(input_file_name(), "day=(\\d{2})", 1))
                        .withColumn("hour", regexp_extract(input_file_name(), "hour=(\\d{2})", 1))

        writeDf.write
                .format("csv")
                .option("header", "true")
                .mode("overwrite")
                .partitionBy("year", "month", "day", "hour")
                .save(destinationBasePath)

        spark.stop()        
    }
}   