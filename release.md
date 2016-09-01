git checkout master
git merge develop --ff

git tag 75.3.0 -m "tagging for release"

mvn clean deploy -DperformRelease
mvn nexus-staging:release

