# Webapps Commons [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/bellotapps/webapps-commons.svg?branch=master)](https://travis-ci.org/bellotapps/webapps-commons)

Project containing a bunch of commons stuff that can be used for building web applications.

**Java 10 is required for this library to be used**

## Description
This project contains a bunch of libraries defining and implementing stuff that can be used for building web applications.

## How to use it?

### Get it!

By the time, the only way to obtain these libraries is by downloading the source code and manually build it.

#### Build from source
**Maven is required for this**

```
$ git clone https://github.com/bellotapps/webapps-commons.git
$ cd webapps-commons
$ mvn clean install
```

**Note:** You can also download the source code from [https://github.com/bellotapps/webapps-commons/archive/master.zip](https://github.com/bellotapps/webapps-commons/archive/master.zip)

**Note:** If you just want to get the JAR files, you must use the following command which won't install the libraries in your local repository.

```
$ mvn clean package
```

### Bill of Materials

This project includes a Bill Of Materials in order to make it easier to import the libraries. Include the following in your ```pom.xml```.

```xml
<dependencyManagement>
    <dependencies>
        <-- ... -->
        <dependency>
            <groupId>com.bellotapps.webapps</groupId>
            <artifactId>webapps-commons-bom</artifactId>
            <version>${webapps-commons.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <-- ... -->
    </dependencies>
</dependencyManagement>
```

After adding the ```webapps-commons-bom``` artifact as an imported managed dependency, you can start using the different libraries in your project.

### Dependencies module

This project includes a module defining a set of dependencies that might be used by users of these libraries. It is also used by the rest of the modules in this project.

To use this module, include the following in your ```pom.xml```.

```xml
<dependencyManagement>
    <dependencies>
        <-- ... -->
        <dependency>
            <groupId>com.bellotapps.webapps</groupId>
            <artifactId>webapps-commons-dependencies</artifactId>
            <version>${webapps-commons.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <-- ... -->
    </dependencies>
</dependencyManagement>
```


## Development

This instructions will set up the development environment in your local machine.

### Prerequisites

1. Clone the repository, or download source code:

    ```
    $ git clone https://github.com/bellotapps/webapps-commons.git
    ```
    or

    ```
    $ wget https://github.com/bellotapps/webapps-commons/archive/master.zip
    ```

2. Install Maven
    #### Mac OS X
	```
	$ brew install maven
	```

	#### Ubuntu
	```
	$ sudo apt-get install maven
	```

	#### Other OSes
	Check [https://maven.apache.org/install.html](https://maven.apache.org/install.html).

### Build

1. Change working directory to project root

    ```
    $ cd <project-root>
    ```

2. Build artifacts

    ```
    $ mvn clean package
    ```

## License

Copyright 2018 BellotApps

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
