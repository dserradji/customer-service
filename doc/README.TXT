Generate a key + keystore
-------------------------

keytool -genkey -alias customerservice -keyalg RSA -keysize 2048 -keystore servicestore.jks -validity 3650

pwd: qwerty
first and last name: localhost (THIS IS IMPORTANT)
all other fields can be left to Unknown

Generate security certificates to use with cURL when HTTPS+OAuth2
-----------------------------------------------------------------
keytool -exportcert -rfc -keystore servicestore.jks -storepass qwerty -alias customerservice > servicestore.pem
keytool -importkeystore -srckeystore servicestore.jks -destkeystore servicestore.pfx -deststoretype PKCS12 -srcalias customerservice -deststorepass qwerty -destkeypass qwerty
openssl pkcs12 -in servicestore.pfx -out servicestore.p12 -nodes

cURL
----
curl --cacert servicestore.pem --cert servicestore.p12 -v -u clientId:clientSecret https://localhost:8443/oauth/token -d grant_type=client_credentials
Copy the token returned in "access_token"
export TOKEN={copied_token}

curl -i -X POST -H "Content-Type:application/json" -H "Authorization: Bearer $TOKEN" https://localhost:8443/clients -d '{"first_name":"John","last_name":"Doe","birth_date":"1990-01-01","client_type":"PERSON","address":{"street_name":"Wellington","country":"canada"}}' --cacert servicestore.pem --cert servicestore.p12
curl -i -X GET -H "Content-Type:application/json" -H "Authorization: Bearer $TOKEN" https://localhost:8443/clients --cacert servicestore.pem --cert servicestore.p12


