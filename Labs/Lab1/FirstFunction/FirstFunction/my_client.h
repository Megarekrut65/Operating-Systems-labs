#pragma once
#include <iostream>
#include <string>
#include <sstream>
#include <type_traits>
#include "Functions/trialfuncs.hpp"
#include <WS2tcpip.h>
#pragma comment(lib, "ws2_32.lib")

namespace mc
{
    using Result = os::lab1::compfuncs::comp_result<int>;
	class MyClient
	{
    private:
        std::string ip;
        const static std::size_t BUFFER_SIZE;
        int port;
        SOCKET sock; 
        const int MAX_ERRORS;
        sockaddr_in get_address();
        void start_up();
        void create_socket();
        Result func(int x);
    public:
        MyClient(const std::string& ip, int port);

        void run();
        void close();
        ~MyClient();
	};
}


