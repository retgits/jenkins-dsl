// Project
String project = "jfrog-devrel"
String icon = "search.png"

// Version Control
String repository = "jfrog-devrel"
String user = "retgits"
String gogs = "ubudevrel.local/gogs"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Build github.com/$user/$repository")

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

 wrappers {
  colorizeOutput()
  credentialsBinding {
   usernamePassword('GITHUB_USERPASS', 'github')
  }
 }

 steps {
  shell("git clone https://\$GITHUB_USERPASS@github.com/$user/$repository .")
  shell("make deps")
  shell("make docker")
  shell("docker tag $user/$repository:latest $user/$repository:\$BUILD_NUMBER")
  shell("docker stop $user/$repository && docker rm $user/$repository")
  shell("docker run -d -p 9999:9999 --name $repository $user/$repository:\$BUILD_NUMBER")
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