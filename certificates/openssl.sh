openssl cms -inform PEM -cmsout -print -in yandex.kassa.pkcs7.checkorder
openssl smime -inform PEM -verify -in yandex.kassa.pkcs7.checkorder -CAfile payment_center_2014.pem
