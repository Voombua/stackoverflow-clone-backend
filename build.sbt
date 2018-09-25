enablePlugins(JavaServerAppPackaging)

lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion    = "2.5.11"
lazy val phantomDslVersion = "2.24.10"
lazy val slickVersion = "3.2.3"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.4"
    )),
    name := "stackoverflowbackend",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                      % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"           % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"                  % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"                    % akkaVersion,
      "com.outworkers"    %% "phantom-dsl"                    % phantomDslVersion,
      "de.heikoseeberger" %% "akka-http-play-json"            % "1.17.0",
      "com.typesafe.play" %% "play-ws-standalone-json"        % "1.1.8",
      "ch.qos.logback"    %  "logback-classic"                % "1.2.3",
      "org.postgresql"     %  "postgresql"                    % "42.2.5",
      "org.flywaydb"       %  "flyway-core"                   % "5.1.4",
      "com.typesafe.slick" %% "slick"                         % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp"                % slickVersion,
      "org.slf4j"          %  "slf4j-nop"                     % "1.7.25",
      "com.typesafe.slick" %% "slick-hikaricp"                % slickVersion,
      "com.jason-goodwin" %% "authentikat-jwt"                % "0.4.5",
      "joda-time"         % "joda-time"                       % "2.10",
      "com.typesafe.akka" %% "akka-http-spray-json"           % "10.0.11",
      "com.roundeights"   %% "hasher"                         % "1.2.0",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test
    )
  )
