// Project
String project = "nginx-proxy"
String icon = "search.png"

// Version Control
String repository = "docker-configs"
String user = "retgits"

// Job DSL definition
freeStyleJob("$project") {
 displayName("$project")
 description("Build $project")

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
  shell("cd nginx-proxy && docker build . -t retgits/nginx-proxy:\$BUILD_NUMBER")
  shell("docker stop nginx-proxy && docker rm nginx-proxy")
  shell("docker run -d -p 80:80 --name=nginx-proxy retgits/nginx-proxy:\$BUILD_NUMBER")
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