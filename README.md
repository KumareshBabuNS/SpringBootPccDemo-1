# Spring Boot Pivotal Cloud Cache Demo

The following demo shows how to use Pivotal Cloud Cache from a Spring Boot application.  This also can run standalone against a local Gemfire cluster.


## Steps 

Create a Pivotal Cloud Cache service instance named `cloudcache`.

```
$ cf create-service p-cloudcache extra-small cloudcache
```


Connect to your PCC service using  *gfsh* as described [here](https://docs.pivotal.io/p-cloud-cache/1-0/developer.html) and create the `employee` region.

```
gfsh>create region --name=employee --type=REPLICATE
```
Build the App
```
$ cd SpringBootPCCDemo
$ mvn package
```

Push to Pivotal Cloud Foundry

```
$ cf push
```

Create some data

```
$curl -X POST -H'Content-type: application/json' <app-base-url>/employees -d'{"id":1,"firstName":"First","lastName":"Last"}'
```

Get the data
```
$curl <app-base-url>/employees/1
```

Run a Query
```
$curl <app-base-url>/employees?q="firstName='First'"
```


