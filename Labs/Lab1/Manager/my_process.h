#pragma once
#include <string>
#include <windows.h>
#include <tchar.h>
#include <thread>
#include <iostream>
/**
 * My process
 */
namespace mp
{
    using FunctionResult = int;
    class MyProcess {
    private:
        std::string app_path;
        std::string ip;
        int port;
        PROCESS_INFORMATION pi;
        void create_new_process();
    public:
        MyProcess(const std::string &app_path, int port);
        FunctionResult get_result();
    };
}

