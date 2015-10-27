# Wish SDK for PHP

## Documentation

http://merchant.wish.com/documentation/api

## Install Instructions

Download composer:

````
curl -sS https://getcomposer.org/installer | php
````

Add wish/wish-sdk-php as a dependect in your project's composer.json

````
{
  "minimum-stability": "dev",
  "require":{
      "wish/php-sdk":"*"
  }
}
````

Run the following:
````
php composer.phar install
````


Put the following at the top of your file:

````
require 'vendor/autoload.php'
````

Sample
````php
<?php 
require_once 'vendor/autoload.php';

use Wish\WishClient;

$token = 'ACCESS_TOKEN';
$client = new WishClient($token,'sandbox');


print "RESULT: ".$client->authTest();
````
