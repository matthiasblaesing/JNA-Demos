#include <stddef.h>
#include <stdio.h>
#include <string.h>

#include "demolib.h"

void print_pose(gnc_pose_t pose1) {
    // Print pose1
    printf("Native: Pos 1      = %1.3f %1.3f %1.3f\n",pose1.state.pos[0],pose1.state.pos[1],pose1.state.pos[2]);
    printf("Native: Vel 1      = %1.3f %1.3f %1.3f\n",pose1.state.vel[0],pose1.state.vel[1],pose1.state.vel[2]);
    printf("Native: Accel 1    = %1.3f %1.3f %1.3f\n",pose1.state.accel[0],pose1.state.accel[1],pose1.state.accel[2]);
    printf("\n");
    printf("Native: Quat 1     = %1.3f %1.3f %1.3f %1.3f\n",pose1.attitude.quat[0],pose1.attitude.quat[1],pose1.attitude.quat[2],pose1.attitude.quat[3]);
    printf("Native: Rate 1     = %1.3f %1.3f %1.3f\n",pose1.attitude.rate[0],pose1.attitude.rate[1],pose1.attitude.rate[2]);
    printf("Native: AttAccel 1 = %1.3f %1.3f %1.3f\n",pose1.attitude.accel[0],pose1.attitude.accel[1],pose1.attitude.accel[2]);
    printf("\n");
    printf("Native: Time:  %1.3f\n", pose1.time);
    printf("Native: Time2: %1.3f\n", pose1.time2);
    printf("Native: Time3: %1.3f\n", pose1.time3);
    printf("\n");
    printf("sizeof(gnc_state_t):    %ld\n", sizeof(gnc_state_t));
    printf("sizeof(gnc_attitude_t): %ld\n", sizeof(gnc_attitude_t));
    printf("sizeof(gnc_pose_t):     %ld\n", sizeof(gnc_pose_t));
    printf("offsetof(gnc_pose_t, state):    %1$ld | 0x%1$lx\n", offsetof(gnc_pose_t, state));
    printf("offsetof(gnc_pose_t, attitude): %1$ld | 0x%1$lx\n", offsetof(gnc_pose_t, attitude));
    printf("offsetof(gnc_pose_t, time):     %1$ld | 0x%1$lx\n", offsetof(gnc_pose_t, time));
}

gnc_pose_t return_pose() {
    gnc_pose_t res;

    res.state.pos[0] = 1;
    res.state.pos[1] = 2;
    res.state.pos[2] = 3;

    res.state.vel[0] = 4;
    res.state.vel[1] = 5;
    res.state.vel[2] = 6;

    res.state.accel[0] = 7;
    res.state.accel[1] = 8;
    res.state.accel[2] = 9;

    res.attitude.quat[0] = 10;
    res.attitude.quat[1] = 11;
    res.attitude.quat[2] = 12;
    res.attitude.quat[3] = 13;

    res.attitude.rate[0] = 14;
    res.attitude.rate[1] = 15;
    res.attitude.rate[2] = 16;

    res.attitude.accel[0] = 17;
    res.attitude.accel[1] = 18;
    res.attitude.accel[2] = 19;

    res.time = 20;

    return res;
}