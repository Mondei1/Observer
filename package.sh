#!/usr/bin/env bash
clear
echo "--- Starting Maven, to package Observer ..."
mvn -q package

# Check if package was successful.
if [ ! -f target/Observer/lib/bungeecord* ]; then
    echo "--- Seems that the packaging wasn't successful, hmm... Quiting."
    exit
fi

# Remove unnecessary packages.
echo "--- Removing unnecessary dependencies ... (spigot, bukkit, bungeecord)"
rm target/Observer/lib/bungeecord*
rm target/Observer/lib/bukkit*
rm target/Observer/lib/spigot*

# Extract every jar file into one folder.
echo "--- Unpacking jar's ..."
cd target/Observer
mkdir final

echo "  -> Observer.jar ..."
unzip -qq -o Observer.jar -d final/

echo ""
echo "  --- Dependencies ---"
cd lib/
for f in *.jar
do
    echo "  -> lib/$f ..."
    unzip -qq -o ${f} -d '../final'
done
cd ..

# Delete original Observer
echo ""
echo "--- Deleting original Observer.jar ..."
rm Observer.jar

# Copy plugin.yml and config.yml into the final folder because these files can be overwritten by other plugins.
echo "--- Do some other stuff ..."
rm final/plugin.yml
cp ../../src/main/resources/plugin.yml ./final/
cp ../../src/main/resources/config.json ./final/

# Compress 'final' directory to one jar.
echo "--- Build new Observer.jar file, with dependencies ..."
cd final
rm -rf '*.jar'
zip -qq -o ../Observer.jar -r *

# Cleaning up
cd ..
echo "--- Cleaning up ..."
rm -rf final/

# UNCOMMEND IF YOU WANT THAT OBSERVER WILL BE INSTALLED LOCALLY IN MAVEN.
#echo "--- Install to local repository ..."
#mvn install:install-file -q -Dfile=Observer.jar -DgroupId=net.mineup -DartifactId=Observer -Dversion=1.0-SNAPSHOT -Dpackaging=jar

# Copying
echo "--- Copy Observer.jar ..."

# ENTER HERE YOU'RE SPIGOT/BUNGEECORD PLUGIN FOLDER, SO THIS SCRIPT WILL COPY THE FINAL JAR INTO YOUR PLUGINS FOLDER.
cp Observer.jar "/home/mondei1/Dokumente/Minecraft/Spigot-1.12.2 Server/plugins/"
cp Observer.jar "/home/mondei1/Dokumente/Minecraft/Spigot-1.12.2 Server - 2/plugins/"
cp Observer.jar "/home/mondei1/Dokumente/Minecraft/BungeeCord-1.12.2/plugins/"

echo ""
echo "Done :D Now you're ready to use Observer."