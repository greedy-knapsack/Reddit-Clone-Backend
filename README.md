# Reddit-Clone-Backend
This is the complete backend for a reddit clone application, with all endpoints getting authorized and authenticated by spring security.
You can clone the repository and run command mvn clean install, to add respective dependencies in pom.xml file.(Since it is a maven build)
To check all apis(to post,read subreddit or posts), you need to first register.
You need to configure fake mail service credentials and DB configuration(MySQL/PostgreSQL).
After activation of user, can use login api which results in a JWT token, use that token in authorization header to access other apis.
