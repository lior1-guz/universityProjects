#include "windows.h"
#include "SocketListener.h"
#include <synchapi.h>
#include <winsock.h>

SocketListener::SocketListener(int msgType, int port, int bufSize, RecorderParser *parser)
{
	_msgType = msgType;
	_port = port;
	_bufSize = bufSize;
	_parser = parser;
	_listenerThread = NULL;
}

SocketListener::~SocketListener()
{
	delete _parser;
	delete _listenerThread;
}

/*Create socket, bind and wait for a message*/
void SocketListener::CreateSocket() {
	// Initialize Windows Socket support
	WSADATA data;
	WORD version = MAKEWORD(2, 2);
	// Start WinSock
	int wsOk = WSAStartup(version, &data);
	if (wsOk != 0)
	{
		cout << "Can't start Winsock! " << wsOk;
		return;
	}

	SOCKET in = socket(AF_INET, SOCK_DGRAM, 0); //UDP

	// Create a server hint structure for the server
	sockaddr_in serverHint;
	serverHint.sin_addr.S_un.S_addr = INADDR_ANY; // Use any IP address available on the machine
	serverHint.sin_family = AF_INET; // Address format is IPv4
	serverHint.sin_port = htons(_port); // Convert from little to big endian

	// Try and bind the socket to the IP and port
	if (bind(in, (sockaddr*)&serverHint, sizeof(serverHint)) == SOCKET_ERROR)
	{
		cout << "Can't bind socket" << _port << WSAGetLastError() << endl;
		return;
	}

	sockaddr_in client; // Use to hold the client information (port / ip address)
	int clientLength = sizeof(client); // The size of the client information

	char* buf{ new char[_bufSize] {} }; // Buffer to hold the message to be recieved

	while (true)
	{
		ZeroMemory(&client, clientLength); // Clear the client structure
		ZeroMemory(buf, _bufSize); // Clear the receive buffer

		cout << "\nWaiting for data..." << endl ;
		// Wait for message
		int bytesIn = recvfrom(in, buf, _bufSize, 0, (sockaddr*)&client, &clientLength);
		if (bytesIn == SOCKET_ERROR)
		{
			cout << "Error receiving from client " << WSAGetLastError() << endl;
			continue;
		}
		
		cout << "Data recieved through port " << _port << " Message type is " << _msgType << endl;
		// Handle message
		OnMessage(buf);
	}

	// Close socket
	closesocket(in);

	// Shutdown winsock
	WSACleanup();
}

void SocketListener::Init()
{
	_listenerThread = new thread(&SocketListener::CreateSocket, this); //Initialize a thread that will create a socket and listen on the desired port 
}
void SocketListener::StartListening()
{
	_listenerThread->join(); // Wait for the thread
}

void SocketListener::OnMessage(string msg) {
		
	_parser->Parse(_msgType, msg);
	
}
