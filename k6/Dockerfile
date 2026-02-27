FROM grafana/k6:0.49.0

WORKDIR /script

COPY script/streaming.js /script/batch.js
COPY run.sh /script/run.sh

ENTRYPOINT ["sh"]