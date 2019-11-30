#include <string.h>
#include <stdio.h>

struct Point {
  int x, y;
};

const struct Point mypoint = {.x=10,.y=20};
struct Point * translate(struct Point const * pt, int dx, int dy) {
    printf("pt.x: %d\n\r", pt->x);
    printf("pt.y: %d\n\r", pt->y);
    printf("dx: %d\n\r", dx);
    printf("dy: %d\n\r", dy);

    return pt;
}