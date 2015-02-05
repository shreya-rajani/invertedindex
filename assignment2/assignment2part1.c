#include <stdio.h>
#include <stdlib.h>
#include <GLUT/glut.h>
#include <math.h>

GLdouble wx, wy, wz;
double wx1, wy1, wz1;
#define MAX 10000
#define CTRL_MAX 40
int ctr = 0, c=0;
float xp[CTRL_MAX];
float yp[CTRL_MAX];
float xp1[MAX];
float yp1[MAX];
double ye[MAX], xe[MAX];
int counter = 0;
int edit = GL_FALSE;
int add = GL_FALSE;

/**
 Function to associate each key with function
 */
void keyboard(unsigned char key, int x, int y)
{
    switch (key) {
            //Draw a Point. Experiment with glPointSize() to vary the size of the displayed point.
        case 'a':
        case 'A':
            add = GL_TRUE;
            edit = GL_FALSE;
            glutPostRedisplay();
            break;

        case 'e':
        case 'E':
            add = GL_FALSE;
            edit = GL_TRUE;
            glutPostRedisplay();
            break;

        case 27:
            exit(0);
            break;
        default:
            break;

    }
}
void addFunc(){
    glClear(GL_COLOR_BUFFER_BIT);
    glPointSize(6);

    glColor3f(0.0, 1.0, 0.0);

    glBegin(GL_POINTS);
    for (int i = 0; i < ctr; i++) {
        glVertex2f(xp[i], yp[i]);
        //        printf("in display: x: %.2f y:%.2f \n", xp[i], yp[i]);

    }
    glEnd();

    glLineWidth(3.0);
    glColor3f(1.0, 1.0, 1.0);

    glBegin(GL_LINE_STRIP);
    for (int i = 0; i < counter; i++) {
        glVertex2f(xp1[i], yp1[i]);
    }
    glEnd();
    glFlush();
}
void editFunc(){
    //printf("xe[c]: %f\n", xe[c]);
    for(int i = 0; i<ctr; i++ ){

        for(int j = 0; j<c; j++){

            double dx = xe[0]-xp[i];
            //printf("xe[c]: %f\n", xe[j]);
            double dy = ye[0]-yp[i];
            double constant = dx*dx - dy*dy;
            double dist = sqrt(constant);

            //printf("dist : %f", dist);

            if(dist <= 4){
                printf("xe[j]:%f\n",xe[j]);
                xp[i] = xe[j];
                yp[i] = ye[0];
                printf("replaced: %f\n", xp[i]);
                addFunc();

            }
        }
    }

}

static void init(void)
{
    glEnable( GL_POINT_SMOOTH );
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	glShadeModel(GL_FLAT);
	glClearColor(0.0, 0.0, 0.0, 0.0);

    for(int i=0;i<CTRL_MAX;i++) {
        xp[i] = yp[i] = 0.0;
    }
}

int fact(int n){
	int fact = 1;
	for (int c = 1; c <= n; c++){
		fact = fact * c;
	}
	return fact;
}

void bez(int low){
	float offset = 0.01f;

	for (float t = 0.0; t <= 1.; t += offset){
		float x = 0;
		float y = 0;

        x = pow(1-t, 3.0) * xp[low] +
        3.0 * pow(1-t, 2.0) * t * xp[low+1] +
        3.0 * (1.0-t) * pow(t,2.0) * xp[low+2] +
        pow(t, 3.0) * xp[low+3];

        y = pow(1-t, 3.0) * yp[low] +
        3.0 * pow(1-t, 2.0) * t * yp[low+1] +
        3.0 * (1.0-t) * pow(t,2.0) * yp[low+2] +
        pow(t, 3.0) * yp[low+3];


		xp1[counter] = x;
		yp1[counter] = y;

		counter++;
	}

}



void display(void)
{
    if(add)
    {
        addFunc();

    }
    if(edit){
        //printf("hi");
        editFunc();

    }
}

void mouse(int button, int state, int x, int y)

{
	GLint viewport[4];
	GLdouble mvmatrix[16], projmatrix[16];
	GLint realy;  /*  OpenGL y coordinate position  */
	switch (button) {
        case GLUT_LEFT_BUTTON:
            if (state == GLUT_DOWN) {
                glGetIntegerv(GL_VIEWPORT, viewport);
                glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix);
                glGetDoublev(GL_PROJECTION_MATRIX, projmatrix);
                /*  note viewport[3] is height of window in pixels  */
                realy = viewport[3] - (GLint)y - 1;
                gluUnProject((GLdouble)x, (GLdouble)realy, 0.0,
                             mvmatrix, projmatrix, viewport, &wx, &wy, &wz);


                xp[ctr] = wx;
                yp[ctr] = wy;
                ctr++;

                //printf("x: %.2f y:%.2f : %d\n", xp[ctr-1], yp[ctr-1]);

                if (ctr == 4) {
                    float newx = xp[ctr-1] + xp[ctr-1] - xp[ctr-2];
                    float newy = yp[ctr-1] + yp[ctr-1] - yp[ctr-2];

                    xp[ctr] = newx;
                    yp[ctr] = newy;
                    //printf("NEW x: %.2f y:%.2f \n", xp[ctr-1], yp[ctr-1]);

                    ctr++;

                    bez(0);
                }
                glutPostRedisplay();

                //printf("CTR is %d\n", ctr);
                if((ctr-7)%3 == 0 && ctr > 6)
                {
                    //printf("ctr-5 is %d\n", ctr-5);
                    bez(ctr-4);

                    float newx = xp[ctr-1] + xp[ctr-1] - xp[ctr-2];
                    float newy = yp[ctr-1] + yp[ctr-1] - yp[ctr-2];

                    xp[ctr] = newx;
                    yp[ctr] = newy;
                    //printf("NEW x: %.2f y:%.2f \n", xp[ctr-1], yp[ctr-1]);

                    ctr++;

                    glutPostRedisplay();
                }
            }
            break;

        default:
            break;
	}
}

void mousemotion(int x1, int y1 ){
    //printf("ggg: %d\n", x1);
    GLint realy;
    GLint viewport[4];
	GLdouble mvmatrix[16], projmatrix[16];
    glGetIntegerv(GL_VIEWPORT, viewport);
    glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix);
    glGetDoublev(GL_PROJECTION_MATRIX, projmatrix);
    /*  note viewport[3] is height of window in pixels  */
    realy = viewport[3] - (GLint)y1 - 1;
    gluUnProject((GLdouble)x1, (GLdouble)realy, 0.0,
                 mvmatrix, projmatrix, viewport, &wx1, &wy1, &wz1);


    xe[c] = wx1;

    ye[c] = wy1;
    c++;
    //glutPostRedisplay();

}

int main(int argc, char** argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(500, 500);
    glutCreateWindow(argv[0]);
    init();
    //glutReshapeFunc (reshape);
    glutKeyboardFunc (keyboard);
    glutMouseFunc(mouse);
    glutMotionFunc(mousemotion);
    glutDisplayFunc(display);
    glutMainLoop();
    return 0;
}