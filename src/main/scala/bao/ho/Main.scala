package bao.ho

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import org.apache.spark.sql.functions.{ col, sum, trunc }

object Main {

  def execute(spark: SparkSession,
              inputFileLocation: String,
              totalTrxAlert: Double): Dataset[Row] = {
    val accountNoField          = StructField("accountNo", StringType)
    val dateField               = StructField("date", TimestampType)
    val transactionDetailsField = StructField("transactionDetails", StringType)
    val valueDateField          = StructField("valueDate", TimestampType)
    val transactionTypeField    = StructField("transactionType", StringType)
    val amountField             = StructField("amount", DoubleType)
    val balanceField            = StructField("balance", DoubleType)

    val schema = new StructType()
      .add(accountNoField)
      .add(dateField)
      .add(transactionDetailsField)
      .add(valueDateField)
      .add(transactionTypeField)
      .add(amountField)
      .add(balanceField)

    val streamDf =
      spark.readStream
        .option("delimiter", ",")
        .option("header", "true")
        .schema(schema)
        .csv(inputFileLocation)

    lazy val dateCol        = col("date")
    lazy val monthlyCol     = trunc(dateCol, "month").alias("month")
    lazy val monthCol       = col("month")
    lazy val accountCol     = col("accountNo")
    lazy val amountCol      = col("amount")
    lazy val aggregateCol   = sum(amountCol).as("totalAmount")
    lazy val totalAmountCol = col("totalAmount")
    streamDf
      .groupBy(monthlyCol, accountCol)
      .agg(aggregateCol)
      .select(monthCol, accountCol, totalAmountCol)
      .filter(s"totalAmount > $totalTrxAlert")
      .orderBy(monthCol, accountCol)
  }

  def writeStream(ds: Dataset[Row]): Unit =
    ds.writeStream
      .queryName(s"aggregates")
      .format("console")
      .option("truncate", "false")
      .option("checkpointLocation", "./checkpoint")
      .outputMode(OutputMode.Complete())
      .start()
      .awaitTermination()

  def main(args: Array[String]): Unit = {
    val NumberOfArgument = 2

    import org.apache.spark._
    val conf = new SparkConf()
      .setAppName("Technical assessment")

    val spark: SparkSession = SparkSession
      .builder()
      .config(conf)
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    if (args.length < NumberOfArgument)
      throw new RuntimeException("The number of argument is incorrect !")
    else {
      val inputLocation = args(0)
      val totalTrxAlert = args(1).toDouble
      val streamDf      = execute(spark, inputLocation, totalTrxAlert)
      writeStream(streamDf)
    }
  }
}
