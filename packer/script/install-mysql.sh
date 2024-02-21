#!/bin/bash

sudo dnf install -y mysql mysql-server

sudo systemctl enable mysqld
sudo systemctl start mysqld

# Wait for MySQL service to start
sudo sleep 5

# Set MySQL root password
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';"

echo "MySQL installation and root password setup completed."