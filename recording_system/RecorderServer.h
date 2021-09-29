#pragma once
#include "RecorderParser.h"
#include "SocketListener.h"

class RecorderServer
{
public:
	RecorderServer();
	virtual ~RecorderServer();
	void Start();
	void Stop();

private:
	const int PORT_HEADER = 54000;
	const int PORT_MSG = 54001;
	const int BUF_SIZE_HEADER = 50;
	const int BUF_SIZE_MSG = 500;

	RecorderParser *_parser;
	SocketListener *_listenerHeader;
	SocketListener *_listenerMessage;
};

