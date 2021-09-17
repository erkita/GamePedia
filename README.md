# GamePedia
--------------

### Overview
Data-driven web application that allows users to:<br />
* Discover more information about searched games (description, release date, price, picture, developers, publisher, tag, game platform, number of players, genre, reviews)
* Find, create, delete, and update 
  * game information 
  * user account
  * user reviews
<br />

### Design
* Data is loaded and their tables (Games, Genre, Platforms, etc.) are created in ```GamePedia_v6```
* In the ```java``` folder, application is divided into a layering model
    * data access layer - contains data access object which executes specific data operations without exposing details of the database
    * model - has classes of basic objects needed for application (Games, Developers, Users, GivenReviews, etc.)
    * servlet - executes the action requested and sends data to view

<br /> *General work flow of the design*: <br />
Web browser sends http request to servlet &#8594; servlet gets model object with data from MySQL &#8594; servlet redirects to webpage (JSP) with model data &#8594; webpage is displayed with model data on browser<br />
 
<br /> *UML Diagram of Model*: <br /> 
<img width="817" alt="Screen Shot 2021-09-16 at 10 08 54 PM" src="https://user-images.githubusercontent.com/82434097/133727842-88dc9ed4-8010-475b-b4ec-86c79267357b.png">
