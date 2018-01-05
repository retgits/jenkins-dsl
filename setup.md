# Docker file for my own Jenkins build
## Additional plugins
* Gogs plugin
* Go Plugin
* NodeJS Plugin
* AnsiColor
* Job DSL
* Sidebar Link
* Green Balls
* Simple Theme Plugin
* Environment Injector Plugin
## Configuration
### Configure System [Jenkins]
#### Theme
* URL of theme CSS: https://cdn.rawgit.com/afonsof/jenkins-material-theme/gh-pages/dist/material-light-blue.css
#### Jenkins Location
* Jenkins URL: http://HOSTNAME:PORT/
* System Admin e-mail address: `Jenkins Daemon <email redacted>`
#### Git plugin
* Global Config user.name Value: retgits
* Global Config user.email Value: `<email redacted>`
#### E-mail notification
* SMTP server: smtp.gmail.com
* Use SMTP Authentication: Yes
* User Name: `<email redacted>`
* Password: `<App specific password>`
* Use SSL: Yes
* SMTP Port: 465
* Reply-To address: `<email redacted>`
* Charset: UTF-8
### Global Tool Configuration
#### Go
* Name: go-192
* Install automatically: Yes
* Version: Go 1.9.2
## Usage
### Build
```
docker build . -t retgits/jenkins
```
### Run
```
docker run -d -p 8080:8080 -p 50000:50000 -v $DOCKER_STORAGE/jenkins:/var/jenkins_home --name=jenkins retgits/jenkins
```