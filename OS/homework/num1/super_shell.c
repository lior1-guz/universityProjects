/*
 * ex1.c
 *
 *  Created on: Mar 29, 2021
 *      Author: braude
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <time.h>
#include <stdbool.h>


#define SUPERSHELL_TOK_DELIM "\t\r\n\a "
#define BUFFLINESIZE 512
#define BUFF_TOKEN_ALLOCATED_STRING 4
#define BUFF_CPY_FOR_REDIRECTION 128
#define PATH_LEN 100
#define MODULU_ENCRYPTION 256
#define TIMEOUT_ARRAY 15


typedef struct timeOut {
	char stopedCmd[30];
	struct timeval startTime;
	int secToStop;
	bool active;
}timeOut;

timeOut arrayOfLockedCmds[TIMEOUT_ARRAY];//global array of locked cmds

void superShellLoop();
char* superShellReadLine();
char** superShellSplitLine(char* line);
int superShellExec(char** args);
void errorAllocationFailed();
int superShellLaunch(char** args);
int myCd(char** args);
int encryptFile(char* srcFile, int num, char* destFile);
int decryptFile(char* srcFile, int num, char* destFile);
void initTimeOut();
int lockCmdForTime(char* cmdNameToStop, int secToStop);
bool checkIfExist(char* checkCmdNameToStop, int* index);
void deleteFromLockedArray(int indexOfLockedToDel);
int letterFreq(char* srcFile);
int uppercaseByIndex(char* srcFile, char* destFile, int index);
int lowercaseByIndex(char* srcFile, char* destFile, int index);



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
	initTimeOut();
	do {
		printf("SuperShell> ");//prints the prompt to the screen
		lineFromSuperShell = superShellReadLine();//get line from user
		listOfArgs = superShellSplitLine(lineFromSuperShell);/*split the lines*/
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
	int indexOfLockedCmd;
	char* num;
	if (args[0] == NULL)
	{
		// An empty command was entered.
		return 1;
	}
	//this if-else is meant to identify the relevant function to be executed

	//check first if lock is active and this is the locked cmd and time hasnt passed print "locked..." if this is the cmd and the  time has passed timeOut->active =false
	struct timeval currentTime;
	if (checkIfExist(args[0], &indexOfLockedCmd) == true)//if the cmd is found in the locked cmd array
	{
		gettimeofday(&currentTime, NULL);
		if (currentTime.tv_sec - arrayOfLockedCmds[indexOfLockedCmd].startTime.tv_sec > arrayOfLockedCmds[indexOfLockedCmd].secToStop)
		{
			deleteFromLockedArray(indexOfLockedCmd);//delete from array of locked cmds
		}
		else//the time has not passed
		{
			fprintf(stderr, "The command %s is now locked!\n", args[0]);
			return 1;
		}
	}

	if (strcmp(args[0], "cd") == 0)//check if cd was entered by the user
	{
		return myCd(args);
	}
	else if (strcmp(args[0], "encryptFile") == 0)
	{
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
	else if (strcmp(args[0], "decryptFile") == 0)
	{
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
			if (atoi(args[2]) > 0 && atoi(args[2]) < 101) //int decryptFile(char* srcFile,int newNum(negative),char* destFile)
			{
				return decryptFile(args[1], atoi(args[2]), args[3]);
			}
		}
	}
	else if (strcmp(args[0], "lockCmdForTime") == 0)
	{
		num = args[2];
		if (args[1] == NULL || args[2] == NULL)
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
			return lockCmdForTime(args[1], atoi(args[2]));
		}
	}
	else if (strcmp(args[0], "letterFreq") == 0)
	{
		if (args[1] == NULL)
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
		}
		else
		{
			return letterFreq(args[1]);
		}
	}
	else if (strcmp(args[0], "uppercaseByIndex") == 0)
	{

		num = args[3];
		if (args[1] == NULL || args[2] == NULL || args[3] == NULL)
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else if (num[0] == '-')
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else
		{
			return uppercaseByIndex(args[1], args[2], atoi(args[3]));
		}
	}
	else if (strcmp(args[0], "lowercaseByIndex") == 0)
	{
		num = args[3];
		if (args[1] == NULL || args[2] == NULL || args[3] == NULL)
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else if (num[0] == '-')
		{
			fprintf(stderr, "SuperShell: Wrong param were delivered!\n");
			return 1;
		}
		else
		{
			return lowercaseByIndex(args[1], args[2], atoi(args[3]));
		}
	}
	// continue here
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

int decryptFile(char* srcFile, int num, char* destFile)
{
	int newNum = (-1) * num;
	return encryptFile(srcFile, newNum, destFile);
}

void initTimeOut()
{
	int i;
	for (i = 0; i < TIMEOUT_ARRAY; i++)//this loop initializing the arrayOfLockedCmds before use
	{
		arrayOfLockedCmds[i].active = false;
	}
}

