# TAMUCourseSearch

This is a Java GUI put together through the use of JavaFX, JDBC (Java Database Connectivity),
and a MySQL database set up on the campus servers. The objective was to use a filer-search method
for courses that differed from the current tree-like structure that the university uses for course searching.
This project was as a part of CSCE 315 - Programming Studio, and my co-writers were fellow students Ifrah Tariq
and Levi Uzodike.

We used a (very large) PDF file containing all the courses with their associated information (times, location, instructor, section, CRN, title, etc)
to create a MySQL database on the campus servers.

Ifrah parsed the PDF file into a .csv file so that we could open it in Microsoft Excel and make appropriate adjustments
based on our desired format for displaying the search results.

Levi derived the MySQL commands used to search for courses as well as a means of searching for rooms that are available at certain times.
If that's unclear, an example would be an organization desiring to find any room that can hold at least 60 people for 2 hours in the Physics Building. Another would be a solitary student trying to find an empty room in the computer science building that's available for an hour at any time on Mondays between 12 pm and 3 pm.
This application allows the user to make that kind of search, which we call Room Availability Search.
Levi also worked with me to write the JDBC code.

I wrote the GUI using JavaFX as well as some of the JDBC code to connect with the database, issue commands, and retrieve results. I wrote all of the fxml and Java code except for some of the JDBC code in the generateResults() function of RoomAvailabilitySearch.java as well as CourseSearch.java.
