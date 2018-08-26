echo "--- Create 'spigot' folder ..."
mkdir spigot

echo "--- Change working directory to spigot/"
cd spigot

echo "--- Download latest BuildTools ..."
wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar

echo "--- Run BuildTools (this process can take a while) ..."
sleep 2
java -jar BuildTools.jar

cd ..
echo ""
echo ""
echo "--- Build done. Now you can execute package.sh."