int lockCmdForTime(char* cmdNameToStop, int secToStop)
{
	int i;
	for (i = 0; i < TIMEOUT_ARRAY; i++)
	{
		if (arrayOfLockedCmds[i].active == false)//isEmptyPlace -> start initializing it with blocked cmd
		{
			arrayOfLockedCmds[i].active = true;
			arrayOfLockedCmds[i].secToStop = secToStop;
			gettimeofday(&(arrayOfLockedCmds[i].startTime), NULL);
			strcpy(arrayOfLockedCmds[i].stopedCmd, cmdNameToStop);
			break;
		}
	}
	return 1;
}

bool checkIfExist(char* checkCmdNameToStop, int* index)
{
	int i;
	for (i = 0; i < TIMEOUT_ARRAY; i++)
	{
		if (strcmp(arrayOfLockedCmds[i].stopedCmd, checkCmdNameToStop) == 0)
		{
			if (arrayOfLockedCmds[i].active == true)
			{
				*index = i;
				return true;
			}
		}
	}
	return false;
}

void deleteFromLockedArray(int indexOfLockedToDel)
{
	arrayOfLockedCmds[indexOfLockedToDel].active = false;
	strcpy(arrayOfLockedCmds[indexOfLockedToDel].stopedCmd, "");
}

int letterFreq(char* srcFile)
{
	int fd_src, rbytes, i, j, maxTemp = 0, totalLetInFile = 0;
	int letters[26] = { 0 };
	char maxChar[3];
	float maxAppearence[3];
	char tempChar[2];
	if ((fd_src = open(srcFile, O_RDONLY)) == -1)//open src file for reading
	{
		perror("open from");
		return(-1);
	}

	if ((rbytes = read(fd_src, tempChar, 1)) == -1)//start reading
	{
		perror("read source file");
		return(-1);
	}

	while (rbytes > 0)
	{
		if (tempChar[0] >= 'A' && tempChar[0] <= 'Z')
		{
			letters[tempChar[0] - 'A']++;
			totalLetInFile++;

		}

		if (tempChar[0] >= 'a' && tempChar[0] <= 'z')
		{
			letters[tempChar[0] - 'a']++;
			totalLetInFile++;
		}

		if ((rbytes = read(fd_src, tempChar, 1)) == -1)//start reading
		{
			perror("read source file");
			return(-1);
		}
	}

	for (i = 0; i < 3; i++)
	{
		for (j = 0; j < 26; j++)//finds out who is the max char
		{
			if (letters[j] > maxTemp)
			{
				maxTemp = letters[j];
				maxAppearence[i] = letters[j];
				maxChar[i] = j + 'a';
			}
		}
		letters[maxChar[i] - 'a'] = 0;
		maxTemp = 0;
	}

	for (i = 0; i < 3; i++)
	{
		fprintf(stdout, "%c - %.1f%\n", maxChar[i], (maxAppearence[i] * 100) / totalLetInFile);
	}

	if ((maxChar[0] == 'e' && maxChar[1] == 'a') && (maxChar[2] == 'o' || maxChar[2] == 'i'))
		fprintf(stdout, "Good Letter Frequency\n");
	close(fd_src);
	return 1;
}

int uppercaseByIndex(char* srcFile, char* destFile, int index)
{
	int fd_src, fd_dest, rbytes, wbytes, i, j = 0, start = 0, end = 0;
	char buff[256];
	if ((fd_src = open(srcFile, O_RDONLY)) == -1)//open src file for reading
	{
		perror("open from");
		return(-1);
	}
	//if file does not exist
	if ((fd_dest = open(destFile, O_WRONLY | O_CREAT | O_TRUNC, 0664)) == -1)//open dest file if exist otherwise create and write content
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
		while (buff[j] != '\0')//until end of string
		{
			if (buff[j] == ' ' || buff[j] == '\n')//if we are on endl or on space
			{
				end = j;
				for (i = start; i < j; i++)
				{
					if (start + index == i && (buff[i] >= 'a' && buff[i] <= 'z'))
					{
						buff[i] -= 32;
					}
				}
				start = end + 1;
			}
			j++;
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
	return 1;
}

int lowercaseByIndex(char* srcFile, char* destFile, int index)
{
	int fd_src, fd_dest, rbytes, wbytes, i, j = 0, start = 0, end = 0;
	char buff[256];
	if ((fd_src = open(srcFile, O_RDONLY)) == -1)//open src file for reading
	{
		perror("open from");
		return(-1);
	}
	//if file does not exist
	if ((fd_dest = open(destFile, O_WRONLY | O_CREAT | O_TRUNC, 0664)) == -1)//open dest file if exist otherwise create and write content
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
		while (buff[j] != '\0')//until end of string
		{
			if (buff[j] == ' ' || buff[j] == '\n')//if we are on endl or on space
			{
				end = j;
				for (i = start; i < j; i++)
				{
					if (start + index == i && (buff[i] >= 'A' && buff[i] <= 'Z'))
					{
						buff[i] += 32;
					}
				}
				start = end + 1;
			}
			j++;
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
	return 1;
}

