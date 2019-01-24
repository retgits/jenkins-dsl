// Project
String project = "webhook-bridge"
String icon = "search.png"

// Version Control
String repository = "webhook-bridge"
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
  shell("git clone https://github.com/$user/$repository .")
  shell("make deps")
  shell("make compile")
  shell("git clone https://github.com/$user/docker-configs configs")
  shell("cp configs/webhook-bridge/config.yml ./out/config.yml")
  shell("cp configs/webhook-bridge/Dockerfile ./out/Dockerfile")
  shell("cd ./out && docker build . -t $user/$repository:\$BUILD_NUMBER")
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