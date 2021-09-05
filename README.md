Task 1.2
=======

### Start the spark cluster 
```shell
$ docker-compose -f docker/docker-compose.yml up -d
```

### Build assembly jar file and copy files to cluster
```shell
$ sbt clean assembly
$ cp ./target/scala-2.12/spark-app.jar ./docker/app/spark-app.jar
$ cp ./src/test/resources/inputs/bank_data.csv ./docker/data/inputs
```

### Submit spark jar to cluster
```shell
$ docker exec spark-master /spark/bin/spark-submit --class "bao.ho.Main" --master spark://spark-master:7077 /app/spark-app.jar /data/inputs 1000000000
```

### Shut down the spark cluster
```shell
$ docker-compose -f docker/docker-compose.yml down
```

### Run tests
```shell
$ sbt test
```

### SQL script
```sql
SELECT DATE_FORMAT(date, "%Y-%m-01") AS transaction_month, 
       account_no, 
       sum(amount) as total_amount
FROM bank_data
GROUP BY transaction_month, account_no
HAVING total_amount > 1000000000
ORDER BY transaction_month, account_no
```

Task 2
=======
Not only in the next 3 years I think the data engineering field will be very important in the next couple of decades because now the demand for using data especially big data is increasing every day. People need data for everything in life, companies need data for building a better product. Data will be more and more and in order exploit the data then we need a lot of people who have knowledge and experience in order to work with data. Data engineering is being promised a lot of opportunities and people will get more achievement when they can exploit the knowledge inside the data so people always need data engineering.
