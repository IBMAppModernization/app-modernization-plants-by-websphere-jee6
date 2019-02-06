#!/bin/bash

if [[ $1 =~ '^(http(s)?)://[-A-Za-z0-9\+&@#/%?=~_|!:,.;]*[-A-Za-z0-9\+&@#/%=~_|]$' ]]; then
  echo "Fatal error: Invalid URL format"
  exit 1
fi

if [ ! -f k8s/pbw-liberty-mariadb-credentials.yaml ]; then
  echo "Fatal error: Can't find k8s secrets file on path k8s/pbw-liberty-mariadb-credentials.yaml"
  exit 1
fi

# store the whole response with the status at the and
HTTP_RESPONSE=$(curl --post301 --silent --write-out "HTTPSTATUS:%{http_code}" -L -X POST -H "Content-Type: text/plain" -d @k8s/pbw-liberty-mariadb-credentials.yaml $1)

# extract the body
HTTP_BODY=$(echo $HTTP_RESPONSE | sed -e 's/HTTPSTATUS\:.*//g')

# extract the status
HTTP_STATUS=$(echo $HTTP_RESPONSE | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

if [ $HTTP_STATUS -ne 200 ]; then
   if [ $HTTP_STATUS -eq 401 ]; then
      echo "$HTTP_BODY"
      exit 1
   else
      echo "Fatal error:  invalid URL for database connection validation service or service not available"
      exit 1
   fi
fi

# Validate the body
if [ "$HTTP_BODY" == "Database connection successful" ]; then
  echo "$HTTP_BODY"
  exit 0
else
  echo "Fatal error:  invalid URL for database connection validation service"
  exit 1
fi
