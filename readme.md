# Database exam project

Made by
- Mikkel Hansen
- Mathias Jensenius
- Mathias Igel
- Oliver Lønning

### Launch the project:

./start.sh

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
``/course`` POST / PUT With request body {"Course":{},"Difficulty":{},"Institutions":[{}],"Instructors":[{}],"Tags":[{}]
