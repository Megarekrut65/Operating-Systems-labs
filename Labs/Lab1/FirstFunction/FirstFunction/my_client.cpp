#include "my_client.h"
namespace mc
{
    const std::size_t MyClient::BUFFER_SIZE = 1024;
    MyClient::MyClient(const std::string& ip, int port) :
        ip{ ip }, port{ port }, sock{ 0 }, MAX_ERRORS{ 10 } {
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
    Result MyClient::func(int x)
    {
        try {
            return os::lab1::compfuncs::trial_f<os::lab1::compfuncs::INT_SUM>(x);
        }
        catch (std::out_of_range& e) {
            return std::get<os::lab1::compfuncs::hard_fail>(Result());
        }
    }
    void MyClient::run() {
        sockaddr_in address = get_address();
        if (connect(sock, (sockaddr*)&address, sizeof(address)) == SOCKET_ERROR)
        {
            std::cerr << "Can't connect to server!" << std::endl;
            return;
        }
        int errors = 0;
        bool hard_fail = false, soft_fail = false;
        do {
            char buffer[BUFFER_SIZE];
            int err = recv(sock, buffer, BUFFER_SIZE, 0);
            auto res = func(atoi(buffer));
            hard_fail = std::holds_alternative<os::lab1::compfuncs::hard_fail>(res);
            soft_fail = std::holds_alternative<os::lab1::compfuncs::soft_fail>(res);
            std::stringstream ss;
            if (hard_fail || soft_fail) ss << "Function f: " << res;
            else ss << std::get<int>(res);
            if (err != SOCKET_ERROR) send(sock, ss.str().c_str(), BUFFER_SIZE, 0);
            errors++;
        } while (!hard_fail && soft_fail && errors < MAX_ERRORS);
    }

    void MyClient::close() {
        WSACleanup();
        closesocket(sock);
    }

    MyClient::~MyClient() {
        close();
    }
}