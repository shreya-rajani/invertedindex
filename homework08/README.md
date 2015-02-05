Homework 08: Cookie Monster
Due Nov 15, 2013 by 11:59pm  Points 10
Summary
For this homework, use Jetty, servlets, cookies, and HTML forms to create a "Cookie Monster" website that displays all of the cookies stored for the website, and allows users to add or delete saved cookies.

Details
Create a Jetty server running on port 8080 and a servlet that:

Responses to GET requests by displaying a web page that shows the name and value for all cookies currently saved for the site, and displays a form allowing users to add/edit their cookies.

Responses to POST requests by creating and saving a new cookie based on the name/value provided in the form, or deletes a cookie if necessary.

Here is an example website:

Screen Shot 2012-11-08 at 9.14.55 PM.png

If no cookies are stored, output a special message. For example:

Screen Shot 2012-11-08 at 9.13.40 PM.png

The exact formatting and design is up to you, but you are encouraged to use the code examples from class as a starting point.

Submission
Submit your work to SVN using Eclipse at:

https://www.cs.usfca.edu/svn/username/cs212/homework08
where username is your USF Connect username. If you are using Eclipse properly, then you should have the following files (at a minimum):

 https://www.cs.usfca.edu/svn/username/cs212/homework08/src/CookieMonster.java
You may add additional classes if necessary.
