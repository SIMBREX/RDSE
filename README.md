# RDSE
[![VERSION](https://img.shields.io/badge/version-1.0-blue.svg?longCache=true&style=for-the-badge)](https://github.com/SIMBREX/RDSE/releases) [![GET](https://img.shields.io/badge/-DOWLOAD-orange.svg?longCache=true&style=for-the-badge)](https://github.com/SIMBREX/RDSE/releases) [![GET](https://img.shields.io/badge/-Android%20App-yellow.svg?longCache=true&style=for-the-badge)](https://play.google.com/store/apps/details?id=com.simbrex.encryptit) [![GET](https://img.shields.io/badge/-Web%20Version-yellow.svg?longCache=true&style=for-the-badge)](https://rdse.simbrex.com)

[![GET](https://img.shields.io/badge/-JAVASCRIPT-brightgreen.svg?longCache=true&style=for-the-badge)](https://github.com/SIMBREX/RDSE/releases) [![GET](https://img.shields.io/badge/-PHP-lightgrey.svg?longCache=true&style=for-the-badge)](https://github.com/SIMBREX/RDSE/releases)

## Installation
[Download](https://github.com/SIMBREX/RDSE/releases) the file `RDSE.java` and copy to any convenient place in your project.

![Example image](https://rdse.simbrex.com/src/github/install_1.png)

## How to use
* Create an object of class `RDSE`:
```java
  RDSE n = new RDSE();
```

* Encrypt and decrypt data can be methods `encrypt` and `decrypt`:
```java
  RDSE rdse = new RDSE();
  byte[] data, key; // Any data in byte format
  byte[] ecryptedData = rdse.encrypt(data, key);
  byte[] originalData = rdse.decrypt(ecryptedData, key);
```

* You can set the encryption depth by specifying its third parameter:

```java
  RDSE rdse = new RDSE();
  int depth = 100; // The greater the value, the more operations must be performed to decrypt.
  byte[] encryptedData = rdse.encrypt(data, key, depth);
  byte[] originalData = rdse.decrypt(encryptedData, key, depth);
```

## Text Encryption Assistant
To encrypt text data, we recommend using the `RDSEText` class (you need the main `RDSE` class to work). [Download it](https://github.com/SIMBREX/RDSE/releases) and place it somewhere near the main class.

![Example image](https://rdse.simbrex.com/src/github/install_2.png)

* You can also encrypt and decrypt using the `encrypt` and `decrypt` methods:

```java
  RDSEText rdseText = new RDSEText();
  String text = "My text";
  String key = "My key";
  String encryptedText = rdseText.encrypt(text, key);
  String originalText = rdseText.decrypt(encryptedText, key);
```

* You can set the encryption depth using the `setDepth` method:
```java
  RDSEText rdseText = new RDSEText();
  String text = "My text";
  String key = "My key";
  rdseText.setDepth(100);
  String encryptedText = rdseText.encrypt(text, key);
```

* The main feature of the class `RDSEText` is the ability to set your own wrapper on the cipher. This is done using the `setCharSet` method:
```java
  RDSEText rdseText = new RDSEText();
  String text = "My text";
  String key = "My key";
  char[] charSet = new char[]{'a', 'b', 'c'};
  // The number of characters must be at least 3!
  // Characters should not be repeated!
  rdseText.setCharset(charSet);
  String encryptedText = rdseText.encrypt(text, key);
  // When decrypted, the character set must match the set used for encryption!
  String originalText = rdseText.decrypt(encryptedText, key);
```

## License
    Copyright 2018 Simbrex Studio
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
