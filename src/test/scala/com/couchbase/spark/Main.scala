package com.couchbase.spark

import com.couchbase.client.java.document.{RawJsonDocument, JsonDocument}
import com.couchbase.client.java.view.ViewQuery
import com.couchbase.spark.connection.{CouchbaseConfig, CouchbaseConnection}
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  def main(args: Array[String]) {
    val conf = new SparkConf()
      .set("couchbase.bucket", "beer-sample")
      .setMaster("local")
      .setAppName("test")

    val sc = new SparkContext(conf)



    val rows = sc.couchbaseView(ViewQuery.from("beer", "brewery_beers"))

    val ids = rows
      .filter(_.id.startsWith("a"))
      .map(_.id)
      .collect()

    val docs = sc.couchbaseGet[JsonDocument](ids)

    docs.foreach(println)
  }

}