# Observer-Client
**This project is still in development and is not published yet! Keep this in mind If some features maybe not work fine.**

This is the client that sends data to the bungeecord (as head-client) and the bungeecord will send this
information to the backend where it get's processed.

## Install
**Note:** Currently linux is supported only! This `package.sh` file will only work under linux or unix-like operating systems.
So If you're under windows this process below will not work! Use the pre-packaged file on [spigot]() instead.

Make sure you have these dependencies installed:

* Maven (Debian `sudo apt install maven` | [Arch Linux](https://www.archlinux.org/packages/community/any/maven/) `sudo pacman -S maven`)
* Git (Debian `sudo apt install git` | [Arch Linux](https://www.archlinux.org/packages/extra/x86_64/git/) `sudo pacman -S git`)

Follow these steps to install and setup observer:

1. Clone this repository
```shell
git clone https://github.com/Mondei1/Observer-Client.git
```
2. Change directory with `cd Observer-Client`.
3. Before you can execute `package.sh` make sure to open it and go to the bottom of the file
and replace the copy path's with your spigot/bungeecord plugin folder. But you can also uncomment it and copy it manually.:
```sh
...

# Copying final jar.
echo "--- Copy Observer.jar ..."

# ENTER HERE YOU'RE SPIGOT/BUNGEECORD PLUGIN FOLDER, SO THIS SCRIPT WILL COPY THE FINAL JAR INTO YOUR PLUGINS FOLDER.
cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"
cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"
cp Observer.jar "PATH/TO/YOUR/PLUGIN/FOLDER"

echo ""
echo "Done :D Now you're ready to use Observer."
```
4. I recommend to edit the following file in this directory: `src/main/resources/config.json` to have a template config, so you don't have to edit each config file for each plugin.

5. Now, execute the script with:
```
sh package.sh
```
6. And now you're ready to use Observer. If you did step 3. right you just have to restart your bungeecord/spigot.

## License
This entire project is licenced under the **GPL-3.0** licence. That means you can modify, distribute, commercial use, private use or patent use
but you have to licence your project under the same license! [Read more](https://github.com/Mondei1/Observer-Backend/blob/master/LICENSE)