// Project information
String project = "Google Calendar to Trello (TIBCO) - Lambda"
String icon = "search.png"

// Gogs information
String gogsRepository = "gocal-to-trello"
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

 steps {
  shell('#!/bin/bash\n' +
        'go get github.com/mikefarah/yq\n' +
        'yq w -i serverless.yml service calendar-to-trello-tibco\n'+
        'yq w -i serverless.yml functions.gocaltrello.environment.FUNCTIONTYPE tibco\n'+
        'for row in $(yq r serverless.yml provider.environment); do if [[ $row = *":"* ]]; then param=${row::-1}; $(yq w -i serverless.yml provider.environment.${param} ${!param}); fi; done\n' +
        'shopt -s extglob\n' +
        'GOPATH=$(pwd)\n' +
        'mkdir -p src/project\n' +
        'mv !(src) src/project\n' +
        'cd src/project\n' +
        'dep ensure\n' +
        'env GOOS=linux go build -ldflags="-s -w" -o bin/gocaltrello gocaltrello/*.go\n' +
        'serverless deploy')
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