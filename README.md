# yandex_payments
Here is some classes that implement Spring MVC - Yandex payments integration.

# PKCS#7 parsing
The point of  interest is PKCS#7 integration. With a help of this classes and slight modification you can parse and verify PKCS#7 packets. All core cryptography is implemented in Bouncy Castle libraries.

# Certificate import

To use yandex certificates. You should convert them manualy from pem format to der. Sample (real) certificates added with CRLS.
