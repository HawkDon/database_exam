# Database exam project

Made by
- Mikkel Lindstrøm Hansen - cph-mh643@cphbusiness.dk
- Mathias Bartels Jensenius - cph-mj561@cphbusiness.dk
- Oliver Scholz Lønning - cph-ol31@cphbusiness.dk
- Mathias Brinkmann Igel - cph-mi73@cphbusiness.dk


### Prerequisites
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)
- [Bash](https://www.gnu.org/software/bash/)
- [Java 11+](https://www.oracle.com/java/technologies/javase-downloads.html)


### Launch the project:

./start.sh  

### The API
Postgress can be found at port 5000  
MongoDB can be found at port 3000  
Redis can be found at port 4000  
Neo4j can be found at port 8080 and 7474 for dashboard  

Syntaxes
Postgres syntax  
``/courses/{id}`` GET / DELETE   
``/courses/`` POST with Request body: ``{"Title: "title", "x":"x"}``   
``/courses/{id}`` PUT with Request body: ``{"Title: "title", "x":"x"}``   

MongoDB Syntax  
``/courses/{id}`` GET / DELETE  
``/courses/`` POST with Request body: ``body: courses: [ {}, {}]``   
``/courses/{id}`` PUT with Request body: ``body: courses: [ {}, {}]``   

Neo4J Syntax  
``/course?id={id},name={name}`` GET  
``/course?id={id}`` DELETE  
``/course`` POST / PUT With request body ``{"Course":{},"Difficulty":{},"Institutions":[{}],"Instructors":[{}],"Tags":[{}]``

Redis syntax
``/courses`` GET all


### Oops
If running on windows, make sure all bash scripts are with format LF. This can be checked with visual studio code for example  
When running make sure that all these ports are open and not occupied. The following are the default ports for the applications and if you have one of them running, we cannot start the services.
- 6379 - Redis
- 7687 - Neo4j
- 27017 - mongoDB
- 5432 - prostgress
