# yandex_payments
Here are some classes that implement Spring MVC - Yandex payments integration.

# PKCS#7 parsing
The point of  interest is PKCS#7 integration. With a help of these classes and slight modification you can parse and verify PKCS#7 packets. All core cryptography is implemented with Bouncy Castle libraries.

# Certificate import

To use Yandex certificates you should convert them manualy from pem to der format. Sample (real) certificates added with CRLS.
