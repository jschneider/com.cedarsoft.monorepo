#### Prepare release branch

````
git checkout develop
git checkout -b release/75.3.0
````

#### Check project state
````
mvn clean install
mvn javadoc:javadoc -Dquiet
````

#### Set version number

Expects a -SNAPSHOT version
````
mvn validate -N
````

Create tag for release
````
git tag 75.3.0 -m "tagging for release"
````

Expects a **none** snapshot version:
````
mvn validate
````

#### Create the release

Publish the code to the repository first
````
git push origin -u --follow-tags
````

Make the release
````
mvn clean deploy -DperformRelease
````

Release the staging repo
````
mvn nexus-staging:release
````


Merge changes to master and develop

````
git checkout master
git pull
git merge release/75.3.0 --ff
git push origin
git checkout develop
git merge master --ff
git push origin
````

#### Cleanup

Delete release branch
````
git branch -d release/75.3.0
git push origin :release/75.3.0
````



