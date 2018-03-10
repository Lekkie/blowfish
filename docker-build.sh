rm -f ./docker/blowfish*.jar
rm -f ./docker/app*.yml
gradle clean build
cp ./build/libs/blowfish-1.0-SNAPSHOT.jar ./docker/
cp ./src/main/resources/application.yml ./docker/
docker rm $(docker ps -a -q)
docker rmi -f blowfish

# docker hub username
USERNAME=lekkie
# image name
IMAGE=blowfish

version=`cat ./docker/VERSION`
version="${version%.*}.$((${version##*.}+1))"
echo $version > './docker/VERSION'
echo "version: $version"

docker build -t $USERNAME/$IMAGE ./docker/

docker tag $USERNAME/$IMAGE $USERNAME/$IMAGE:$version



#docker run -p 8080:8080 swordfish
#docker run -it -p 8080:8080 swordfish



