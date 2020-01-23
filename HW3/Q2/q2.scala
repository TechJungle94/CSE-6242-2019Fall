// Databricks notebook source
// Q2 [25 pts]: Analyzing a Large Graph with Spark/Scala on Databricks

// STARTER CODE - DO NOT EDIT THIS CELL
import org.apache.spark.sql.functions.desc
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import spark.implicits._

// COMMAND ----------

// STARTER CODE - DO NOT EDIT THIS CELL
// Definfing the data schema
val customSchema = StructType(Array(StructField("answerer", IntegerType, true), StructField("questioner", IntegerType, true),
    StructField("timestamp", LongType, true)))

// COMMAND ----------

// STARTER CODE - YOU CAN LOAD ANY FILE WITH A SIMILAR SYNTAX.
// MAKE SURE THAT YOU REPLACE THE examplegraph.csv WITH THE mathoverflow.csv FILE BEFORE SUBMISSION.
val df = spark.read
   .format("com.databricks.spark.csv")
   .option("header", "false") // Use first line of all files as header
   .option("nullValue", "null")
   .schema(customSchema)
   .load("/FileStore/tables/mathoverflow.csv")
   .withColumn("date", from_unixtime($"timestamp"))
   .drop($"timestamp")

// COMMAND ----------

//display(df)
df.show()

// COMMAND ----------

// PART 1: Remove the pairs where the questioner and the answerer are the same person.
// ALL THE SUBSEQUENT OPERATIONS MUST BE PERFORMED ON THIS FILTERED DATA

// ENTER THE CODE BELOW
var filtered_data = df.filter($"answerer"=!=$"questioner")
filtered_data.select($"answerer",$"questioner",$"date".alias("timestamp")).show

// COMMAND ----------

// PART 2: The top-3 individuals who answered the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest out-degrees.

// ENTER THE CODE BELOW
filtered_data.groupBy("answerer").count.sort($"count".desc,$"answerer").limit(3).select($"answerer",$"count".alias("questions_answered")).show


// COMMAND ----------

// PART 3: The top-3 individuals who asked the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest in-degree.

// ENTER THE CODE BELOW
filtered_data.groupBy("questioner").count.sort($"count".desc,$"questioner").limit(3).select($"questioner",$"count".alias("questions_asked")).show


// COMMAND ----------

// PART 4: The top-5 most common asker-answerer pairs - sorted in descending order - if tie, the one with lower value node-id in the first column (u->v edge, u value) gets listed first.

// ENTER THE CODE BELOW
filtered_data.groupBy("answerer","questioner").count.sort($"count".desc,$"answerer",$"questioner").limit(5).show

// COMMAND ----------

// PART 5: Number of interactions (questions asked/answered) over the months of September-2010 to December-2010 (i.e. from September 1, 2010 to December 31, 2010). List the entries by month from September to December.

// Reference: https://www.obstkel.com/blog/spark-sql-date-functions
// Read in the data and extract the month and year from the date column.
// Hint: Check how we extracted the date from the timestamp.

// ENTER THE CODE BELOW
val data1 = filtered_data.where(month(to_date($"date"))>=9 && year(to_date($"date"))===2010).groupBy(month(to_date($"date")).alias("month")).count.sort($"month".asc).select($"month",$"count".alias("total_interactions")).show



// COMMAND ----------

// PART 6: List the top-3 individuals with the maximum overall activity, i.e. total questions asked and questions answered.

// ENTER THE CODE BELOW
val data1 = filtered_data.groupBy("answerer").count.sort($"count".desc,$"answerer")
val data2 = filtered_data.groupBy("questioner").count.sort($"count".desc,$"questioner")
val data3 = data1.join(data2,data1("answerer")===data2("questioner")).withColumn("total_activity", col = data1("count")+data2("count")).sort($"total_activity".desc).limit(3).select($"answerer".alias("userID"),$"total_activity").show


