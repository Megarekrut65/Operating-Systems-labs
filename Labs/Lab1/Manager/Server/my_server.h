#pragma once

#include <string>
#include <WS2tcpip.h>
#include <iostream>
#pragma comment(lib, "ws2_32.lib")
namespace ms
{
    using FunctionResult = int;
    using FunctionParam = int;
    class MyServer {
    private:
        std::string ip;
        const static std::size_t BUFFER_SIZE;
        int port;
        SOCKET sock;
        sockaddr_in get_address();
        void start_up();
        void create_socket();
    public:
        MyServer(const std::string &ip, int port);

        FunctionResult* run(FunctionParam x);
        void close();
        ~MyServer();
    };

}
