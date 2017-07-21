<h1>Spring Boot Pivotal Cloud Cache Demo</h1>

The following demo shows how to use Pivotal Cloud Cache from a Spring Boot application.

![alt tag](https://image.ibb.co/eGq3fk/pcc_springboot_1.png)

<h2> Steps </h2>

- Create a Pivotal Cloud Cache service instance as shown below

```
$ cf create-service p-cloudcache extra-small pas-pcc
Creating service instance pas-pcc in org pivot-papicella / space development as papicella@pivotal.io...
OK

Create in progress. Use 'cf services' or 'cf service pas-pcc' to check operation status.
```

- Install Spring Cloud GemFire Connector dependencies into your local Maven repo:

```
$ git clone -b v1.1.0 https://github.com/pivotal-cf/spring-cloud-gemfire-connector.git  
$ cd spring-cloud-gemfire-connector  
$ gradle clean install
```

- Connect to your PCC service using GemFire GFSH as shown below and create the "employee" region

```
gfsh>create region --name=employee --type=PARTITION
              Member                | Status
----------------------------------- | -------------------------------------------------------------------
cacheserver-PCF-PEZ-Heritage-RP04-1 | Region "/employee" created on "cacheserver-PCF-PEZ-Heritage-RP04-1"
cacheserver-PCF-PEZ-Heritage-RP04-3 | Region "/employee" created on "cacheserver-PCF-PEZ-Heritage-RP04-3"
cacheserver-PCF-PEZ-Heritage-RP04-0 | Region "/employee" created on "cacheserver-PCF-PEZ-Heritage-RP04-0"
cacheserver-PCF-PEZ-Heritage-RP04-2 | Region "/employee" created on "cacheserver-PCF-PEZ-Heritage-RP04-2"
```

- clone the project code as follows

```
$ git clone https://github.com/papicella/SpringBootPCCDemo.git
```

- Package as shown below

```
$ cd SpringBootPCCDemo
$ mvn package
```

- Edit manifest.yml to add the correct service name, if you used the name above you won't have to change this

```
applications:
- name: pas-pcc-springboot
  random-route: true
  path: ./target/springboot-pcc-demo-0.0.1-SNAPSHOT.jar
  buildpack: java_buildpack_offline
  services:
  - pas-pcc
```

- Push to Pivotal Cloud Foundry as follows

```
$ cf push
```

<hr />
Pas Apicella [papicella at pivotal.io] is a Senior Platform Architect at Pivotal Australia 
