#pragma once
#include <string>
#include <tchar.h>
#include <iostream>
#include "Server/my_server.h"
/**
 * My process
 */
namespace mp
{
    using namespace ms;
    class MyProcess {
    private:
        std::string app_path;
        std::string ip;
        int port;
        PROCESS_INFORMATION pi;
        void create_new_process();
    public:
        MyProcess(const std::string &app_path,const std::string& ip, int port);
        FunctionResult* get_result(FunctionParam x);
    };
}

