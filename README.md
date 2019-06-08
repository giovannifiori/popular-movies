# __Popular Movies__  
This is the second project of the Udacity's Android Developer Nanodegree.  

The project consists in a App that fetch the most popular and top rated movies from the themoviedb.org API and shows some info to the user.    
  
---
## Adding themoviedb.org API Key

For you to be able to access the themoviedb.org API, you need to setup your API access key.  

To get an API access key, [create an account](https://www.themoviedb.org/account/signup) or [login](https://www.themoviedb.org/login) if you already has an account and follow the instructions in the site.

__After you have your access key, follow the steps below.__  

1) Open the `grade.properties` file in __your home directory__ under `.gradle` directory (If the file not exists, you can create a new one).
   > For Linux, usually is at: /home/<Your Username\>/.gradle

   > For MacOS, usually is at: /Users/<Your Username\>/.gradle

   > For Windows, usually is at: C:\Users\\<Your Username\>\\.gradle
2)  In the `grade.properties` file, add:
    ```
        PopularMovies_ApiKey="<your access key here>"
    ```
Build the project and you are good to go. =)
