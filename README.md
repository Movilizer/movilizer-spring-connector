# Movilizer Spring Connector

Movilizer Spring Connector is the easiest way to integrate your java based enterprise backends or to start up a new
stand-alone app that leverages the Movilizer technology full potential.

This project is meant to be deploy as a production ready stand-alone java app ran with `java -jar` or as a WAR file in
your servelt container of choice. If you don't want to use spring in you environment take a look at the webservice
compilation at https://github.com/Movilizer/movilizer-webservice to access the cloud directly.


## Installation

First of all, you need to know how the Movilizer Platform operates and how the web service is used to issue commands to
cloud. For this you have comprehensive documentation at https://help.movilizer.com. In order to be able to access cloud
and use the platform you'll need a "System Id" and its password, get one at https://movilizer.com/contact.html.

Once you have the prerequisites set, we need to build one of the dependencies from source. Please go through the
following commands:

```bash
git clone --branch 14.11.1.3 --depth 1 https://github.com/Movilizer/movilizer-webservice.git
cd movilizer-webservice
mvn install
```

With the dependencies installed then we have to install the movilizer connector in the local repository so we can use it
later in our projects. To do so execute this commands:

```bash
git clone --branch 1.0 --depth 1 https://github.com/Movilizer/movilizer-spring-connector.git
cd movilizer-spring-connector
gradlew installApp
```

If both installation were successful you're all set to start developing your first mobility app with Movilizer.


## Your first app

For this example we'll only showcase basic functioning of the connector. If you want to see a more complete example go
to http://example.com

Our first app will be a reader app. Our user we'll capture readings and we'll print them to the standard out as they
come to our connector.

### Requirements

### Running a sample and debugging it

### Deployment

### Monitoring the app

## Getting help

## Reporting Issues

## License