##Preconditions
***1、you must install php and curl first, if you are't install them yet, you can use the flower command do it.***
```shell
 $ sudo apt-get install php5-cli
 $ sudo apt-get install curl libcurl3 libcurl3-dev php5-curl
 ```
 ***2、copy config.simple.php to config.php
 ```shell
 cp config.simple.php config.php
 ```
 
 ***3、past you public and private key to the config.php***
 ```php
 public_key = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
 private_key = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
 ```
 ***4、run the script in linux command line***
 ```shell
 $ php -f create_ulb.php
 ```
