/*
 * ex1.c
 *
 *  Created on: Mar 29, 2021
 *      Author: braude
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>//linux lib
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>


#define SUPERSHELL_TOK_DELIM "\t\r\n\a "
#define BUFFLINESIZE 512
#define BUFF_TOKEN_ALLOCATED_STRING 4
#define BUFF_CPY_FOR_REDIRECTION 128
#define PATH_LEN 100
#define MODULU_ENCRYPTION 256

void superShellLoop();
char* superShellReadLine();
char** superShellSplitLine(char* line);
int superShellExec(char** args);
void errorAllocationFailed();
int superShellLaunch(char** args);
int myCd(char** args);
int encryptFile(char* srcFile, int num, char* destFile);


int main(int argc, char** argv)
{
	superShellLoop();
	return EXIT_SUCCESS;
}


void superShellLoop()
{
	char* lineFromSuperShell;
	char** listOfArgs;
	int status;
	//int i;
	do {
		printf("SuperShell> ");//prints the prompt to the screen
		lineFromSuperShell = superShellReadLine();//get line from user
		listOfArgs = superShellSplitLine(lineFromSuperShell);/*split the lines*/
//	    for(i=0;i<BUFF_TOKEN_ALLOCATED_STRING;i++)//check out if ok not necessary
//	    {
//	    	printf("args[%d]= %s\n",i,listOfArgs[i]);
//	    }
		status = superShellExec(listOfArgs);
		free(lineFromSuperShell);
		free(listOfArgs);
	} while (status);
}

char* superShellReadLine()
{
	int buffSize = BUFFLINESIZE;
	int position = 0;
	char* buffer = malloc(sizeof(char) * buffSize);
	int letter;
	if (buffer == NULL)//check if allocation failed
	{
		errorAllocationFailed();//send message to screen and abort
	}

	while (1)
	{
		letter = getchar();//read a single letter from the line
		// If we hit EOF, replace it with a null character and return.
		//letter is defined as int type because of EOF
		if (letter == EOF || letter == '\n')
		{
			buffer[position] = '\0';
			return buffer;
		}
		else
		{
			buffer[position] = letter;
		}
		position++;
		// if the buffer is too small for our use than reallocate
		if (position >= buffSize)
		{
			buffSize += BUFFLINESIZE;
			buffer = realloc(buffer, buffSize);
			if (buffer == NULL)//check if allocation failed
			{
				errorAllocationFailed();//send message to screen and abort
			}
		}
	}

}

void errorAllocationFailed()
{
	fprintf(stderr, "SuperShell: allocation error\n");
	exit(EXIT_FAILURE);
}

char** superShellSplitLine(char* line)
{
	int buffSize = BUFF_TOKEN_ALLOCATED_STRING, indexInTokensArray = 0;
	char** listOfArgs = malloc(buffSize * sizeof(char*));//allocate array of strings
	char* token;//this is the separated string
	if (listOfArgs == NULL)
	{
		errorAllocationFailed();
	}
	token = strtok(line, SUPERSHELL_TOK_DELIM);
	while (token != NULL)
	{
		listOfArgs[indexInTokensArray] = token;
		indexInTokensArray++;
		if (indexInTokensArray >= buffSize)
		{
			buffSize += BUFF_TOKEN_ALLOCATED_STRING;
			listOfArgs = realloc(listOfArgs, buffSize * sizeof(char*));
			if (listOfArgs == NULL)
			{
				errorAllocationFailed();
			}
		}
		token = strtok(NULL, SUPERSHELL_TOK_DELIM);
	}
	listOfArgs[indexInTokensArray] = NULL;
	return listOfArgs;
}

