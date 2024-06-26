# TournamentSystem Community Edition
This is a free to use version of our minecraft tournament system

# Installing it
### Linux (Recommended)
The install script only supports debian based distros!

On linux use the following steps to download the tournament system
* Navigate into the folder you want the files to be located in
* Run `wget https://cdn.novauniverse.net/tournament_system_ce/install.sh`
* Run `chmod +x install.sh`
* Run `./install.sh`
To start and stop the tournament user `docker compose up` and `docker compose down`. The tournament will automatically start one the install script is finished

### Windows
Before downloading make sure you have docker installed https://www.docker.com/get-started/
* Download the installer from the releases page https://github.com/NovaUniverse/TournamentSystemCEInstaller/releases/
* Put the installer jar in a the folder you want the tournament to be installed to
* Open cmd and navigate into the folder
* Run `java -jar TournamentSystemCEInstaller.jar <Version>` Replace &lt;version&gt; with the version you want to run. [Available versions can be found here](https://novauniverse.s3.amazonaws.com/tournamentsystem/ce/versions.json)
To start and stop the tournament use start.bat and stop.bat

# Logging in to the admin ui
Navigate to http://localhost:8080 and log in with `admin` as both username and password. The password can be changed in System > Manage accounts

If this is the first time running the tournament system you will have to configure the name and motd in the system settings

## Important info
* If you want to run the tournament in offline mode change OFFLINE_MODE to true in the .env file
* If you move the folder containing the tournament system you need to edit the .env file and change the DATA_DIRECTORY folder
* If you want to change the MySQL password you need to first change it in phpMyAdmin then in the .env file
* While testing we sometimes ran into an issue where it fails to update the map database. To resolve this restart the docker container on windows by running `stop.bat` then `start.bat` or on linux `docker compose down` then `docker compose up`
