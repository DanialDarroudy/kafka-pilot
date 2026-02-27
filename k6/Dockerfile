FROM grafana/k6:0.49.0

WORKDIR /scripts

COPY scripts/poisson.js /scripts/poisson.js
COPY run.sh /scripts/run.sh

ENTRYPOINT ["sh"]