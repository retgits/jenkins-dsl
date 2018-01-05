freeStyleJob('mirror-blog') {
 displayName('mirror-blog')
 description('Mirror github.com/retgits/retgits.github.io')

 checkoutRetryCount(3)

 properties {
  githubProjectUrl('https://github.com/retgits/retgits.github.io')
  sidebarLinks {
   link('http://leons-mbp.na.tibco.com:10080/retgits/retgits.github.io', 'retgits/retgits.github.io', 'notepad.png')
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
  shell('git clone --mirror https://github.com/retgits/retgits.github.io repo')
  shell('cd repo && git push --mirror http://$GOGS_USERPASS@leons-mbp.na.tibco.com:10080/retgits/retgits.github.io.git')
 }

 publishers {
  wsCleanup()
 }
}