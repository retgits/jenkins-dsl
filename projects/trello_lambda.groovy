// Project information
String project = "Trello - Lambda"
String icon = "search.png"

// GitHub information
String gitHubRepository = "trello-lambda"
String gitHubUser = "retgits"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Deploy $project to AWS Lambda")

 checkoutRetryCount(3)

 properties {
  sidebarLinks {
   link("https://github.com/$gitHubUser/$gitHubRepository", "GitHub", "$icon")
  }
 }

 scm {
  git {
   remote {
    url("https://github.com/$gitHubUser/$gitHubRepository")
   }
   branches('*/master')
   extensions {
    wipeOutWorkspace()
    cleanAfterCheckout()
    relativeTargetDirectory("src/$gitHubRepository")
   }
  }
 }

 logRotator {
  numToKeep(100)
  daysToKeep(15)
 }

 wrappers {
  colorizeOutput()
 }

 environmentVariables(GOPATH: '$WORKSPACE')
  
 steps {
  shell("#!/bin/bash\n" +
        "#Set general properties for Lambda based deployments\n" +
        "export AWS_ACCESS_KEY_ID=\$AWSACCESSKEYID\n" +
        "export AWS_SECRET_ACCESS_KEY=\$AWSSECRETACCESSKEY\n" +
        "export AWS_DEFAULT_REGION=\$AWSREGION\n" +
        "find . -type f -name '*.sh' -exec chmod +x {} \\;\n" +
        "#Perform the build\n" +
        "cd src/$gitHubRepository\n" +
        "./build.sh deps\n" +
        "./build.sh deploy\n")
 }

 publishers {
  mailer {
   recipients('$ADMIN_EMAIL')
   notifyEveryUnstableBuild(true)
   sendToIndividuals(false)
  }
  wsCleanup()
 }
}