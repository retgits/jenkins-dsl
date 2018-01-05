freeStyleJob('mirror-dockerfiles') {
 displayName('mirror-dockerfiles')
 description('Mirror github.com/retgits/dockerfiles')

 checkoutRetryCount(3)

 properties {
  githubProjectUrl('https://github.com/retgits/dockerfiles')
  sidebarLinks {
   link('http://leons-mbp.na.tibco.com:10080/retgits/dockerfiles', 'retgits/dockerfiles', 'notepad.png')
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
   usernamePassword('GOGS_USERPASS', 'eb7f29a7-dbf0-4d6d-a407-6ac8dc4bf63b')
  }
 }

 steps {
  shell('git clone --mirror https://github.com/retgits/dockerfiles repo')
  shell('cd repo && git push --mirror http://$GOGS_USERPASS@leons-mbp.na.tibco.com:10080/retgits/dockerfiles.git')
 }

 publishers {
  wsCleanup()
 }
}