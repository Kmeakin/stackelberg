build: src/*.java
	javac -classpath src src/Leader.java

clean: 
	rm -rf src/*.class