// Project
String project = "devrel-sources"
String icon = "search.png"

// Version Control
String repository = "devrel-sources"
String user = "jfrog"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Build github.com/$user/$repository")

 checkoutRetryCount(3)

 properties {
  githubProjectUrl("https://github.com/$user/$repository")
 }

 logRotator {
  numToKeep(100)
  daysToKeep(15)
 }

 wrappers {
  colorizeOutput()
  credentialsBinding {
   usernamePassword('GITHUB_USERPASS', 'github')
  }
 }

 steps {
  shell("git clone https://\$GITHUB_USERPASS@github.com/$user/$repository .")
  shell("make deps")
  shell("make docker SERVERNAME=10.6.18.185 PORT=9999 PROXY=true PREFIX=retgits")
  shell("docker tag retgits/jfrog-devrel:latest retgits/jfrog-devrel:\$BUILD_NUMBER")
  shell("docker stop jfrog-devrel && docker rm jfrog-devrel")
  shell("docker run -d -p 9999:9999 --name jfrog-devrel retgits/jfrog-devrel:\$BUILD_NUMBER")
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