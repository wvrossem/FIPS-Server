FIPS-Server
==============

Project Overview
----------------

This repository is part of the project "An Extensible Framework for Indoor Positioning on Mobile Devices", which is the master thesis that I did in 2011-2012 at the Vrije Universiteit Brussel to achieve my "Master in Applied Computer Science". My promotor for this master thesis was [Prof. Dr. Beat Signer](http://www.beatsigner.com/). The thesis document can be found [here](https://www.dropbox.com/s/j0xehv5qodxh3id/Van%20Rossem%20-%202012%20-%20A%20FrameWork%20for%20Indoor%20Positioning%20on%20Mobile%20Devices.pdf).

The entire project is divided into several repositories:

* [FIPS-Datastore](https://github.com/wvrossem/FIPS-Datastore)
* [FIPS-Server](https://github.com/wvrossem/FIPS-Server)
* [FIPS-Tool](https://github.com/wvrossem/FIPS-Tool)
* [FIPS-Android-Offline](https://github.com/wvrossem/FIPS-Android-Offline)
* [FIPS-Android-Online](https://github.com/wvrossem/FIPS-Android-Online)

Server Usage
------------

This project includes the servlets and algorithms for the indoor positioning framework. It it used by creating a WAR file and copying this to the `webapps` folder of a tomcat (v 7) server. 

```
$ ant war
```

The two servlets `/DataUploadServlet` and `/PositioningServlet` are then available, which expect a `DataUploadRequest` and a `PositioningRequest` respectively.

Because one of the algorithms uses the ForkJoin framework that was introduced in Java 7, there is a problem when using older versions of Java and you first need to add this line to your `setenv.sh` file in the `bin` folder of your tomcat installation:

```                           
CATALINA_OPTS='-Xbootclasspath/p:/home/username/opt/apache-tomcat/lib/jsr166.jar'
```

Where you change this path to the location of this library (which can be found in the repository).

Once the server is running and the datastore is set up, it should then  be ready to be used by the [FIPS-Tool](https://github.com/wvrossem/FIPS-Tool) to upload data & test the algorithms.

License
-------

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.



