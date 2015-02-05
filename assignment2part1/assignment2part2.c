#include <stdio.h>
#include <stdlib.h>
#include <GLUT/glut.h>
#include <math.h>

//Global variables declaration
GLdouble wx, wy, wz;
#define MAX 10000
#define CTRL_MAX 40
int ctr = 0;
float xp[CTRL_MAX];
float yp[CTRL_MAX];
float xp1[MAX];
float yp1[MAX];
int counterx = 0;
int counterz = 0;
double intermediate[4][4];
FILE *fr;
double lx;
double lz;
double xmatrix[4][4] = {{0,0.3,0.6,0.9},{0,0.3,0.6,0.9},{0,0.3,0.6,0.9},{0,0.3,0.6,0.9}};
double zmatrix[4][4] = {{0,0,0,0},{0.3,0.3,0.3,0.3},{0.6,0.6,0.6,0.6},{0.9,0.9,0.9,0.9}};
//double xmatrix[4][4] = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0.1,0,0.9,0}};
//double zmatrix[4][4] = {{0,0,0,0.1},{0,0,0,0},{0,0,0,0.9},{0,0,0,0}};
double xi1[1][4];
double zi1[1][4];
double xi[MAX];
double zi[MAX];
//reading of the file
void fileread(){

    char ch;

    fr=fopen("/Users/shreyarajani/Desktop/a.txt","r");



    if(!fr)

    {

        printf("Can't open file\n");

    }



    while( ( ch = fgetc(fr) ) != EOF )

        printf("%c",ch);



    fclose(fr);

}


//Initial method
static void init(void)
{
    glEnable( GL_POINT_SMOOTH );
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glShadeModel(GL_FLAT);

    glClearColor(0.0, 0.0, 0.0, 0.0);

}

//Method to compute the matrices
void bez(){

    double offset = 0.1f;

    for(double s = 0.0; s <= 1.0; s += offset){

        double s2 = s*s;

        double s3 = s*s*s;

        double sm[1][4] = {{s3, s2, s, 1.0}};

        //multiplication of s with intermediate value

        for(int i=0;i<1;i++){ //row 1 = 1

            for(int j=0;j<4;j++){ // column 2 =4

                for(int k=0;k<4;k++){ // column 1 =4

                    lx=lx+sm[i][k]*xmatrix[k][j];

                }

                //printf("l= %d\n",l);
                xi1[i][j] = lx; //1x4

                lx=0;

            }
        }

        for(int i=0;i<1;i++){ //row 1 = 1

            for(int j=0;j<4;j++){ // column 2 =4

                for(int k=0;k<4;k++){ // column 1 =4

                    lz=lz+sm[i][k]*zmatrix[k][j];

                }

                //printf("l= %d\n",l);
                zi1[i][j] = lz; //1x4
                lz=0;

            }

        }

        counterx = 0;

        counterz = 0;



        for (double t = 0.0; t <= 1.0; t += offset){



            double t2 = t*t;

            double t3 = t*t*t;

            double tm[4][1] = {{t3},{t2},{t},{1}};



            //final value of x

            for(int i=0;i<1;i++){ //row 1 =

                for(int j=0;j<1;j++){ //column 2 =

                    for(int k=0;k<4;k++){ // column 1 =

                        lx=lx+xi1[i][k]*tm[k][j];

                        //printf("l= %d\n",l);

                    }

                    xi[counterx] = lx;



                    counterx++;

                    //printf("%f %d\n", l, counter);

                    lx=0;

                }

            }



            for(int i=0;i<1;i++){ //row 1 =

                for(int j=0;j<1;j++){ //column 2 =

                    for(int k=0;k<4;k++){ // column 1 =

                        lz=lz+zi1[i][k]*tm[k][j];

                        //printf("l= %d\n",l);

                    }

                    zi[counterz] = lz;



                    counterz++;

                    //printf("%f %d\n", l, counter);

                    lz=0;
                }
            }
        }
    }
}


//display method
void display(void)

{

    glClear(GL_COLOR_BUFFER_BIT);

    fileread();

    bez();

    glPointSize(6);



    glColor3f(0, 1.0, 1.0);

    glBegin(GL_POINTS);

    for (int i = 0; i < counterx; i++) {

        for(int j = 0; j< counterz; j++){

            //glVertex3f(0,0, 0);

            glVertex2f(xmatrix[i][j], zmatrix[i][j]);

            //printf("in for: x: %.2f y:%.2f \n", xi[i][j], zi[i][j]);
        }
    }
    glEnd();

    glBegin(GL_LINES);
    for (int i = 0; i<4; i++) {
        for (int j = 0; j<4; j++){
            glVertex2f(xmatrix[i][j], zmatrix[i][j]);
            glVertex2f(xmatrix[i+1][j], zmatrix[i+1][j]);
        }}
    glEnd();

    glBegin(GL_LINES);
    for (int i = 0; i<4; i++) {
        for (int j = 0; j<4; j++){
            glVertex2f(xmatrix[i][j], zmatrix[i][j]);
            glVertex2f(xmatrix[i][j+1], zmatrix[i][j+1]);
        }}
    glEnd();
    glFlush();
}

//Main method
int main(int argc, char** argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(1000, 1000);
    glutCreateWindow(argv[0]);
    init();
    glutDisplayFunc(display);
    glutMainLoop();
    return 0;
}