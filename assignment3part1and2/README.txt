ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ                                                   
   README                     
ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ
ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ                                                   
   Computer Graphics - CS686 - Assignment3                   
ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ

Name: Shreya Sudhir Rajani

USFCA ID: ssrajani2

CWID: 20293808

Operating System: OS X

Language: C

URL: https://www.cs.usfca.edu/svn/ssrajani2/cs686/assignment1/

ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ

This README file is intended for describing the features of Assignment3

ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ


ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ
   Description
ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ
Part 1 - Loading and rendering mesh files (40 points) 

For this assignment, you will load a class of mesh files. You are free to pick a file format (OBJ, OFF, PLY, etc.) as long as your program can take filenames as input.

Create a program that reads in an input mesh file.
Store the vertices, faces, and any other information that may be available in your mesh file
If your mesh does NOT have face/vertex normals, pre-calculate and store the normals
In the draw/display function, go through the list of faces and display each triangle/polygon (10 points)
Make sure to specify the vertex normal for each vertex (10 points)
Precompute the bounding box for a mesh and display the bounding box when the user presses the 'b' key (10 points)
Note: Your program must be able to receive command line parameters (argc, argv if using C) or through a user interface (10 points)
If you are not starting with the starter code from Assignment 2, then make sure to enable mouse interaction, lighting, and shading in your program
Part 2 - Navigation through a user-created scene (60 points) 

For this part of the assignment, you will allow a user to walk through a scene using the keyboard/mouse functionality.
Create a scene with at least 3 meshes and at least 2 instances of each mesh. Use transforms for each mesh as necessary. (25 points)
Allow the user to navigate the scene using W (front), S (back), A (left), D (right) keys. (10 points)
Make sure that the user does NOT go through any mesh. Hint: Use the precomputed bounding box from Part 1 for this. (15 points)
Change from orthographic/perspective projection using the 'p' key. (5 points)
Creativity of the scene and program performance. (5 points)

ÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