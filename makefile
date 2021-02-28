build: src/*.java
	javac -classpath . src/Leader.java 

run: build
	killall -q rmiregistry || true
	rmiregistry &
	java -classpath poi-3.7-20101029.jar: -Djava.rmi.server.hostname=127.0.0.1 comp34120.ex2.Main &
	java -Djava.rmi.server.hostname=127.0.0.1 Leader &

clean: 
	rm -rf src/*.class