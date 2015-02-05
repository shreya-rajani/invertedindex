/* your drawing code goes in here */

/* by including draw.h, we ensure that the exported prototypes
 match the function definitions */
#include "draw.h"
#include <GLUT/glut.h>
#include <stdlib.h>
#include <stdio.h>

// true: should use per-vertex color & normal
// false: should use per-face color and normal
static int smooth = 0;
GLdouble wx, wy, wz;
#define MAX 10000
#define CTRL_MAX 40
int ctr = 0;
float xp[CTRL_MAX];
float yp[CTRL_MAX];
float xp1[MAX];
float yp1[MAX];
int counterx = 0;
int counterz = 0, countery=0;
double intermediate[4][4];
double lx;
double lz, ly;

struct point {
    double x, y, z;
};

struct point pointarray[MAX];
int ptr_ctr = 0;

double xmatrix[4][4] = {{0,0.3,0.6,0.9},{0,0.3,0.6,0.9},{0,0.3,0.6,0.9},{0,0.3,0.6,0.9}};
double zmatrix[4][4] = {{0,0,0,0},{0.3,0.3,0.3,0.3},{0.6,0.6,0.6,0.6},{0.9,0.9,0.9,0.9}};
double ymatrix[4][4] = {{0,0,0,0},{0,3.0,3.0,0.0},{0,2.0,3.5,0.0},{0.0,0.0,0.0,0.0}};

//double xmatrix[4][4] = {{0,3,6,9},{0,3,6,9},{0,3,6,9},{0,3,6,9}};
//double zmatrix[4][4] = {{0,0,0,0},{3,3,3,3},{6,6,6,6},{9,9,9,9}};
//double ymatrix[4][4] = {{0,0,0,0},{0,3,3,0},{0,2.0,3.5,0.0},{0.0,0.0,0.0,0.0}};
//double xmatrix[4][4] = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0.1,0,0.9,0}};
//double zmatrix[4][4] = {{0,0,0,0.1},{0,0,0,0},{0,0,0,0.9},{0,0,0,0}};
double xi1[1][4];
double zi1[1][4];
double xi[MAX];
double yi1[1][4];
double yi[MAX];
double zi[MAX];
FILE *fr;

void fileread(){

    char ch = ' ';
    fr=fopen("box.off","r");
    if(!fr)
    {
        printf("Can't open file\n");

    }
    while( ( ch = fgetc(fr) ) != EOF )
        printf("%c",ch);
    fclose(fr);
}

void bez(){

    //glOrtho(-1, 10, -1, 10, -1, 1);
    double offset = 0.1f;

    for(double s = 0.0; s <= 1.0; s += offset){

        double s2 = s*s;

        double s3 = s*s*s;

        lx=0;
        lz = 0;
        ly=0;
        double sm[1][4] = {{s3, s2, s, 1.0}};
        //multiplication of s with intermediate value

        for(int i=0;i<1;i++){ //row 1 = 1
            for(int j=0;j<4;j++){ // column 2 =4
                for(int k=0;k<4;k++){ // column 1 =4

                    lx=lx+sm[i][k]*xmatrix[k][j];
                    ly=ly+sm[i][k]*ymatrix[k][j];
                    lz=lz+sm[i][k]*zmatrix[k][j];
                }

                //printf("l= %d\n",l);
                xi1[i][j] = lx; //1x4
                yi1[i][j] = ly; //1x4
                zi1[i][j] = lz; //1x4

                lx = 0.0;
                ly = 0.0;
                lz = 0.0;
            }
        }
        //        for(int i=0;i<1;i++){ //row 1 = 1
        //
        //            for(int j=0;j<4;j++){ // column 2 =4
        //                for(int k=0;k<4;k++){ // column 1 =4
        //                    ly=ly+sm[i][k]*ymatrix[k][j];
        //                }
        //
        //                //printf("l= %d\n",l);
        //                yi1[i][j] = ly; //1x4
        //                ly = 0.0;
        //
        //
        //            }
        //        }
        //
        //        for(int i=0;i<1;i++){ //row 1 = 1
        //            for(int j=0;j<4;j++){ // column 2 =4
        //                for(int k=0;k<4;k++){ // column 1 =4
        //                    lz=lz+sm[i][k]*zmatrix[k][j];
        //                }
        //
        //                //printf("l= %d\n",l);
        //                zi1[i][j] = lz; //1x4
        //                lz=0;
        //            }
        //        }

        counterx = 0;

        counterz = 0;

        for (double t = 0.0; t <= 1.0; t += offset){

            double t2 = t*t;
            double t3 = t*t*t;
            double tm[4][1] = {{t3},{t2},{t},{1}};

            lx = 0;
            lz = 0;
            ly = 0;


            //final value of x

            for(int i=0;i<1;i++){ //row 1 =
                for(int j=0;j<1;j++){ //column 2 =
                    for(int k=0;k<4;k++){ // column 1 =
                        lx=lx+xi1[i][k]*tm[k][j];
                        ly=ly+yi1[i][k]*tm[k][j];
                        lz=lz+zi1[i][k]*tm[k][j];
                        //printf("l= %d\n",l);
                    }
                    xi[counterx] = lx;
                    lx=0;

                    pointarray[ptr_ctr].x = lx;
                    pointarray[ptr_ctr].y = ly;
                    pointarray[ptr_ctr].z = lz;
                    ptr_ctr++;

                    counterx++;
                    //printf("%f %d\n", l, counter);


                }

            }

            //            for(int i=0;i<1;i++){ //row 1 =
            //                for(int j=0;j<1;j++){ //column 2 =
            //                    for(int k=0;k<4;k++){ // column 1 =
            //                        ly=ly+yi1[i][k]*tm[k][j];
            //                        //printf("l= %d\n",l);
            //                    }
            //
            //                    yi[countery] = ly;
            //                    ly=0;
            //
            //                    countery++;
            //                    //printf("%f %d\n", l, counter);
            //                }
            //            }
            //
            //            for(int i=0;i<1;i++){ //row 1 =
            //                for(int j=0;j<1;j++){ //column 2 =
            //                    for(int k=0;k<4;k++){ // column 1 =
            //                        lz=lz+zi1[i][k]*tm[k][j];
            //                        //printf("l= %d\n",l);
            //                    }
            //                    zi[counterz] = lz;
            //                    lz=0;
            //
            //                    counterz++;
            //
            //                    //printf("%f %d\n", l, counter);
            //                }
            //            }


        }
    }
}


