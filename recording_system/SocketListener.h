#include "RecorderParser.h"
#include <iostream>
#include <thread>
#pragma comment (lib, "ws2_32.lib")

class SocketListener
{
public:
	SocketListener(int msgType, int port, int bufSize, RecorderParser *parser);
	virtual ~SocketListener();
	void StartListening();
	void Init();

private:
	void CreateSocket();
	void OnMessage(string msg);
	
	int _msgType;
	int _port;
	int _bufSize;
	RecorderParser *_parser;
	thread *_listenerThread;

};

