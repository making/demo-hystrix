#!/bin/bash
cf push -f `dirname $0`/manifest-a.yml &
cf push -f `dirname $0`/manifest-b.yml &
cf push -f `dirname $0`/manifest-c.yml &
