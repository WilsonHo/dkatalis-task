name := "dkatalis-task"

version := "0.1"

scalaVersion := "2.12.14"

val sparkV = "3.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"         % sparkV        % "provided;test",
  "org.apache.spark" %% "spark-sql"          % sparkV        % "provided;test",
  "org.apache.spark" %% "spark-streaming"    % sparkV        % "provided;test",
  "org.scalatest"    %% "scalatest"          % "3.2.9"       % "test",
  "com.holdenkarau"  %% "spark-testing-base" % "3.1.1_1.1.0" % "test"
)

test / fork := true

test / parallelExecution := false

javaOptions ++= Seq("-Xms512M",
                    "-Xmx2048M",
                    "-XX:MaxPermSize=2048M",
                    "-XX:+CMSClassUnloadingEnabled")

assembly / assemblyJarName := "spark-app.jar"
