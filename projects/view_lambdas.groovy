listView('AWS Lambda') {
 description('All jobs that deploy a function to AWS Lambda.')
 filterBuildQueue()
 filterExecutors()
 jobs {
  regex(/(?!mirror.*).*Lambda/)
 }
 columns {
  status()
  weather()
  name()
  lastSuccess()
  lastFailure()
  lastDuration()
  buildButton()
 }
}