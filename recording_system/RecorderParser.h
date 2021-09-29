#pragma once
#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <map>
#include <mutex>

using namespace std;

class RecorderParser
{
public:
	const int TYPE_HEADER = 2;
	const int TYPE_MSG = 1;
	const string FILE_OUT = "rec.txt";

	void Init();
	void Parse(int msgType, string msg);
	void Clear();
	
	//DataPacket structure - used as value in the map
	struct DataPacket {
		int numOfExpectedPackets = 0;
		string dataHeader;
		std::list<string> dataMsgList;
	};

	// Map of DataPackets
	map<int, DataPacket> _mapDataPacketPerID;
private:
	vector<string> Split(const char* str, char c);
	ofstream  _outputFile;
	// Thread lock object
	std::mutex door;
};