/*
 * this is called every time the screen needs to be redrawn
 */
void
draw(void)
{
    /* clear old screen contents */
    glClearColor(0.5,0.7,0.9,1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    //fileread();

    /* draw something */


    glPointSize(5);

    glColor3f(0, 1.0, 1.0);
    //
    glBegin(GL_POINTS);
    for(int i=0;i<4;i++) {
        for(int j=0;j<4;j++){
            glVertex2f(xmatrix[i][j], zmatrix[i][j]);
        }
    }
    glEnd();

    glColor3f(1.0, 0.0, 0.0);


    glBegin(GL_POINTS);

    //printf("cx: %d cz: %d\n", counterx, counterz);

    for(int i= 0; i<ptr_ctr; i++) {
        //glVertex3f(0,0, 0);

        //glVertex3f(xi[i], yi[i], zi[i]);
        glVertex3f(pointarray[i].x, pointarray[i].y, pointarray[i].z);

        //printf("%d %d in for: x: %.2f y:%.2f \n", i, j, xi[i], zi[j]);

    }
    glEnd();

    //    glBegin(GL_LINES);
    //    for (int i = 0; i<4; i++) {
    //        for (int j = 0; j<4; j++){
    //            glVertex2f(xmatrix[i][j], zmatrix[i][j]);
    //            glVertex2f(xmatrix[i+1][j], zmatrix[i+1][j]);
    //        }}
    //    glEnd();
    //
    //    glBegin(GL_LINES);
    //    for (int i = 0; i<4; i++) {
    //        for (int j = 0; j<4; j++){
    //            glVertex2f(xi[i][j], zi[i][j]);
    //            glVertex2f(xi[i][j+1], zi[i][j+1]);
    //        }}
    //    glEnd();
    glFlush();
    
    //    // or, for fun, uncomment this
    //       glPushMatrix();
    //       glTranslatef(-10,0,0);
    //       glutSolidTeapot(90);
    //       glPopMatrix();
    
    
    /* tell OpenGL to finish drawing and switch the double buffer */
    glutSwapBuffers();
}

/* called on any keypress
 *
 * We don't use x and y, but they're the mouse location when the key
 * was pressed.
 */
void
key(unsigned char k, int x, int y)
{
    switch (k) {
        case 27:			/* Escape: exit */
            exit(0);
        case 's': case 'S':	     /* 's' or 'S': toggle smooth & faceted */
            smooth = !smooth;
            glutPostRedisplay();	/* tell GLUT to redraw after this change */
            break;
            
            
        case 'b':
            bez();
            break;
    }
}
