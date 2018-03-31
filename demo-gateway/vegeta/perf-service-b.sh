#!/bin/bash

url=https://payment-gateway.cfapps.io/payment/b
#url=http://localhost:8080/payment/b
#url=https://payment-b.cfapps.io/payment
duration=60s
rate=150
echo "Sending $duration * $rate to $url"
echo "POST $url" | \
./vegeta attack -duration=$duration -rate=$rate -timeout=180s | \
tee results.bin | \
./vegeta report -reporter=text