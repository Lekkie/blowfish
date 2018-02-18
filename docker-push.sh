./docker-build.sh

# docker hub username
USERNAME=lekkie
# image name
IMAGE=blowfish

#export USERNAME="lekkie"
#docker login

version=`cat ./docker/VERSION`
echo "version: $version"

# push it
docker push $USERNAME/$IMAGE
#docker push $USERNAME/$IMAGE:latest
#docker push $USERNAME/$IMAGE:$version
#docker push $DOCKER_ID_USER/blowfish
