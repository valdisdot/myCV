## myCV Application

This is a simple web-application, PET-project, based on Vaadin, Spring Boot and SQLite implementation of JDBC.

### How to start

#### Fill the application with your CV-content

- build a jar file with `mvn clean install -P production`
- go to the `target`
- find a jar file `mycv-***.jar`
- run the application in the builder mode with `java -jar mycv-***.jar -b`
- following the tips and masters, put the information about yourself like name, hobbies, pictures etc.
- save the info, you'll get a file `database.db`. 

Next, you can run the application on a local server or deploy it on the real one like AWS EC2. You can modify your personal information with `CLIBuilder` or make direct modifications in the `database.db` file with `sqlite3` cli-application or `DBBrowser`.

#### Deploy and run on the remote machine

- make a directory with the `application jar` file and `database.db` file
- get a certificate for allowing a connection to your cloud machine, make sure that you allowed `SSH-connection` on the server
- go to the terminal:

  1. make a dir `my_fancy_cv` (or any) in the home directory on the server machine:
  
    ``````
    ssh -i my_certificate.pem my_user_name@my_server_remote_address
    mkdir my_fancy_cv
    exit
    ``````

  2. copy `application jar` file and `database.db` file to the server
  
    ``````
    scp -i my_certificate.pem mycv-***.jar database.db my_user_name@my_server_remote_address:/home/ubuntu/my_fancy_cv/
    ``````

  3. run the application
  
    ``````
    ssh -i my_certificate.pem my_user_name@my_server_remote_address
    cd my_fancy_cv
    nohup java -jar run.jar > log.log 2>&1 &
    ``````



