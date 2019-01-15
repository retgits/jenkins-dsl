listView('Builds') {
 description('All jobs that have builds.')
 filterBuildQueue()
 filterExecutors()
 jobs {
  regex(/(?!mirror.*).*/)
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