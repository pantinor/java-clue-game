Instructions:

Single Player Mode:  

Not much to say here.  Just pick the number of computer controlled players and yourself and try it out.

Multi Player Mode:

You have to get the smart fox server installed and running.
Here are some details to run it.

Drop the zone xml file into the zones dir of 
Add the "clue" extension in SFS2X server like this :

C:\Program Files\SFS2X-RC1a\SFS2X\extensions\clue

Add the clue jar to this directory.

For example:
C:\Program Files\SFS2X-RC1a\SFS2X\extensions\clue\clue-game-1.0.1.jar

Start up the server and the Clue game zone should become active.  There is an SFS admin GUI to login and see some details of the zone.

Maven Project Compile Dependencies:

Unfortunately, Smart Fox Server dependencies are not available in any public maven repository.  It is not an open source project.
You have to download the jars from the smartfox website and manually set up your local m2 repository.

I added them in this projects maven resources - third party directory, but latest jars are available at http://www.smartfoxserver.com

<dependency>
	<groupId>org.gotoandplay.smartfoxclient.sfs2x</groupId>
	<artifactId>sfs2x-client-core</artifactId>
	<version>1.0.1</version>
</dependency>

<dependency>
	<groupId>org.gotoandplay.smartfoxclient.sfs2x</groupId>
	<artifactId>sfs2x-api</artifactId>
	<version>1.0.1</version>
</dependency>

<dependency>
	<groupId>org.gotoandplay.smartfoxclient.sfs2x</groupId>
	<artifactId>sfs2x-server</artifactId>
	<version>1.0.1</version>
</dependency>

<dependency>
	<groupId>org.gotoandplay.smartfoxclient</groupId>
	<artifactId>xmlsocket</artifactId>
	<version>1.0.1</version>
</dependency>


Author: Paul Antinori (pantinor@gmail.com)
