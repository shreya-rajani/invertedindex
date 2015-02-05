#include "draw.h"
#include <GLUT/glut.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define AXES_LENGTH 100

static int smooth = 0;
int v=0, f0,f1, f2, f3;
float x=0,y=0,z=0;
int num_points, tot_faces, num_lines;
float minx, miny, minz, maxx, maxy, maxz;
int ctr1=0;
double point_x[100], point_y[100], point_z[100];
int ctr=0;
static int bbox = GL_FALSE;

struct face{
    int num_faces;
    int f[6];
    float nx, ny, nz;
};

struct vec{
    float v1x, v1y, v1z;
    float v2x, v2y, v2z;
};

struct vertices{
    float x, y, z;
    float fx, fy, fz;
    float vnx, vny, vnz;
    int eachctr;
};

struct face face_array[100000];
struct vec vec_array[100000];
struct vertices vertices_array[100000];

float v1x, v1y, v1z, v2x, v2y, v2z;

FILE *fr;
size_t len;

//Getting the file name from the command line
void fileread(char *argv[]){

    //char ch;
    //fscanf
    fr=fopen(argv[1],"r");
    //ch = getc(fr) ;

    if(!fr)
    {
        printf("Can't open file\n");
    }

    char firstline[20];

    fscanf(fr, "%s\n", firstline);
    fscanf(fr, "%d %d %d\n", &num_points, &tot_faces, &num_lines);

    //printf("firstline is %s\n", firstline);
    //printf("points %d\n face %d\n lines %d\n", num_points, num_faces, num_lines);


    //while((ch = fgetc(fr) ) != (num_points + 1)){ // EOF for the end of the file
    // printf("%c",ch); // prints the all content of the file


    for(int i=0; i<num_points; i++){

        fscanf(fr, "%f %f %f\n", &vertices_array[i].x, &vertices_array[i].y, &vertices_array[i].z);
        vertices_array[i].fx = 0;
        vertices_array[i].fy = 0;
        vertices_array[i].fz = 0;

        //printf("Points are : %f %f %f\n", vertices_array[i].x, vertices_array[i].y, vertices_array[i].z);
    }

    for(int i=0; i<tot_faces ; i++){

        fscanf(fr, "%d\n", &face_array[i].num_faces);

        if(face_array[i].num_faces==3){
            fscanf(fr, "%d %d %d\n", &face_array[i].f[0], &face_array[i].f[1], &face_array[i].f[2]);

            //printf("Total vertices are: %d\n", face_array[i].num_faces);
            //printf("Face are : %d %d %d\n", face_array[i].f[0], face_array[i].f[1], face_array[i].f[2]);
        }
        if(face_array[i].num_faces==4){
            fscanf(fr, "%d %d %d %d\n", &face_array[i].f[0], &face_array[i].f[1], &face_array[i].f[2], &face_array[i].f[3]);

            //printf("Total vertices are: %d\n", face_array[i].num_faces);

            //printf("i is: %d", i);
            //printf("Faces are : %d %d %d %d\n", face_array[i].f[0], face_array[i].f[1], face_array[i].f[2], face_array[i].f[3]);
        }
    }

    // printf("Faces are : %d %d %d %d\n", face_array[1].f[0], face_array[1].f[1], face_array[1].f[2], face_array[1].f[3]);
    fclose(fr);
}

