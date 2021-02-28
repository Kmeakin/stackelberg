build: src/*.java
	javac -classpath . -source 1.6 -target 1.6 src/Leader.java 

kill:
	killall -q rmiregistry || true
	pkill -f 'java Leader' || true

run: build
	killall -q rmiregistry || true
	rmiregistry &
	java -classpath poi-3.7-20101029.jar: -Djava.rmi.server.hostname=127.0.0.1 comp34120.ex2.Main &
	java -classpath .:src -Djava.rmi.server.hostname=127.0.0.1 Leader

clean: 
	rm -rf *.class src/*.class