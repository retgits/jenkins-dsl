# seed-projects
Create a new Jenkins Freestyle project with the below code to automatically create `update-dsl`, which in turn can be triggered to create the other items

```
freeStyleJob('update dsl') {
 displayName('update dsl')
 description('Updatess all the Jenkins DSLs in the jenkins-dsl repository.')

 checkoutRetryCount(3)

 properties {
  githubProjectUrl('https://github.com/retgits/jenkins-dsl')
 }

 logRotator {
  numToKeep(100)
  daysToKeep(15)
 }

 scm {
  git {
   remote {
    url('https://github.com/retgits/jenkins-dsl.git')
   }
   branches('*/master')
   extensions {
    wipeOutWorkspace()
    cleanAfterCheckout()
   }
  }
 }

 triggers {}

 wrappers {
  colorizeOutput()
 }

 steps {
  dsl {
   external('**/*.groovy')
   removeAction('DELETE')
   removeViewAction('DELETE')
   additionalClasspath('.')
  }
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
```