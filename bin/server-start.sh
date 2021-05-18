#!/usr/bin/env sh
mastername="log.out.txt"
echo "" > ${mastername}
nohup java -jar single-room-server-1.0-SNAPSHOT.jar  -Dspring.config.location=application-dev.yml > ${mastername}
