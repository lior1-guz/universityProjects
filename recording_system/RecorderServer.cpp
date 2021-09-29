#include "RecorderServer.h"
#include "RecorderParser.h"

RecorderServer::RecorderServer()
{
	_parser = new RecorderParser();
	_listenerHeader = new SocketListener(_parser->TYPE_HEADER, PORT_HEADER, BUF_SIZE_HEADER, _parser);
	_listenerMessage = new SocketListener(_parser->TYPE_MSG, PORT_MSG, BUF_SIZE_MSG, _parser);
}

RecorderServer::~RecorderServer()
{
	delete _parser;
	delete _listenerHeader;
	delete _listenerMessage;
}

void RecorderServer::Start()
{
	_parser->Init();

	//Initialize threads and start listening on 2 ports (for header/msg)
	_listenerHeader->Init();
	_listenerMessage->Init();

	_listenerHeader->StartListening();
	_listenerMessage->StartListening();
}

void RecorderServer::Stop()
{
	_parser->Clear();
}
