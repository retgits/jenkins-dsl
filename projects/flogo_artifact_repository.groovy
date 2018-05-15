// Project information
String project = "Flogo Artifact Repository"
String icon = "search.png"

// Gogs information
String gogsRepository = "flogo-artifact-repository"
String gogsUser = "retgits"
String gogsHost = "ubusrvls.na.tibco.com:3000"
String gogsInternalUrl = "gogs:3000"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Create the Flogo Artifact Repository")

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
   }
  }
 }

 logRotator {
  numToKeep(100)
  daysToKeep(15)
 }

 triggers {
  cron('@daily')
 }

 wrappers {
  colorizeOutput()
 }

 steps {
  shell('#!/bin/bash\n' +
        'export GHACCESSTOKEN=$GHACCESSTOKEN\n' +
        'export TOMLFILE=./data/items.toml\n' +
        'go get -u github.com/nareix/curl\n' +
        'go get -u github.com/tomnomnom/linkheader\n' +
        'go run build.go\n' +
        'hugo')
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