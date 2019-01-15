#!/usr/bin/env bash

# Description: Validate if all my public GitHub repos have an associated mirror job in Jenkins
# Author: retgits
# Last Updated: 2019-01-02

# NOMIRROR contains a list of GitHub repos that do not need to have a mirror in Jenkins
NOMIRROR=',gclone,lc,lambda-builder,wtf,kubefiles,flogo-presentation,slacker,jfrog-cli-go,artifactory-go-blog-4,artifactory-go-blog-3,artifactory-go-blog-2,jfrog-devrel'

for repo in $(curl 'https://api.github.com/users/retgits/repos' | jq '.[] .name')
do
    origRepo=${repo//\"/}
	repo=${origRepo//[-.]/_}
	if [[ ! -f ../projects/mirror_$repo.groovy ]]; then
		if [[ $NOMIRROR != *",$origRepo,"* ]];then
			echo Found no mirror for $origRepo
			ERR=true
		fi
	fi
done

if [ -z ${ERR+x} ]; then 
	echo "All repos existing and accounted for!"; 
else 
	exit 1 
fi