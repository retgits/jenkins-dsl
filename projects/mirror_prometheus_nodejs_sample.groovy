// Project information
String project = "blog"
String icon = "search.png"

// GitHub information
String gitHubRepository = "prometheus-nodejs-sample"
String gitHubUser = "retgits"

// Gogs information
String gogsRepository = "prometheus-nodejs-sample"
String gogsUser = "retgits"
String gogsHost = "ubusrvls.na.tibco.com:3000"

// Job DSL definition
freeStyleJob("mirror-$project") {
 displayName("mirror-$project")
 description("Mirror github.com/$gitHubUser/$gitHubRepository")

 checkoutRetryCount(3)

 properties {
  githubProjectUrl("https://github.com/$gitHubUser/$gitHubRepository")
  sidebarLinks {
   link("http://$gogsHost/$gogsUser/$gogsRepository", "Gogs", "$icon")
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

 steps {
  shell("git clone --mirror https://github.com/$gitHubUser/$gitHubRepository repo")
  shell("cd repo && git push --mirror http://\$GOGS_USERPASS@gogs:3000/$gogsUser/$gogsRepository")
 }

 publishers {
  wsCleanup()
 }
}