#include <string.h>

/**
 * demoString can be called with a NULL output buffer. In anycase the function
 * will return the minimum size of the supplied buffer in bytes.
 */
long demoString(char * output) {
    char* demo = "Hallo Welt";

    if(output != NULL) {
        memcpy(output, demo, strlen(demo) + 1);
    }

    return strlen(demo) + 1;
}