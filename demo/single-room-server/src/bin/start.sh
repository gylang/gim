#!/usr/bin/env sh
mkdir "./his.loh"
name=room.$(date  '+%Y%m%d%H%M').out
mastername="log.out.txt"
mv -f ${mastername} "./hos.log/"${name}
 java -jar single-room-server-1.0-SNAPSHOT.jar  -Dspring.config.location=application-dev.yml > ${mastername}
