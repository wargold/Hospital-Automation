# Emergency Room Automation
#### Authors: War Ahmed

##### Problem:
Patients arrive at the emergency room either by showing up on their own or else by ambulance. When they arrive, the triage nurse assigns every patient a priority based on the severity of their condition (with 5 being the gravest and 1 being the least pressing) and assign them to a medical treatment team. The emergency room contains 5 different such teams and every team is competent to deal with a 3 different medical issues, out of a list of 10 different medical issues (eg. impact trauma, chest pains, seizures, allergic reactions a.s.o) in total. The patients are then put in their assigned medical team’s queue and when they reach the top of the queue they will get to see their medical team. Patients with higher priorities will be moved up the queue before patients with lower priorities, but inside every priority classification, they should move up on a first come, first serve basis. During treatment, the team may utilize up to 3 different medical procedures (procedures are unique for every medical issue) and decide to provide the patient with up to 3 drugs (drugs are common for all issues). After the immediate treatment, the physician in charge of the team decides whether to release the patient back home, or else to commit the patient to a general hospital ward for further care.

##### Solution:

Based on this problem I’ve designed an Entity–relationship model that correspond to the problem.  You can see my E/R diagram for the problem [here](https://github.com/wargold/Hospital-Automation/blob/master/ERDiagram.jpg). Then I’ve also created SQL-tables for saving information about a patient, medicine available, the medical procedure available etc. see this [folder](https://github.com/wargold/Hospital-Automation/tree/master/SQL-Table).

I’ve then created two different front-end menus, one for the nurse and the other for the doctor. When a patient is released all their data is logged in a specific log in the database (Medical Record table) where we save all the information about the patient visit in the hospital.

In order to run the code you will need to set up the class path for the postgresql.jar file, form information click [here](https://jdbc.postgresql.org/documentation/head/classpath.html). You have to also load all the SQL-tables (you can find them in the SQL-Table folder) into your database. For this purpose I used the Postgres.app, which is simple to use, just launch the app on your mac computer and you got a PostgreSQL server running in no time on your computer.Then just compile and run the code.

_This lab was developed during my fourth semester (2017) for the course, [DD1368](https://www.kth.se/student/kurser/kurs/DD1368?l=en) Database Technology._
