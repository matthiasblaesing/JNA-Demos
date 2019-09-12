all: directories demolib.o
	gcc -shared ../../../target/native/demolib.o -o ../../../target/native/libdemolib.so

demolib.o: demolib.c
	gcc -c demolib.c -o ../../../target/native/demolib.o

directories:
	mkdir -p ../../../target/native

clean:
	rm -rf ../../../target/native