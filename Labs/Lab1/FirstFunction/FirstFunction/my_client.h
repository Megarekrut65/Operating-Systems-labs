#pragma once
#include <iostream>
#include <string>
#include <WS2tcpip.h>
#pragma comment(lib, "ws2_32.lib")

namespace mc
{
	class MyClient
	{
    private:
        std::string ip;
        const static std::size_t BUFFER_SIZE;
        int port;
        SOCKET sock;
        sockaddr_in get_address();
        void start_up();
        void create_socket();
        std::string func(int x);
    public:
        MyClient(const std::string& ip, int port);

        void run();
        void close();
        ~MyClient();
	};
}


