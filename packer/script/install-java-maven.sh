#!/bin/bash

#Install required packages
sudo yum install wget unzip -y
#Installing java
sudo dnf install java-17-openjdk-devel.x86_64 -y
echo "Java installation completed."

export VER="3.9.5"
echo "Downloading Apache Maven ${VER}..."
sudo wget https://downloads.apache.org/maven/maven-3/${VER}/binaries/apache-maven-${VER}-bin.tar.gz
sudo tar xf /tmp/apache-maven-${VER}-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-${VER}/bin/mvn /usr/local/bin/mvn
sudo rm ./apache-maven-${VER}-bin.tar.gz

# Set Maven environment variables
cat > /etc/profile.d/maven.sh <<'EOF'
export MAVEN_HOME=/opt/maven
export PATH=$PATH:/opt/apache-maven-3.9.5/bin
EOF
source /etc/profile.d/maven.sh

echo "Maven installation completed."