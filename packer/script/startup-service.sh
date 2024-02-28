#!/bin/bash

sudo mv /tmp/webapp-1.1.0.jar /opt/
sudo mv /tmp/webapp.service /etc/systemd/system/webapp.service
echo "SELINUX=permissive" | sudo tee /etc/selinux/config
sudo systemctl daemon-reload
sudo systemctl enable webapp.service