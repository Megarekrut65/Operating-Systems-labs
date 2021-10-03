#include "my_server.h"

namespace ms
{
    const std::size_t MyServer::BUFFER_SIZE = 1024;
    MyServer::MyServer(const std::string &ip, int port) :
    ip{ip}, port{port}, sock{0} {
        start_up();
        create_socket();
    }
    void MyServer::start_up() {
        WSAData data;
        WORD ver = MAKEWORD(2, 2);
        int result = WSAStartup(ver, &data);
        if (result != 0)
        {
            std::cerr << "Can't start WinSock, Err #" << result << std::endl;
        }
    }

    void MyServer::create_socket() {
        sock = socket(AF_INET, SOCK_STREAM, 0);
        if (sock == INVALID_SOCKET)
        {
            std::cerr << "Can't create socket, Err #" << WSAGetLastError() << std::endl;
            WSACleanup();
        }
    }
    sockaddr_in MyServer::get_address() {
        sockaddr_in hint;
        hint.sin_family = AF_INET;
        hint.sin_port = htons(port);
        inet_pton(AF_INET, ip.c_str(), &hint.sin_addr);
        return hint;
    }

    FunctionResult* MyServer::run(FunctionParam x) {
        sockaddr_in address = get_address();
        if(bind(sock, (sockaddr*)&address, sizeof(address)) == SOCKET_ERROR)
        {
            std::cerr << "Can't bind socket!" << std::endl;
            return nullptr;
        }
        listen(sock, SOMAXCONN);
        SOCKET client = accept(sock, nullptr, nullptr);
        send(client, std::to_string(x).c_str(), BUFFER_SIZE, 0);
        char buffer[BUFFER_SIZE];
        int res = recv(client, buffer, BUFFER_SIZE, 0);
        if(res != SOCKET_ERROR) return new FunctionResult(atoi(buffer));
        return nullptr;
    }

    void MyServer::close() {
        WSACleanup();
        closesocket(sock);
    }

    MyServer::~MyServer() {
        close();
    }
}