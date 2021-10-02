#pragma once
#include <string>
#include <windows.h>
#include <cstdio>
#include <tchar.h>
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

