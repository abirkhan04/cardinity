Task Manager APIs


IMPLEMENTATION:

DB and REST APIS for task manager are created using JSON format for interchange.
Endpoints are secured with Spring Security. Users, Roles are database managed and configurable.
We need to insert USER and ADMIN roles in the database once created automatically for the first time. 

USER can only access own tasks and projects .Authentication credentials are stored in DB

USER and ADMIN are able to:

• Create Project

• Get all projects

• Delete project

• Create task

• Edit task (change description, status, due date). Closed tasks cannot be edited.

• Get task

• Search tasks
   Get all by project
   Get expired tasks (due date in the past)
   By status
   

ADMIN additionally are able to:

• Get all tasks by user

• Get all projects by user



Task properties includes:

• Description (Mandatory)

• Status (Mandatory), OPEN/IN_PROGRESS/CLOSED

• Project (Mandatory)

• Due date (Optional)

Project properties:

• Name (Mandatory)


REST API are validating data and return validation errors if data is invalid
DB tables are automatically created (if doesn't exist) on application startup.
App and DB designs are flexible enough to allow easily adding new properties for task and 
project


Instructions to run the application:
1. If you have maven installed then clone the repository. 
2. Go to project root folder and run: mvn clean package -Dmaven.test.skip=true
3. The will create task-manager.jar in the target directory.
4. Please take the jar (task-manager.jar) file , don't take original file from there.
5. Open MySQL and create a database named:  taskmanager
6. Open windows powershell and run : java -jar task-manager.jar.
7. Above command will create all necessary tables.
8. take the user.sql file from the root folder. Open it and run the queries which will create use `evaldas` as an administrator.
9. Go to: http://localhost:8080/swagger-ui.html#/  . You will get swagger file to test all APIs. 
