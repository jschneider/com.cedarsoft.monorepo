# Key-Erstellung
# ==============================

openssl req -new -x509 -nodes -out test.crt -keyout test.key -days 999999 -subj "/C=DE/ST=BW/L=Gomaringen/CN=invoices-cedarsoft"
openssl pkcs8 -topk8 -inform PEM -in test.key -outform DER -nocrypt -out test.der
