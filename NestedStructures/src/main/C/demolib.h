
typedef double F64;

typedef struct{
    F64 pos[3];
    F64 vel[3];
    F64 accel[3];
} gnc_state_t;

typedef struct{
    F64 quat[4];
    F64 rate[3];
    F64 accel[3];
} gnc_attitude_t;

typedef struct{
    gnc_state_t state;
    gnc_attitude_t attitude;
    F64 time;
    F64 time2;
    F64 time3;
} gnc_pose_t;

void print_pose(gnc_pose_t pose1);
gnc_pose_t return_pose();