void render_image(){
    //printf("%d\n", tot_faces);

    for (int i = 0; i<tot_faces; i++) {
        //printf("%d", face_array[i].num_faces);
        if(face_array[i].num_faces > 2){
            //printf("total faces are:%d", face_array[i].num_faces);

            f0= face_array[i].f[0];
            f1=face_array[i].f[1];
            f2=face_array[i].f[2];

            if(face_array[i].num_faces == 3) {

                glBegin(GL_TRIANGLES);
                glNormal3f(vertices_array[f0].vnx, vertices_array[f0].vny, vertices_array[f0].vnz);
                glNormal3f(face_array[i].nx, face_array[i].ny, face_array[i].nz);
                glVertex3f(vertices_array[f0].x, vertices_array[f0].y, vertices_array[f0].z);

                //glNormal3f(face_array[i].nx, face_array[i].ny, face_array[i].nz);
                //glNormal3f(vertices_array[f1].vnx, vertices_array[f1].vny, vertices_array[f1].vnz);
                glVertex3f(vertices_array[f1].x, vertices_array[f1].y, vertices_array[f1].z);

                //glNormal3f(face_array[i].nx, face_array[i].ny, face_array[i].nz);  // This is vertex normals.
                //glNormal3f(vertices_array[f2].vnx, vertices_array[f2].vny, vertices_array[f2].vnz);
                glVertex3f(vertices_array[f2].x, vertices_array[f2].y, vertices_array[f2].z);
                glEnd();


            }
            else {

                f3=face_array[i].f[3];

                glBegin(GL_QUADS);

                glNormal3f(face_array[i].nx, face_array[i].ny, face_array[i].nz);
                //glNormal3f(vertices_array[f0].vnx, vertices_array[f0].vny, vertices_array[f0].vnz);  // This is face normals.
                glVertex3f(vertices_array[f0].x, vertices_array[f0].y, vertices_array[f0].z);


                //glNormal3f(face_array[i].nx, face_array[i].ny, face_array[i].nz);  // This is vertex normals.
                //glNormal3f(vertices_array[f1].vnx, vertices_array[f1].vny, vertices_array[f1].vnz);
                glVertex3f(vertices_array[f1].x, vertices_array[f1].y, vertices_array[f1].z);

                // glNormal3f(face_array[i].nx, )  // This is vertex normals.

                //glNormal3f(vertices_array[f2].vnx, vertices_array[f2].vny, vertices_array[f2].vnz);
                glVertex3f(vertices_array[f2].x, vertices_array[f2].y, vertices_array[f2].z);


                //glNormal3f(vertices_array[f3].vnx, vertices_array[f3].vny, vertices_array[f3].vnz);
                glVertex3f(vertices_array[f3].x, vertices_array[f3].y, vertices_array[f3].z);


                glEnd();

            }
        }
    }
}

void computeFaceNormal(){

    for (int i = 0; i<tot_faces; i++) {

        v1x = vertices_array[f1].x - vertices_array[f0].x;
        v1y = vertices_array[f1].y - vertices_array[f0].y;
        v1z = vertices_array[f1].z - vertices_array[f0].z;

        v2x = vertices_array[f2].x - vertices_array[f1].x;
        v2y = vertices_array[f2].y - vertices_array[f1].y;
        v2z = vertices_array[f2].z - vertices_array[f1].z;

        //cross product
        face_array[i].nx = (vertices_array[f0].y * vertices_array[f1].z) + (vertices_array[f0].z * vertices_array[f1].y);

        face_array[i].ny = (vertices_array[f0].z * vertices_array[f1].x) + (vertices_array[f0].x * vertices_array[f1].z);

        face_array[i].nz = (vertices_array[f0].x * vertices_array[f1].y) + (vertices_array[f0].y * vertices_array[f1].x);

        vertices_array[f1].fx = vertices_array[f1].fx + face_array[i].nx;
        vertices_array[f1].fy = vertices_array[f1].fy + face_array[i].ny;
        vertices_array[f1].fz = vertices_array[f1].fz + face_array[i].nz;
        vertices_array[f1].eachctr++;

        vertices_array[f0].fx = vertices_array[f0].fx + face_array[i].nx;
        vertices_array[f0].fy = vertices_array[f0].fy + face_array[i].ny;
        vertices_array[f0].fz = vertices_array[f0].fz + face_array[i].nz;
        vertices_array[f0].eachctr++;

        vertices_array[f2].fx = vertices_array[f2].fx + face_array[i].nx;
        vertices_array[f2].fy = vertices_array[f2].fy + face_array[i].ny;
        vertices_array[f2].fz = vertices_array[f3].fz + face_array[i].nz;
        vertices_array[f2].eachctr++;

        if(face_array[i].num_faces == 4) {

            vertices_array[f3].fx = vertices_array[f3].fx + face_array[i].nx;
            vertices_array[f3].fy = vertices_array[f3].fy + face_array[i].ny;
            vertices_array[f3].fz = vertices_array[f3].fz + face_array[i].nz;
            vertices_array[f3].eachctr++;
        }
    }
}

