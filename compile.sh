#!/usr/bin/env bash
clear
echo "--- Starting Maven, to package Observer ..."
mvn -q package

# Remove unnecessary packages.
echo "--- Removing unnecessary dependencies ..."
rm target/Observer/lib/bungeecord*
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

# Compress 'final' directory to one jar.
echo "--- Build new Observer.jar file, with dependencies ..."
cd final
rm -rf '*.jar'
zip -qq -o ../Observer.jar -r *

# Cleaning up
cd ..
echo "--- Cleaning up ..."
rm -rf final/

# Copying
echo "--- Copy Observer.jar ..."

cp Observer.jar "/home/mondei1/Dokumente/Minecraft/Spigot-1.12.2 Server/plugins/"
cp Observer.jar "/home/mondei1/Dokumente/Minecraft/BungeeCord-1.12.2/plugins/"

echo ""
echo "Done :D Now you're ready to use Observer."