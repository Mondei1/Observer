# Observer-Client
**This project is still in development and is not published yet! Keep this in mind If some features maybe not work fine.**

This is the client that sends data to the backend where it get's processed and displayed on the frontend.

## Install
**Note:** Currently linux is supported only! This `package.sh` and `install.sh` file will only work under linux or unix-like operating systems.
So If you're under windows this process below will not work! Use the pre-packaged file on [spigot]() instead.

Make sure you have these dependencies installed:

* Maven (Debian `apt install maven` | [Arch Linux](https://www.archlinux.org/packages/community/any/maven/) `pacman -S maven`)
* Git (Debian `apt install git` | [Arch Linux](https://www.archlinux.org/packages/extra/x86_64/git/) `pacman -S git`)
* Java 8 JDK (Debian `apt install openjdk-8-jdk` | [Arch Linux](https://www.archlinux.org/packages/extra/x86_64/jdk8-openjdk/) `pacman -S jdk8-openjdk`)

Follow these steps to install and setup observer:

1. Clone this repository
    ```shell
    git clone https://github.com/Mondei1/Observer-Client.git
    ```
2. Change directory with.
    ```sh
    cd Observer-Client
    ```
3. If you never ran the Spigot BuildTools you have to execute `install.sh` first:
    ```sh
    sh install.sh
    ```
    The reason for that is that Maven need the SpigotAPI as dependency and the only way to get this is to build Spigot on your machine, so Spigot install's it to your local Maven repository.
4. Before you can execute `package.sh` make sure to open it and go to the bottom of the file
and replace the copy path's with your spigot/bungeecord plugin folder. But you can also uncomment it and copy it manually.
    ```sh
    ...
    # ENTER HERE YOU'RE SPIGOT/BUNGEECORD PLUGIN FOLDER, SO THIS SCRIPT WILL COPY THE FINAL JAR INTO YOUR PLUGINS FOLDER.
    cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"
    cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"
    cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"

    echo ""
    echo "Done :D Now you're ready to use Observer."
    ```
5. I recommend to edit the following file in this directory: `src/main/resources/config.json` to have a template config, so you don't have to edit each config file for each plugin. As example: You already setup the backend and frontend, you go to the frontend and the first you will see is an API-Key, Copy & Paste that thing into the above mentioned file under `backend => apikey`.

6. Now, execute the script with:
    ```
    sh package.sh
    ```
7. And now you're ready to use Observer. If you did step 4. and 5. right you just have to restart your bungeecord(s) and later your spigot(s).

## Data Privacy
All data that the client collect's will never be send to third parties. Everything get's directly send to your configured backend **only**.

## License
This entire project is licenced under the **GPL-3.0** licence. Read more [here](https://github.com/Mondei1/Observer-Client/blob/master/LICENSE).