void computeVertexNormal(){
    for(int i = 0; i< num_points; i++){
        vertices_array[i].vnx = vertices_array[i].fx/vertices_array[i].eachctr;
        vertices_array[i].vny = vertices_array[i].fy/vertices_array[i].eachctr;
        vertices_array[i].vnz = vertices_array[i].fz/vertices_array[i].eachctr;
    }
}

void boundingbox(){

    minx = vertices_array[0].x;
    miny = vertices_array[0].y;
    minz = vertices_array[0].z;

    maxx = vertices_array[0].x;
    maxy = vertices_array[0].y;
    maxz = vertices_array[0].z;

    for(int i=1; i< num_points; i++){
        if(minx > vertices_array[i].x){
            minx = vertices_array[i].x;
        }

        if(miny > vertices_array[i].y){
            miny = vertices_array[i].y;
        }

        if(minz > vertices_array[i].z){
            minz = vertices_array[i].z;
        }

        if(maxx < vertices_array[i].x){
            maxx = vertices_array[i].x;
        }

        if(maxy < vertices_array[i].y){
            maxy = vertices_array[i].y;
        }

        if(maxz < vertices_array[i].z){
            maxz = vertices_array[i].z;
        }
    }

    glBegin(GL_LINE_LOOP);
    glVertex3f(minx, miny, maxz);
    glVertex3f(maxx, miny, maxz);
    glVertex3f(maxx, maxy, maxz);
    glVertex3f(minx, maxy, maxz);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex3f(maxx, miny, maxz);
    glVertex3f(maxx, miny, minz);
    glVertex3f(maxx, maxy, minz);
    glVertex3f(maxx, maxy, maxz);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex3f(maxx, miny, minz);
    glVertex3f(minx, miny, minz);
    glVertex3f(minx, maxy, minz);
    glVertex3f(maxx, maxy, minz);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex3f(minx, miny, minz);
    glVertex3f(minx, miny, maxz);
    glVertex3f(minx, maxy, maxz);
    glVertex3f(minx, maxy, minz);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex3f(minx, maxy, maxz);
    glVertex3f(maxx, maxy, maxz);
    glVertex3f(maxx, maxy, minz);
    glVertex3f(minx, maxy, minz);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex3f(minx, miny, maxz);
    glVertex3f(minx, miny, minz);
    glVertex3f(maxx, miny, minz);
    glVertex3f(maxx, miny, maxz);
    glEnd();
}

void draw_axes() {

    glLineWidth(3.0);
    glBegin(GL_LINES);

    glColor3f(1, 0, 0); // x-axis
    glVertex3f(0, 0, 0);
    glVertex3f(AXES_LENGTH, 0, 0);

    glColor3f(0, 1, 0); // y-axis
    glVertex3f(0, 0, 0);
    glVertex3f(0, AXES_LENGTH, 0);

    glColor3f(0, 0, 1); // z-axis
    glVertex3f(0, 0, 0);
    glVertex3f(0, 0, AXES_LENGTH);

    glEnd();
}

void draw(void)
{
    /* clear old screen contents */
    glClearColor(0, 0, 0, 0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    //fileread()
    //draw_axes();
    computeFaceNormal();
    computeVertexNormal();
    render_image();

    if (bbox) {
        boundingbox();
    }

    /* tell OpenGL to finish drawing and switch the double buffer */
    glutSwapBuffers();
}

/* called on any keypress
 *
 * We don't use x and y, but they're the mouse location when the key
 * was pressed.
 */
void key(unsigned char k, int x, int y)
{
    switch (k) {
        case 27:			/* Escape: exit */
            exit(0);
        case 's': case 'S':	     /* 's' or 'S': toggle smooth & faceted */
            smooth = !smooth;
            glutPostRedisplay();	/* tell GLUT to redraw after this change */
            break;
            
        case 'b':
        case 'B':
            bbox = GL_TRUE;
            break;
            
    }
}



