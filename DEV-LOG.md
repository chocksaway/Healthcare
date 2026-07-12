Development Log - Miles Davenport

Install PostgreSQL database and client, load data.
 + I experimented with installing PostgreSQL locally, but decided to use docker-compose.
 + + There is a docker-compose.yml file, which is in the root of the project:
 + + + The PostgreSQL database credentials are hardcoded for development and testing purposed.  In a production environment these would be stored as environment variables or in a secure keystore.
 + + + The volume ./data/postgres is used to persist the database data between docker container restarts.
```
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_USER: ${DB_USERNAME:-myuser}
      POSTGRES_PASSWORD: ${DB_PASSWORD:-mypass}
      POSTGRES_DB: ${DB_NAME:-mydb}
    volumes:
      - ./data/postgres:/var/lib/postgresql/data
      - ./scripts/init:/docker-entrypoint-initdb.d:ro
    ports:
      - "5432:5432"
```
A core part of this functionality is running the numbered scripts in scripts/init:
+ create database schema and sequences.
+ load patient data
+ load action data
+ post import - adding table constraints.

Convert schema and SQL dump to use PostgreSQL database for development and testing.
    
Focus on creating the schema and loading the data in a repeatable way.
+ Unit test for checking sequences - AfterDataLoadedSequenceTest.java
+ Unit test for checking One Patient to Many Action relationship - ActionPatientJoinTest.java

###  Please note:    
+ There is an issue with the CONSTRAINT patient_gender_check - this does not work because the gender column is a character varying.  It is not declared NOT NULL.
+  + I have not changed the schema as I did not want orphaned data.  I have added a visual check (the table row has a red border) in the patients.html template.

Create Patient and Action entities.
+ I have created the Patient and Action entities, which are annotated with JPA annotations.
+ I have used @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) for Patient Id and Action Id.
+ I have used @ManyToOne for the Action to Patient relationship, and @OneToMany for the Patient to Action relationship.
+ I have skipped ActionDTO::setPatient to avoid circular dependencies - actionMap.addMappings(m -> m.skip(ActionDTO::setPatient)

Create DTO for Patient and Action entities
+ I have implemented DTO for both Patient and Action. 
+ I'm using a new API ModelMapper to map between entities and DTOs.
+ I have avoided skipped ActionDTO::setPatient to avoid circular dependencies - actionMap.addMappings(m -> m.skip(ActionDTO::setPatient)
+ + Deliberate decision to limit the number of Patient and Action attributes to return in the ActionDTO and PatientDTO
+ +  Limited to just the data required for the website.

Create Controller endpoints for Patient and Action entities
+ I have added a WebController - which integrates with Thymeleaf templates.
+ + Top level index.html
+ + List of patients.html
+ + Single patient.html

+ /index - find the invited, registered, and discharged figures
+ + patientService.countInvited();
+ + patientService.countRegistered();
+ + discharged = patientService.countDischarged();
+ + Pass to index.html - Render a graph

+ /patients - has a page input parameter - calls patientService.listPage(pageIndex, 10); // 10 to a page.
+ + Pass to patients.html
+ + /patients/{id} - patient id - patientService.getActionsForPatient(id);
+ + Pass to patient.html
+ + /api/patients/search - has an optional query string query parameter - this is used by the search box feature on the patients.html page.

Create service layer for Patient and Action entities
+ search - calls patientRepository.searchByQuery - return a List of PatientDTO
+ listAll - calls patientRepository.findAll() - returns a List of PatientDTO
+ listPage - calls patientRepository.findAll(pg) - with a page - returns a List of PatientDTO
+ getPatient - patientRepository.findById - returns a PatientDTO
+ getActionsForPatient - actionRepository.findByPatientIdOrderByWhenRecordedDesc - returns a List of ActionDTO

Create repository layer for Patient and Action entities
+ PatientRepository 
+ + countInvited - count
+ + countRegistered - count
+ + countDischarged - count
+ + searchByQuery - search givenName, familyName, NHS number, hospital ID.

----

###  Please note: 

For searchByQuery - I have escaped backslashes and underscores in the SQL dump to prevent SQL injection.
Spring Data JPA binds parameters to prepared statements which prevents SQL injection; 

I am aware that the searchByQuery does a SQL LIKE query, which is not the most efficient way to search (columns) data.
In a production system, I would make sure that all columns being searched were indexed, or would consider using a full-text search engine such as Elasticsearch or SOLR.

The ADD Constraint / Unique or Primary key implicitly creates an index on the column.  I would prefer to create an index explicitly (or comment).

----


Create unit tests for service layer
+ PatientServiceSpringTest and PatientServiceTest - uses Mockito to test PatientService, PatientRepository, and ActionRepository

Create integration tests for WebController endpoints
+ Using Playwright framework to test the index.html -> patients.html -> patient.html flow:
+ + Go to http://localhost:8080" - check for View patients link - click the link
+ + Go to patients.html - Assert patients header and search label text - Click the first patient link in the table
+ + Go to patient.html - On patient page assert action table headings - When, Activity, Context, and Module.

Develop front end using Thymeleaf templates for Graph, Patient and Action views
+ index.html - graph showing invited, registered, and discharged patient numbers.

Think about look and feel and how to make it user-friendly:
+ I have applied a single styles.css file.
+ chart.js - this has been used to render the index.html chart
#end



