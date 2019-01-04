// Project
String project = "webhook-bridge"
String icon = "search.png"

// Version Control
String repository = "webhook-bridge"
String user = "retgits"
String gogs = "ubudevrel.local"

// Job DSL definition
freeStyleJob("mirror $project") {
 displayName("mirror $project")
 description("Mirror github.com/$user/$repository")

 checkoutRetryCount(3)

 properties {
  githubProjectUrl("https://github.com/$user/$repository")
  sidebarLinks {
   link("http://$gogs/$user/$repository", "Gogs", "$icon")
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
  shell("git clone --mirror https://github.com/$user/$repository repo")
  shell("cd repo && git push --mirror http://\$GOGS_USERPASS@ubudevrel.local/$user/$repository")
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