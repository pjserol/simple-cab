#!/bin/bash

echo "Creating mysql container..."

docker pull mysql/mysql-server:5.7

docker run -d -p 3306:3306 --name mysql-cab \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_ROOT_HOST=% \
-e MYSQL_DATABASE=ny_cab_data mysql/mysql-server:5.7

echo "Sleep 20 seconds"
sleep 20

echo "Importing sql scripts..."
mysql -hlocalhost -P3306 --protocol=tcp -u root -ppassword \
-Dny_cab_data < ./sql-scripts/ny_cab_data_cab_trip_data_full.sql

echo "Creating redis container..."

docker pull redis:6.0.6

docker run --name redis-cab -p 6379:6379 -d redis:6.0.6

