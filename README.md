# run configuration

this is the run configuration for the ldfs master server
you should change the master server port by using spring configuration

## spring configuration properties

```
server.port={MASTER_SERVER_PORT}
```

example
```
server.port=8080
```

## How to connect to local in memory H2 DB
- browser base console : http://localhost:8080/h2-console
- intellij data console jdbc url : `jdbc:h2:tcp://localhost:9092/mem:testdb;OLD_INFORMATION_SCHEMA=TRUE`
- precaution: database option -> advanced -> expert option -> Introspect using jdbc metadate  
```
id: sa
password:
```
- The in memory H2 DB can only run while the server is running because the server provides a tcp server wrapped around the in memory H2 DB you can access it.

## How to connect to swagger 3 documentation
```
http://localhost:8080/swagger-ui/index.html
```

---


# what is ldfs?

ldfs short for **light distributed file system** is a project that aims to replicate a simple distributed file system for "fun".

[MIT 6.824 distributed file system](https://pdos.csail.mit.edu/6.824/index.html)

[CMU distributed file system assignment](https://www.andrew.cmu.edu/course/14-736-s20/applications/labs/proj3/proj3.pdf) 

this project will rougly follow the requiremnts in the linked resources but there might be lot of variants.

This is a hobby project if theres is any kind of copyright problem with linked resources I do not intent to do copyright fraud please contact the repository's owner if you want to shutdown the project.



---



run configuration
this is the run configuration for the ldfs client desktop application you should change the master server address in the gradle properties file

gradle properties
masterServerAddress={MASTER_SERVER_ADDRESS}
example

ex) masterServerAddress=127.0.0.1:8080
