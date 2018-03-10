# Lets switch to the /opt/blowfish dir
## Create the config folder
#mkdir /opt/blowfish/

set -e
cd /root/blowfish/

sed -i '/jdbcUrl: jdbc:h2/c\    jdbcUrl: '${DB_URL} application.yml
sed -i '/platform: h2/c\    platform: '${DB_PLATFORM} application.yml
sed -i '/username: sa/c\    username: '${DB_USER} application.yml
sed -i '/password: test/c\    password: '${DB_PASSWD} application.yml
sed -i '/driverClassName: org.h2.Driver/c\    driverClassName: '${DB_DRIVER} application.yml
sed -i '/database-platform: org.hibernate.dialect.H2Dialect/c\    database-platform: '${DB_DATABASE_PLATFORM} application.yml

sed -i '/cluster-mode: false/c\    cluster-mode: '${REDIS_CLUSTER_MODE} application.yml
sed -i '/host: localhost/c\    host: '${REDIS_HOST} application.yml
sed -i '/port: 6379/c\    port: '${REDIS_PORT} application.yml
sed -i '/nodes: redis1.4un0o7.ng.0001.euw2.cache.amazonaws.com:6379/c\      nodes: '${REDIS_CLUSTER_NODES} application.yml
sed -i '/max-redirects: 3/c\      max-redirects: '${REDIS_CLUSTER_MAX_REDIRECT} application.yml

sed -i '/enabled: true/c\      enabled: '${H2_ENABLED} application.yml



