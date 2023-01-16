**Name: Ionescu Matei-Ștefan**  
**Group: 323CAb**

# OOP Homework #3 - POO TV V2 #

## Summary: ##

**POO TV** implements the core backend mechanics of platforms like HBO GO
and Netflix using OOP concepts. The application receives a list of actions
from a file in _json_ format and generates a new _json_ file with the
required output for each action.

This project builds upon the Homework #2 - POO TV and represents an improved
version with more features. In this README I will be taking about the new
features and their implementation. For more information about the project check
the README from the first version of the application available at
https://github.com/Matei77/OOP-POO-TV.

## Implementation: ##

### Design Patterns Used ###

#### 1. Singleton Design Pattern ####
The core of the application is still represented by the `PlatformEngine` class.
This class has been designed using the **Singleton Design Pattern** to allow 
only one instantiation of the class and acts as a database to hold information
about the movies and users on the platform, as well as the current user, the
movies the current user can see and the current page the user is located in.

#### 2. Factory Design Pattern ####
Each page has specific actions that can only happen on that page. In my
implementation each page represents a class that implements the _Page_
interface and is created using the **Factory Design Pattern**. The 
_PageFactory_ is instantiated using the **Singleton Design Pattern** to save 
memory by allowing only one instance of the _PageFactory_. Each page class
overrides the required methods from the _Page_ interface. The default output
for each method in the interface is the error message.

#### 3. Observer Design Pattern ####
When a new admin action is executed, all observers are notified. Admin actions 
are adding and deleting movies to and from the database. The `AdminActions` 
represents the observable class, and the `User` class is the observer.
The `AdminActions` class will store the list of observers and will notify them 
when an action is executed. The `notifyObserver` method from the `User` 
class will then decide if the user should receive the notification given by the
admin.

#### 4. Command Design Pattern ####
A new feature has been implemented, the back button, and it is using the 
**Command Design Pattern**. Changing the page is now executed using the 
`ChangePageInvoker` which hold a history of successful change page commands 
and implements the `execute` and `undo` methods that goes to the next page, 
respectively goes back to the previous page.

### Data structure ###
#### User ####
For each user the platform holds information about his credentials, his
purchased, watched, liked and rated movies, the type of his account,
the number of free premium movies he has available, his tokens and his balance.
In this version of the application the notification system was introduced 
and users now receive notifications from the platform. The `Notification` 
class was implemented for this reason.

#### Movie ####
For each movie the platform holds information about the name, the year, the
duration, the genres, the actors, the countries banned in, the number of
likes and the ratings. Movies now have an improved rating system that allows 
changing the user's rating. This is implemented using the `Rating` class.


### New Actions ###
When the `runEngine` method is called movies and users databases will be
updated with the information contained in the input _json_ file and the actions
received will be executed. The output will be shown in a new _json_ file called
_results.out_.

#### User Actions ####
The following new user actions were introduced:

1. **subscribe**

    _This action allows a user to subscribe to a genre._

    When the _subscribe_ command is given, the `subscribe()` method will be 
   called, and it will check if the movie currently seeing has the selected 
   genre and if the user was not already subscribed to it. If this is the 
   case, the user will now receive notifications when movies from that genre 
   are added.

2. **back**

    _This action allows a user to go to the previous page._

    When the _back_ command is given, the `ChangePageInvoker.undo()` method 
   will be called, and it will go to the previous page if possible. 
   Otherwise, it will output an error.

#### Admin Actions ####
The following admin actions were introduced:

1. **add**

    _This action allows admins to add a movie to the database at any time._ 

    When the _add_ command is given, the `AdminActions.addMovieToDatabase()` 
   method will be called, and it will add the movie given in input to the 
   database. Users that subscribed to any of the genres of the movie will be 
   notified about the new movie.

2. **delete**

   _This action allows admins to delete a movie from the database at any time._

   When the _delete_ command is given, the `AdminActions.deleteMovieFromDatabase()`
   method will be called, and it will delete the movie given in input from the
   database. Users that have purchased the movie will be notified about the 
   deleted movie and reimbursed.

The `AdminActions` class is implemented using the **Observer Design Pattern**.
Admin actions are observable by the observers (the users).


### Recommendation System ###
At the end of the execution of all actions, if there is a premium user 
logged-in they will be notified about a movie they should watch. The movie 
recommendation system is implemented in the `giveRecommendation()` method 
from the `User` class. And find a movie the user would probably enjoy by
analyzing the user's liked genres and the most popular movies on the platform.
The most popular movie that the user hasn't watched yet is recommended. The 
`LikedGenre` class was implemented to allow this new feature of the platform.