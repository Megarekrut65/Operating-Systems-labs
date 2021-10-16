#pragma once

#include <string>
#include <WS2tcpip.h>
#include <iostream>
#include "../Addition classes and functions/my_printer.h"
#pragma comment(lib, "ws2_32.lib")
namespace mys
{
    using other::Printer;
    using FunctionResult = int;
    using FunctionParam = int;
    /**
     * \brief Socket server to send x to other program in this PC and receive result of function
     */
    class MyServer {
    private:
        std::string ip;/*Server ip*/
        const static std::size_t BUFFER_SIZE;/*Size of sending and receiving buffer*/
        int port;/*Connection port*/
        SOCKET sock;
        const int MAX_ERRORS;/*Max number of errors during function calculating*/
        bool is_close;
        sockaddr_in get_address();
        void start_up();
        void create_socket();
        /**
         *
         * @param buffer - result of function
         * @param hard_fail - if in function was hard fail than there be true
         * @param soft_fail - if in function was soft fail than there be true
         * @return function result if it was calculated or nullptr else
         */
        FunctionResult* normalize_result(char* buffer, bool& hard_fail, bool& soft_fail);
    public:
        MyServer(const std::string &ip, int port);
        /**
         *
         * @param x - function parameter
         * @return function result or nullptr
         *
         * \brief Starts server and wait for result
         */
        FunctionResult* run(FunctionParam x);
        void close();
        ~MyServer();
    };

}
