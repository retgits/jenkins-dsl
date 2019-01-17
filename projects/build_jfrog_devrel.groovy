// Project
String project = "jfrog-devrel"
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
  shell("docker tag $user/$project:latest $user/$project:\$BUILD_NUMBER")
  shell("docker stop $project && docker rm $project")
  shell("docker run -d -p 9999:9999 --name $project $user/$project:\$BUILD_NUMBER")
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