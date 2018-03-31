#!/bin/bash
cf add-network-policy prometheus-cf --destination-app payment-a --protocol tcp --port 8080
cf add-network-policy prometheus-cf --destination-app payment-b --protocol tcp --port 8080
cf add-network-policy prometheus-cf --destination-app payment-c --protocol tcp --port 8080