int superShellLaunch(char** args)
{
	pid_t pid, wpid;
	int status;
	pid = fork();
	if (pid == 0)
	{
		// Child process
		int fd0, fd1, i, in = 0, out = 0;//fd0,fd1 is a file descriptor, in/out will tell us if its in/out redirection
		char input[BUFF_CPY_FOR_REDIRECTION], output[BUFF_CPY_FOR_REDIRECTION];

		//check if '>' or '<' in line from user for redirection

		for (i = 0; args[i] != '\0'; i++)//run until end of string and find out if this string is '<' or '>'
		{
			if (strcmp(args[i], "<") == 0)//found the string in the listOfArgs
			{
				args[i] = NULL;//cut the string
				strcpy(input, args[i + 1]);//cpy the other part of the redirection
				in = 1;
			}

			if (strcmp(args[i], ">") == 0)
			{
				args[i] = NULL;
				strcpy(output, args[i + 1]);
				out = 1;
			}
		}

		//if '<'  was found in args[i] inputed by the user
		if (in)
		{
			if ((fd0 = open(input, O_RDONLY, 0)) < 0)
			{
				perror("Couldn't open input file");
				exit(0);
			}
			// dup2() copies content of fdo in input of preceeding file
			dup2(fd0, STDIN_FILENO); // STDIN_FILENO here can be replaced by 0 and equals to the fd that describes input

			close(fd0); // necessary
		}

		//if '>' char was found args[i] inputed by the userr
		if (out)
		{
			if ((fd1 = creat(output, 0777)) < 0)
			{
				perror("Couldn't open the output file");
				exit(0);
			}

			dup2(fd1, STDOUT_FILENO); // 1 here can be replaced by STDOUT_FILENO
			close(fd1);
		}

		if (execvp(args[0], args) == -1)
		{
			perror("Super Shell");
		}
		exit(EXIT_FAILURE);
	}
	else if (pid < 0)
	{
		// Error forking
		perror("Super Shell");
	}
	else
	{
		// Parent process
		wpid = waitpid(pid, &status, 0);
	}
	return 1;
}


int superShellExec(char** args)
{
	if (args[0] == NULL)
	{
		// An empty command was entered.
		return 1;
	}

	//this if-else is meant to identify the relevant function to be executed

	if (strcmp(args[0], "cd") == 0)//check if cd was entered by the user
	{
		return myCd(args);
	}
	else if (strcmp(args[0], "encryptFile") == 0)
	{
		char* num;
		num = args[2];
		if (args[1] == NULL || args[2] == NULL || args[3] == NULL)
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else if (num[0] == '-')/*check if args sent to the superShell are valid*/
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else
		{
			if (atoi(args[2]) > 0 && atoi(args[2]) < 101) //int encryptFile(char* srcFile,int num,char* destFile);
			{
				return encryptFile(args[1], atoi(args[2]), args[3]);
			}
		}

	}
	return superShellLaunch(args);

}


int myCd(char** args)
{
	char path[PATH_LEN];
	if (args[1] == NULL)
	{
		return 1;
	}
	else if (strcmp(args[1], "..") == 0)//check if we need to go back
	{
		if (chdir("..") != 0)//use chdir function
		{
			perror("cd .. failed");
			return 1;
		}
	}
	else
	{
		if (chdir(strcat(strcat(getcwd(path, 100), "/"), args[1])) != 0) //getcwd gets the current path
		{
			perror("cd to path given failed");
		}
	}
	return 1;
}

int encryptFile(char* srcFile, int num, char* destFile)//param sent to the func are valid
{
	int fd_src, fd_dest, rbytes, wbytes, i;
	char buff[256];
	if ((fd_src = open(srcFile, O_RDONLY)) == -1)//open src file for reading
	{
		perror("open from");
		return(-1);
	}
	if ((fd_dest = open(destFile, O_WRONLY | O_CREAT, 0664)) == -1)//open dest file if exist otherwise create and write content
	{
		perror("open to");
		return(-1);
	}
	if ((rbytes = read(fd_src, buff, 256)) == -1)//start reading
	{
		perror("read source file");
		return(-1);
	}
	while (rbytes > 0)
	{
		for (i = 0; i < rbytes - 1; i++)
		{
			buff[i] = (buff[i] + num) % MODULU_ENCRYPTION;//encrypt
		}
		if ((wbytes = write(fd_dest, buff, rbytes)) == -1)
		{
			perror("write");
			return(-1);
		}
		if (wbytes != rbytes)
		{
			fprintf(stderr, "bad write\n");
			return(-1);
		}
		if ((rbytes = read(fd_src, buff, 256)) == -1)
		{
			perror("read dest file");
			return(-1);
		}
	}
	close(fd_src);
	close(fd_dest);
	return(1);
}



