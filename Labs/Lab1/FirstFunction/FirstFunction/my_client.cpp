#include "my_client.h"
namespace mc
{
    const std::size_t MyClient::BUFFER_SIZE = 1024;
    MyClient::MyClient(const std::string& ip, int port) :
        ip{ ip }, port{ port }, sock{ 0 } {
        start_up();
        create_socket();
    }
    void MyClient::start_up() {
        WSAData data;
        WORD ver = MAKEWORD(2, 2);
        int result = WSAStartup(ver, &data);
        if (result != 0)
        {
            std::cerr << "Can't start WinSock, Err #" << result << std::endl;
        }
    }

    void MyClient::create_socket() {
        sock = socket(AF_INET, SOCK_STREAM, 0);
        if (sock == INVALID_SOCKET)
        {
            std::cerr << "Can't create socket, Err #" << WSAGetLastError() << std::endl;
            WSACleanup();
        }
    }
    sockaddr_in MyClient::get_address() {
        sockaddr_in hint;
        hint.sin_family = AF_INET;
        hint.sin_port = htons(port);
        inet_pton(AF_INET, ip.c_str(), &hint.sin_addr);
        return hint;
    }
    std::string MyClient::func(int x)
    {
        if (x == -1)
        {
            throw std::logic_error{"Function(-1) = undef"};
        }
        return std::to_string(x * x);
    }
    void MyClient::run() {
        sockaddr_in address = get_address();
        if (connect(sock, (sockaddr*)&address, sizeof(address)) == SOCKET_ERROR)
        {
            std::cerr << "Can't connect to server!" << std::endl;
            return;
        }
        char buffer[BUFFER_SIZE];
        int res = recv(sock, buffer, BUFFER_SIZE,0);
        if (res != SOCKET_ERROR) send(sock, func(atoi(buffer)).c_str(), BUFFER_SIZE, 0);
    }

    void MyClient::close() {
        WSACleanup();
        closesocket(sock);
    }

    MyClient::~MyClient() {
        close();
    }
}