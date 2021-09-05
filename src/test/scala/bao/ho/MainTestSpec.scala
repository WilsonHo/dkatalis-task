package bao.ho

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class MainTestSpec extends AsyncFlatSpec with StreamingSuiteBase with Matchers {
  "Run execute function in local" should "show the number result" in {
    val spark         = SparkSession.builder.config(sc.getConf).getOrCreate()
    val OneBillionIDR = 1000000000
    Main
      .execute(spark, "./src/test/resources/inputs/", OneBillionIDR)
      .writeStream
      .format("console")
      .option("truncate", "false")
      .outputMode(OutputMode.Complete())
      .start()
      .awaitTermination(30000)
    //TODO: This is just the sample test cases
    1 shouldBe 1
  }
}
