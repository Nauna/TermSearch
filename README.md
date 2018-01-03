#Google search terms

The motivation for this was HST540 History of Espionage. In this course we were expected to know a list of general terms that related to the class. So as a reference I wrote this code. 
	This code will look into a predefined directory for all *.txt files, read the .txt files line by line (such that each line will be a new term), do a google search of each term, then writing the description from the top 10 google search result into a file separating each term.
	Currently program has a unique output file for each .txt term file found in given directory. 
	
IN this folder/Directory

/outputs = folder containing output files of program execution

terms.txt = sample terms for execution of program

/TermSearch = files and folders associated with Java Eclipse project

/TermSearch/src/Tester.java = main java program. More details of program at the top of this file