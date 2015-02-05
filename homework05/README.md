Homework 05: Multithreaded Directory Traverser
Due Oct 25, 2013 by 11:59pm  Points 10
Create a class Multithreaded Directory Traverser that traverses one or more directories and their subdirectories to locate all files with a particular extension. For example, calling traverseDirectory(dir, ".txt") should find and add all text files in the dir directory and its subdirectories.

Your code must use a WorkQueue and MultiReaderLock for multithreading and synchronization to receive full credit. Each worker thread should parse a single directory. If any subdirectories are found, a new worker thread should be created to parse that subdirectory.

You class must have at a minimum the following private members:

private final WorkQueue workers;
private final TreeSet<Path> paths;
private final MultiReaderLock lock;
Your class must have at a minimum the following public methods:

MultithreadedDirectoryTraverser()
void traverseDirectory(Path dir, String ext)
Set<Path> getPaths()
void finish()
void reset()
void shutdown()
You must decide the appropriate keywords to use for the above methods (e.g. when to use final, static, synchronized). For getPaths(), use the Collections.unmodifiableSet() (Links to an external site.) method.

You may have additional members and methods beyond what is listed above. You may want to look at the following examples from class:

WorkQueue.java (Links to an external site.)
MultithreadedDirectorySizeCalculator.java (Links to an external site.)
MultiReaderLock.java (Links to an external site.)
You should perform your own testing. We will not release test code for this homework.
