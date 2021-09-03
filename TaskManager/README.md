Task Manager APIs


IMPLEMENTATION:

DB and REST APIS for task manager are created using JSON format for interchange.
Endpoints are secured with Spring Security. Users, Roles are database managed and configurable.
We need to insert USER and ADMIN roles in the database once created automatically for the first time. 

  USER can only access own tasks and projects
  Authentication credentials are stored in DB

USER and ADMIN are able to:
• Create project
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
