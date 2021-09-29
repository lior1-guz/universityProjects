#include "RecorderParser.h"
#include <vector>
#include <string>
#include <sstream>

/*Opening files*/
void RecorderParser::Init()
{
	try {
		_outputFile.open(FILE_OUT);
	}
	catch (...) {
		cout << "RecorderParser::Init - File open failed";
	}
}

/*Parsing the recieved packet and writing the combined data to the desired output*/
void RecorderParser::Parse(int msgType, string msg)
{
	// Lock the thread, will be unlocked on function exit
	lock_guard<mutex> lg(door);
	
	if (msg.empty()) {
		cout << "RecorderParser::Parse - Empty message recieved";
		return;
	}
	// If msg type is 2 (Header) then the message is assumed as (comma separated): headerID,NumOfPackets,HEADER Data 
	// If msg type is 1 (Msg) then the message is assumed as(comma separated) : headerID,MSG Data
	// HEADER: 1,3,my data
	// MSG: 1,my data
	auto arr = Split(msg.c_str(), ','); 
	int headerID = atoi(arr[0].c_str()); //Both msg types have headerID as first value
	
	if (msgType == TYPE_HEADER) {
		_mapDataPacketPerID[headerID].numOfExpectedPackets = atoi(arr[1].c_str()); //Extract number of packets from recieved msg and insert into map
		_mapDataPacketPerID[headerID].dataHeader = arr[2]; // Extract header data from recieved msg and insert into map
	}
	else { // Msg type is MSG
		_mapDataPacketPerID[headerID].dataMsgList.push_back(arr[1].c_str()); // Extract msg data from recieved msg and insert into a list in the map
	}

	// If it's time to write all data => meaning a map enrty's msg list contains numOfExpectedPackets
	if (_mapDataPacketPerID[headerID].numOfExpectedPackets > 0 && _mapDataPacketPerID[headerID].dataMsgList.size() == _mapDataPacketPerID[headerID].numOfExpectedPackets) {
		
		// Write data to file
		_outputFile << _mapDataPacketPerID[headerID].dataHeader << endl; // First write Header data to file
		for (list<string>::iterator it = _mapDataPacketPerID[headerID].dataMsgList.begin(); it != _mapDataPacketPerID[headerID].dataMsgList.end(); ++it)
		{
			_outputFile << ' ' << *it << endl; // Secondly, write all msgs related to the header
		}
		// Clear written data from map
		_mapDataPacketPerID[headerID].dataMsgList.clear();
		_mapDataPacketPerID.erase(headerID);
	}
}
/*Splitting recieved message by ',' into a string vector*/
vector<string> RecorderParser::Split(const char* str, char c)
{
	vector<string> result;

	do
	{
		const char* begin = str;

		while (*str != c && *str)
			str++;

		result.push_back(string(begin, str));
	} while (0 != *str++);

	return result;
}

/*Closing all opened files*/
void RecorderParser::Clear()
{
	_outputFile.close();
}