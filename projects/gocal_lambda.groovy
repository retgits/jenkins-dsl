// Project information
String project = "Google Calendar - Lambda"
String icon = "search.png"

// Gogs information
String gogsRepository = "gocal-lambda"
String gogsUser = "retgits"
String gogsHost = "ubusrvls.na.tibco.com:3000"
String gogsInternalUrl = "gogs:3000"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Deploy $project to AWS Lambda")

 checkoutRetryCount(3)

 properties {
  sidebarLinks {
   link("http://$gogsHost/$gogsUser/$gogsRepository", "Gogs", "$icon")
  }
 }

 scm {
  git {
   remote {
    url("http://$gogsInternalUrl/$gogsUser/$gogsRepository")
    credentials('gogs')
   }
   branches('*/master')
   extensions {
    wipeOutWorkspace()
    cleanAfterCheckout()
    relativeTargetDirectory("src/$gogsRepository")
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
        "cd src/$gogsRepository\n" +
        "./build.sh deps\n" +
        "export FUNC=GocalPersonal\n" +
        "mv template_personal.yaml template.yaml\n" +
        "./build.sh deploy\n" + 
        "export FUNC=GocalTibco\n" +
        "mv template_tibco.yaml template.yaml\n" +
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