# Lets switch to the /opt/blowfish dir
## Create the config folder
#mkdir /opt/blowfish/

set -e
cd /root/blowfish/

sed -i '/trade: /c\    trade: '${TRADE_DELAY} application.yml
sed -i '/completeOpenOrder: /c\    completeOpenOrder: '${COMPLETE_OPEN_ORDER_DELAY} application.yml
#sed -i '/deleteOldHistory: /c\deleteOldHistory: '${DELETE_OLD_HISTORY_CRON} application.yml
#sed -i '/setAverageRate: /c\setAverageRate: '${AVERAGE_RATE_CRON} application.yml

sed -i '/jdbcUrl: /c\    jdbcUrl: '${DB_URL} application.yml
sed -i '/platform:: /c\    platform:: '${DB_PLATFORM} application.yml
sed -i '/username: /c\    username: '${DB_USER} application.yml
sed -i '/password: /c\    password: '${DB_PASSWD} application.yml
sed -i '/driverClassName:/c\    driverClassName: '${DB_DRIVER} application.yml
sed -i '/database-platform: /c\    database-platform: '${DB_DATABASE_PLATFORM} application.yml

sed -i '/enabled: /c\      enabled: '${H2_ENABLED} application.yml



