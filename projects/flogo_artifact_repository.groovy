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
    relativeTargetDirectory("src/$gogsRepository")
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
  credentialsBinding {
   usernamePassword('GOGS_USERPASS', 'gogs')
  }
 }

 environmentVariables(GOPATH: '$WORKSPACE')

 steps {
  shell('#!/bin/bash\n' +
        "cd src/$gogsRepository\n" +
        'export GHACCESSTOKEN=$GHACCESSTOKEN\n' +
        'export TOMLFILE=./items.toml\n' +
        'go get -u github.com/nareix/curl\n' +
        'go get -u github.com/tomnomnom/linkheader\n' +
        'go run build.go\n' +
        "sed -i '1,3d' items.toml\n" +
        "sed -i '1i \\' items.toml\n" +
        "sed -i '1i lastmodified = \"'`date +%Y%m%d`'\"' items.toml\n" +
        "sed -i '1i [[version]]' items.toml\n" +
        "git remote set-url origin http://\$GOGS_USERPASS@gogs:3000/$gogsUser/$gogsRepository\n" +
        'git add -A . && git commit -a -m "Update `date +%Y%m%d`" && git push origin master --force')
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