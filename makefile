build: src/*.java
	javac -classpath . -source 1.6 -target 1.6 src/Leader.java 

kill:
	killall -q -9 rmiregistry || true
	killall -q -9 java 		  || true

run: kill build
	rmiregistry &
	sleep 1
	java -classpath poi-3.7-20101029.jar: -Djava.rmi.server.hostname=127.0.0.1 comp34120.ex2.Main &
	sleep 2
	java -classpath .:src -Djava.rmi.server.hostname=127.0.0.1 Leader

clean: 
	rm -rf *.class src/*.class