#!/bin/bash

sudo mv /tmp/webapp-1.1.0.jar /opt/
sudo mv /tmp/.env /opt/
sudo chown -R csye6225:csye6225 /tmp/webapp.service
sudo mv /tmp/webapp.service /etc/systemd/system/webapp.service
sudo systemctl daemon-reload
sudo systemctl enable webapp.service